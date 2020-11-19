package frames;


import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

public class CommandCSGetFile extends BaseCommandFrame {

    @Setter @Getter
    private int currentFrame;

    @Setter @Getter
    private String relativePath;

    public final int FRAME_SIZE = 16384;

    public CommandCSGetFile(int currentFrame, String relativePath) {
        this.currentFrame = currentFrame;
        this.relativePath = relativePath;
    }

    /**
     * Запрос с клиента на передачу данных с сервера на клиент
     * Наименование файла передается в качестве строкового параметра
     * Номер требуемого кадра в качестве следующего long
     * Получаем GET, в ответ отправляем кадр
     */
    @Override
    public BaseFrame processing(ChannelHandlerContext ctx) {
        return null;
    }


}
