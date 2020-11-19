package frames;

import fileobjects.FileList;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

public class CommandSCDir extends BaseCommandFrame {

    @Getter
    @Setter
    private FileList fileList;

    public CommandSCDir() {
        fileList = new FileList();
    }


    @Override
    public BaseFrame processing(ChannelHandlerContext ctx) {
        return null;
    }
}
