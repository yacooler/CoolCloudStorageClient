package cloudusers;


import database.services.UserService;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;


/**
 * Пул пользователей.
 * Ключ - соединение пользователя с сервером
 */
public class UserPool {
    //Хэшмапа соединений
    private Map<Channel, CloudServerUser> users = new HashMap<>();

    public Map<Channel, CloudServerUser> getUsers() {
        return users;
    }

    private UserService Service = new UserService();

    //При коннекте
    public void addUser(Channel channel,CloudServerUser user){
        users.put(channel, user);
    }

    //При дисконнекте
    public void removeUser(Channel channel){
        users.remove(channel);
    }

    //Получение пользователя из мапы
    public CloudServerUser getUser(Channel channel){
        return users.get(channel);
    }

    public UserService getUserService() {
        return Service;
    }
}
