package netty;

import fileobjects.FileInformation;
import frames.*;
import io.netty.channel.ChannelHandlerContext;
import javafx.util.Callback;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PostRemoteFile extends BaseCommandHandler implements Callback<BaseFrame, BaseFrame> {

    private Path localFile;
    private String remoteDirectory;
    private InputStream inputStream;
    private DataFrame dataFrame;

    public PostRemoteFile(ChannelHandlerContext channelHandlerContext) {
        super(channelHandlerContext);
    }

    public void request(Path localFile, String remoteDirectory){
        this.localFile = localFile;
        this.remoteDirectory = remoteDirectory;

        //Отправили серверу команду на прием файла и путь к папке, куда файл должен
        //быть сохранён
        channelHandlerContext.writeAndFlush(new CommandCSPostFile(remoteDirectory));
    }

    @Override
    public BaseFrame call(BaseFrame param) {
        if (!(param instanceof CommandSCGetFile)) return null;

        CommandSCGetFile commandSCGetFile = (CommandSCGetFile) param;

        try {
            if (commandSCGetFile.getCurrentFrame() == 0){
                FileInformation fileInformation = new FileInformation(localFile);
                inputStream = Files.newInputStream(localFile);
                dataFrame = new DataFrame();
                dataFrame.setLastFrame((int) fileInformation.getSize() / commandSCGetFile.FRAME_SIZE);
                dataFrame.setContent(new byte[commandSCGetFile.FRAME_SIZE]);
                dataFrame.setFileInformation(fileInformation);
            }

            int readed;
            readed = inputStream.read(dataFrame.getContent());
            dataFrame.setContentDataSize(readed);
            dataFrame.setCurrentFrame(commandSCGetFile.getCurrentFrame());

            if (dataFrame.getCurrentFrame() == dataFrame.getLastFrame()){
                inputStream.close();
                ready = true;
                return null;
            }

        }
        catch (IOException e){
            throw new RuntimeException("Не удалось открыть файл на клиенте " + localFile.toString(), e);
        }

        return dataFrame;

    }
}
