package netty;


import frames.BaseFrame;
import frames.CommandCSGetFile;
import frames.DataFrame;
import io.netty.channel.ChannelHandlerContext;
import javafx.util.Callback;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetRemoteFile extends BaseCommandHandler implements Callback<BaseFrame, BaseFrame> {

    private DataFrame dataFrame;
    private CommandCSGetFile commandCSGetFile;
    private OutputStream outputStream;
    private Path destinationDir;
    private String relativePath;

    public double getProgress(){
        return (dataFrame.getCurrentFrame() + 1d) / (dataFrame.getLastFrame() + 1d);
    }

    public GetRemoteFile(ChannelHandlerContext channelHandlerContext) {
        super(channelHandlerContext);
    }

    public void request(String relativePath, Path destinationDir){
        this.destinationDir = destinationDir;
        this.relativePath = relativePath;

        //инициализируем скачивание
        commandCSGetFile = new CommandCSGetFile(0, relativePath);
        channelHandlerContext.writeAndFlush(commandCSGetFile);
    }

    @Override
    public BaseFrame call(BaseFrame param) {
        if (!(param instanceof DataFrame)) return null;
        dataFrame = (DataFrame) param;

        try {
        //На нулевом кадре подготовительный этап
        if (dataFrame.getCurrentFrame() == 0) {
            System.out.println("Файл назначения: "  + destinationDir.resolve(Paths.get(relativePath).getFileName().toString()) );
            outputStream = Files.newOutputStream(destinationDir.resolve(Paths.get(relativePath).getFileName().toString()));
        }

        //Пишем пришедший кадр в файл
        outputStream.write(
                dataFrame.getContent(),
                0,
                dataFrame.getContentDataSize());

        //На последнем кадре закрываем стрим
        if (dataFrame.getCurrentFrame() == dataFrame.getLastFrame()) {
            outputStream.close();
            ready = true;
            return null;
        }

        } catch (IOException e) {
            throw new RuntimeException("Не удалось записать на локальный диск файл с удаленного сервера", e);
        }

        //Просим у сервера следующий кадр
        commandCSGetFile.setCurrentFrame(dataFrame.getCurrentFrame() + 1);

        return commandCSGetFile;


    }
}
