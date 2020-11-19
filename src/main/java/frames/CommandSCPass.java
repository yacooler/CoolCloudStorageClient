package frames;

import io.netty.channel.ChannelHandlerContext;

public class CommandSCPass extends BaseCommandFrame {
    public CommandSCPass(String content) {
        super(content);
    }
    @Override
    public BaseFrame processing(ChannelHandlerContext ctx) {
        return null;
    }
}
