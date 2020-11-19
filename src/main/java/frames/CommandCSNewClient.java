package frames;

import io.netty.channel.ChannelHandlerContext;

public class CommandCSNewClient extends BaseCommandFrame {

    public CommandCSNewClient() {
        super();
    }

    @Override
    protected void afterConstruct() {
        semantic = "NEW".getBytes();
    }


    @Override
    public BaseFrame processing(ChannelHandlerContext ctx){
        System.out.println("new user connected and sent message NewClient");
        return new CommandSCLogin("server is waiting for login");
    }

}
