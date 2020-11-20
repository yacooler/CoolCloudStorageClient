package frames;

import io.netty.channel.ChannelHandlerContext;

public class CommandCSLogin extends BaseCommandFrame {
    public CommandCSLogin() {
        super();
    }

    public CommandCSLogin(byte[] content) {
        super(content);
    }

    public CommandCSLogin(String content) {
        super(content);
    }

    @Override
    protected void afterConstruct() {
        semantic = "LOG".getBytes();
    }

    /**
     *В случае, если клиент прислал логин - пытаемся найти пользователя с таким логином
     * По бизнес-процессу ищем этого пользователя в БД, а не в пуле, т.к. это может
     * быть новое соединение с другого устройства
     */

    @Override
    public BaseFrame processing(ChannelHandlerContext ctx){
        System.out.println("user sent login:" + getContentAsString());
        return null;
    }

}
