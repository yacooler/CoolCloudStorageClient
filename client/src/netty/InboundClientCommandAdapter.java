package netty;

import frames.BaseFrame;
import frames.CommandCSNewClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import javafx.util.Callback;

public class InboundClientCommandAdapter extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext currentHandlerContext;
    private AuthorizationLoop authorizationLoop;
    private Callback<BaseFrame, BaseFrame> receivedMessageHandler;



    public ChannelHandlerContext getCurrentHandlerContext() {
        return currentHandlerContext;
    }

    public AuthorizationLoop getAuthorizationLoop() {
        return authorizationLoop;
    }

    public void setReceivedMessageHandler(Callback<BaseFrame, BaseFrame> receivedMessageHandler) {
        this.receivedMessageHandler = receivedMessageHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //Пользователь подключился
        System.out.println("Клиент подключился к серверу");
        currentHandlerContext = ctx;
        System.out.println(authorizationLoop);
        ctx.writeAndFlush(new CommandCSNewClient());
        System.out.println(Thread.currentThread());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        BaseFrame response;
        BaseFrame command;
        System.out.println(Thread.currentThread());
        //Если пока не подключен обработчик события - ничего не делаем
        if (receivedMessageHandler == null) return;

        if (msg instanceof BaseFrame) {
            command = (BaseFrame) msg;
            response = receivedMessageHandler.call(command);
            if (response != null) {
                System.out.println(response);
                ctx.writeAndFlush(response);
            }
        } else {
            throw new UnsupportedMessageTypeException("Не удалось обработать сообщение. Ожидается объект класса BaseFrame!");
        }


        return;
    }
}



//                                    if (msg instanceof CommandCSLogin){
//                                        //Если мессейдж - логин, запрашиваем его у пользователя в каком-то месте
//                                        System.out.println("введите логин");
//                                        //login = scanner.next();
//                                        command.setContent(login.getBytes());
//                                        System.out.println(new String(command.getContent()));
//                                        ctx.writeAndFlush(command);
//                                    } else if(msg instanceof CommandCSPass){
//                                        //Если мессейдж - пароль, запрашиваем его у пользователя
//                                        System.out.println("Введите пароль");
//                                        //password = scanner.next();
//                                        String pass = login + password;
//                                        System.out.println(pass);
//                                        command.setContent(AuthUtils.md5(pass).getBytes());
//                                        ctx.writeAndFlush(command);
//                                    } else if(msg instanceof CommandSCC){
//                                        System.out.println("Запрос списка файлов корневой директории");
//                                        ctx.writeAndFlush(new CommandDIR());
//                                    } else if(msg instanceof CommandDIR){
//                                        CommandDIR commandDIR = (CommandDIR) msg;
//                                        fileList = commandDIR.getFileList();
//                                        for (FileParameters fileParameters : fileList.getFiles()) {
//                                            System.out.println(fileParameters);
//                                        }
//                                        ctx.writeAndFlush(new CommandWAI());
//
//                                    } else if(msg instanceof DataFrameTRN) {
//                                        //Получение файла
//                                        DataFrameTRN dataFrameTRN = (DataFrameTRN) msg;
//                                        dataFrameTRN = dataFrameTRN;
//                                        //System.out.println("DataFrameTRN");
//                                        outputStream.write(
//                                                dataFrameTRN.getContent(),
//                                                0,
//                                                dataFrameTRN.getContentDataSize()
//                                        );
//
//                                        //Если всё скачали
//                                        if (dataFrameTRN.getCurrentFrame() == dataFrameTRN.getLastFrame()){
//                                            outputStream.close();
//                                            outputStream = null;
//                                            System.out.println("WAIT");
//                                            ctx.writeAndFlush(new CommandWAI());
//                                        } else {
//                                            //Иначе запрашиваем следующий кадр
//                                            CommandGET commandGET = new CommandGET(dataFrameTRN.getCurrentFrame() + 1);
//                                            //параметры файла
//                                            commandGET.setFileParameters(dataFrameTRN.getFileParameters());
//
//                                            ctx.writeAndFlush(commandGET);
//                                        }
//
//
//                                    } else {
//                                        System.out.println("Выберите файл на сервере:");
//                                        for (int i = 0; i < fileList.getFiles().size(); i++) {
//                                            System.out.println(String.format("[%d] %s", i, fileList.getFiles().get(i)));
//                                        }
//                                        int fileNumber = scanner.nextInt();
//                                        var fileParam = fileList.getFiles().get(fileNumber);
//
//
//                                        //Выбрали файл, запрашиваем первый фрейм, дальше запросы идут через DataFrame
//                                        if (fileParam != null) {
//                                            System.out.println("client/" + fileParam.getName());
//                                            outputStream = Files.newOutputStream(Path.of("client/" + fileParam.getName()));
//                                        }
//                                        //С первого кадра
//                                        CommandGET commandGET = new CommandGET(0);
//                                        //Файл
//                                        commandGET.setFileParameters(fileParam);
//                                        //Передаем параметры файла
//                                        ctx.writeAndFlush(commandGET);
//                                    }
