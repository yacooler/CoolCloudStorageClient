package frames;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

public class CommandSCGetFile extends BaseCommandFrame {
    @Setter
    @Getter
    private int currentFrame;


    public final int FRAME_SIZE = 16384;

    public CommandSCGetFile(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    @Override
    public BaseFrame processing(ChannelHandlerContext ctx) {
        return null;
    }
}
