package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/rpg");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        //properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "root");
        properties.put(Environment.HBM2DDL_AUTO, "update");
        sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(Player.class)
                .buildSessionFactory();
    }




    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
//        Query<Player> query = session.createNativeQuery("select * from rpg.player limit = ':pageNumber' offset ':pageSize'", Player.class );
//        query.setParameter("pageNumber", pageNumber);
//        query.setParameter("pageSize", pageSize);
//        List<Player> players = query.getResultList();
//        session.getTransaction().commit();
//        session.close();

        Query<Player> query = session.createQuery("from rpg.player", Player.class);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        List<Player> players = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return players;
    }

    @Override
    public int getAllCount() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query<Long> query = session.createQuery("select count(*) from rpg.player", Long.class);
        Long result =  query.getSingleResult();
        session.getTransaction().commit();
        session.close();
        return result.intValue();
    }

    @Override
    public Player save(Player player) {
        return null;
    }

    @Override
    public Player update(Player player) {
        return null;
    }

    @Override
    public Optional<Player> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Player player) {

    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();

    }
}