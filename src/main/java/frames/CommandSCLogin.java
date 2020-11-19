package frames;

import io.netty.channel.ChannelHandlerContext;

public class CommandSCLogin extends BaseCommandFrame {

    public CommandSCLogin(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "NEW".getBytes();
    }


    @Override
    public BaseFrame processing(ChannelHandlerContext ctx){
        System.out.println("response login from client");
        return null;
    }

}

