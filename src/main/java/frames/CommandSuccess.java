package frames;


import io.netty.channel.ChannelHandlerContext;

/**
 * Success. Команда отправляется в случае, если предыдущая операция завершена успешно
 */
public class CommandSuccess extends BaseCommandFrame {
    public CommandSuccess() {
        super();
    }

    public CommandSuccess(byte[] content) {
        super(content);
    }

    public CommandSuccess(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "SCC".getBytes();
    }

    @Override
    public BaseFrame processing(ChannelHandlerContext ctx){
        return null;
    }
}
