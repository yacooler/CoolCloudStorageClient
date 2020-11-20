package nettyhandlers;


import cloudusers.CloudServerUser;
import cloudusers.UserPool;
import database.entity.User;
import fileobjects.FileInformation;
import frames.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;


public class InboundCommandHandler extends ChannelInboundHandlerAdapter {

    //todo пофиксить баг с крашащимся потоком, если передали отсутствующее имя пользователя
    private UserPool userPool;


    public InboundCommandHandler(UserPool userPool) {
        this.userPool = userPool;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        userPool.addUser(ctx.channel(), new CloudServerUser());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userPool.removeUser(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //System.out.println("InboundCommandHandler");

        //Получили пользователя, с которым работаем
        CloudServerUser user = userPool.getUser(ctx.channel());

        if (msg instanceof BaseFrame) {

            BaseFrame response = null;

            if (msg instanceof BaseCommandFrame) {
                BaseCommandFrame frame = (BaseCommandFrame) msg;

                if(msg instanceof CommandCSLogin){
                    CommandCSLogin commandCSLogin = (CommandCSLogin) msg;
                    //Контекст обрабатываем по месту
                    //Если логин совпал - вычитали пользователя из БД и записали в текущего пользователя сервера
                    Optional<User> optionalUser = userPool.getUserService().findUser(commandCSLogin.getContentAsString());
                    if (optionalUser.isPresent()) {
                        System.out.println("login OK");
                        user.setDatabaseuser(optionalUser.get());
                    } else {
                        System.out.println("login FAIL");
                    }
                    //Пароль запрашиваем в любом случае, чтобы избежать атаки перебором на логины
                    response = new CommandSCPass("Server is waiting for the password");

                } else if(msg instanceof CommandCSPass){
                    CommandCSPass commandCSPass = (CommandCSPass) msg;

                    System.out.println("user sent password:" + commandCSPass.getContentAsString());
                    //Если пароль, пришедший от клиента совпал с паролем в базе - вышли в рабочий режим
                    if (user.getDatabaseuser().getPassword().equalsIgnoreCase(commandCSPass.getContentAsString())){
                        System.out.println("Ура, мы вычитали из базы для пользователя такой-же пароль!");
                        response = new CommandSuccess();
                    } else {
                        //иначе запрашиваем логин и пароль заново
                        response = new CommandCSLogin(CommandCSPass.FAIL_WHILE_AUTH);
                    }

                } else if(msg instanceof CommandCSDir){
                    //Получение списка директорий на сервере
                    response = commandCSDirProcessing((CommandCSDir) msg, user);

                } else if(msg instanceof CommandCSGetFile){
                    //Получение кадра файла на сервере
                    response = commandCSGetFileProcessing((CommandCSGetFile) msg, user);

                } else if(msg instanceof CommandCSPostFile){
                    //Клиент хочет передать нам файл - запрашиваем у него первый кадр (с номером 0)
                    user.setCurrentFileDirectory(((CommandCSPostFile) msg).getRelativePath());
                    System.out.println("Клиент запросил передачу файла на сервер в папку " + user.getCurrentFileDirectory());
                    response = new CommandSCGetFile(0);
                }

                if (response == null) response = frame.processing(ctx);

            } else if(msg instanceof DataFrame){
                response = commandPostFileDataFrameProcessing((DataFrame) msg, user, new CommandSCGetFile(0));
            }

            //Часть ответов формируется автоматически, внутри команд

            if (response != null) {
                ctx.writeAndFlush(response);
            }

        }

    }

    private BaseCommandFrame commandCSDirProcessing(CommandCSDir commandCSDir, CloudServerUser user){
        //Контекст обработаем по месту
        //Путь к папке для отображения корня
        Path path = user.getRootPath();


        //Путь к выбранной папке относительно корня
        if (!commandCSDir.getContentAsString().isEmpty()) path = path.resolve(commandCSDir.getContentAsString());

        System.out.println("Клиент запросил путь к папке " + path.toString());

        try {
            CommandSCDir response = new CommandSCDir();


            response.getFileList().addAll(Files.list(path).map(FileInformation::new).collect(Collectors.toList()));

            for (FileInformation fi: response.getFileList().getFiles()) {
                System.out.println(fi);
            }

            return response;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось получить список файлов командой dir",e );
        }

    }


    private DataFrame commandCSGetFileProcessing(CommandCSGetFile commandCSGetFile, CloudServerUser user){
        DataFrame dataFrame;

        //Если только запросили файл - закэшируем всё, что сможем
        if (commandCSGetFile.getCurrentFrame() == 0) {
            System.out.println("Пользователь запросил файл по относительному пути " + user.getRootPath() +" | "+  commandCSGetFile.getRelativePath() );
            user.setCurrentFileInformation(new FileInformation(user.getRootPath().resolve(commandCSGetFile.getRelativePath())));

            dataFrame = new DataFrame();
            dataFrame.setLastFrame((int) user.getCurrentFileInformation().getSize() / commandCSGetFile.FRAME_SIZE);
            dataFrame.setContent(new byte[commandCSGetFile.FRAME_SIZE]);
            dataFrame.setFileInformation(user.getCurrentFileInformation());

            user.setDataFrame(dataFrame);

            try {
                //Путь к файлу - корень пользователя + переданный относительный путь
                user.setInputStream(Files.newInputStream(user.getRootPath().resolve(commandCSGetFile.getRelativePath())));
            } catch (IOException e) {
                //todo сделать что-то полезное
                e.printStackTrace();
            }
        } else {
            dataFrame = user.getDataFrame();
        }

        //Устанавливаем нашему ответу те параметры, которые пользователь запрашивает
        dataFrame.setDataOffset(commandCSGetFile.getCurrentFrame() * commandCSGetFile.FRAME_SIZE);
        dataFrame.setCurrentFrame(commandCSGetFile.getCurrentFrame());

        int readed = 0;
//        reader.skip(currentFrame * FRAME_SIZE);
        //читаем содержимое файла в ответный кадр

        try {
            readed = user.getInputStream().read(dataFrame.getContent(), 0, commandCSGetFile.FRAME_SIZE);

            dataFrame.setContentDataSize(readed);

            //Если прочитали весь файл - закрываем стрим
            if (dataFrame.getCurrentFrame() == dataFrame.getLastFrame()) {
                user.getInputStream().close();
            }
        }
        catch (IOException ioe){
            throw new RuntimeException("Ошибка при чтении или закрытии файла", ioe);
        }

        //Возвращаем кадр
        return dataFrame;

    }

    public CommandSCGetFile commandPostFileDataFrameProcessing(DataFrame dataFrame, CloudServerUser user, CommandSCGetFile commandSCGetFile){
        //Если нам пришел первый пакет передаваемого файла - кэшируемся
        if (dataFrame.getCurrentFrame() == 0){

            user.setCurrentFileInformation(dataFrame.getFileInformation());

            Path path = user.getRootPath().resolve(user.getCurrentFileDirectory()).resolve(user.getCurrentFileInformation().getName());

            try {
                user.setOutputStream(Files.newOutputStream(path));
            } catch (IOException e) {
                throw new RuntimeException("Не удалось открыть файл для записи:" + path.toString(), e);
            }
        };

        try {
            user.getOutputStream().write(dataFrame.getContent(), 0, dataFrame.getContentDataSize());

            //для последнего пакета - закрываем стрим
            if (dataFrame.getLastFrame() == dataFrame.getCurrentFrame()) {
                user.getOutputStream().close();
                return null;
            }
        }
        catch (IOException ioe){
            throw new RuntimeException("Не удалось записать файл", ioe);
        }

        //Запрашиваем следующий кадр
        commandSCGetFile.setCurrentFrame(dataFrame.getCurrentFrame() + 1);

        return commandSCGetFile;
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        throw new RuntimeException("Ошибка Command Handler", cause);
    }
}
