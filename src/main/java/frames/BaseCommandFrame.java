package frames;

import io.netty.channel.ChannelHandlerContext;

public abstract class BaseCommandFrame extends BaseFrame {


    public BaseCommandFrame() {}

    public BaseCommandFrame(byte[] content) {
        super(content);
    }

    public BaseCommandFrame(String content) {
        super(content);
    }

    @Override
    public String getContentAsString() {
        return super.getContentAsString();
    }

    /**
     * Функция обработки пришедших в команду данных
     * Либо обрабатывается в наследнике, либо переопределяется по месту наличия контекста
     */
    public abstract BaseFrame processing(ChannelHandlerContext ctx);
}
