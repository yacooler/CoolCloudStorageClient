package netty;

import io.netty.channel.ChannelHandlerContext;

public class BaseCommandHandler {
    protected ChannelHandlerContext channelHandlerContext;
    protected boolean ready;

    public BaseCommandHandler(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public boolean isReady() {
        return ready;
    }
}
