package netty;

import fileobjects.FileList;
import frames.BaseFrame;
import frames.CommandCSDir;
import frames.CommandSCDir;
import io.netty.channel.ChannelHandlerContext;
import javafx.util.Callback;



public class GetRemoteFileList extends BaseCommandHandler implements Callback<BaseFrame, BaseFrame> {

    private FileList fileList;


    public GetRemoteFileList(ChannelHandlerContext channelHandlerContext) {
        super(channelHandlerContext);
    }

    public void request(String remoteDirectoty){
        channelHandlerContext.writeAndFlush(new CommandCSDir(remoteDirectoty));
    }

    /**
     *Получение ответа от удаленного сервера
     */
    @Override
    public BaseFrame call(BaseFrame param) {
        if (param instanceof CommandSCDir){
            fileList = ((CommandSCDir) param).getFileList();
            ready = true;
        };
        return null;
    }

    public FileList getFileList() {
        return fileList;
    }
}
