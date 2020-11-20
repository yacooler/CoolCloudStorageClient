package frames;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

public class CommandCSPostFile extends BaseCommandFrame{

    @Getter @Setter
    private String relativePath;

    public CommandCSPostFile(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public BaseFrame processing(ChannelHandlerContext ctx) {
        return null;
    }
}