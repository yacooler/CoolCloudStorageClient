package frames;

import io.netty.channel.ChannelHandlerContext;

public class CommandCSPass extends BaseCommandFrame {

    public static final String FAIL_WHILE_AUTH = "Логин или пароль некорректны";

    public CommandCSPass() {
        super();
    }

    public CommandCSPass(byte[] content) {
        super(content);
    }

    public CommandCSPass(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "PAS".getBytes();
    }

    @Override
    public BaseFrame processing(ChannelHandlerContext ctx){
        return null;
    }
}
