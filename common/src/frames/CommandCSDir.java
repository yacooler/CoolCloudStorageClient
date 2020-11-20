package frames;


import io.netty.channel.ChannelHandlerContext;


public class CommandCSDir extends BaseCommandFrame {

    public CommandCSDir(String content) {
        super(content);
    }

    /**
     * Получение данных о директории, путь к которой относительно root - папки запросил пользователь
     * В ответ на запрос возвращаем пришедший объект с заполненным fileList
     */
    @Override
    public BaseFrame processing(ChannelHandlerContext ctx) {
        return null;
    }
}
