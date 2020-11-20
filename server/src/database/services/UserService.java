package database.services;

import org.hibernate.Session;
import org.hibernate.query.Query;
import database.HibernateSessionFactory;
import database.entity.User;

import java.util.List;
import java.util.Optional;

public class UserService {

    public Optional<User> findUser(String name){
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("From User where name = :name").setParameter("name", name);
        List<User> list = (List<User>) query.list();
        session.close();

        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(0));
    }
}
