package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Session session = null;
        Transaction transaction;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createNativeQuery("""
                CREATE TABLE IF NOT EXISTS users(
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(60) NOT NULL,
                lastName VARCHAR(60) NOT NULL,
                age TINYINT UNSIGNED NOT NULL
                );""", User.class).executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            // if (transaction != null) { transaction.rollback(); } rollback for DDL?
            throw new RuntimeException(e);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void dropUsersTable() {

        Session session = null;
        Transaction transaction;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.createNativeQuery("""
              DROP TABLE IF EXISTS users;
             """, User.class).executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            // if (transaction != null) { transaction.rollback(); } rollback for DDL?
            throw new RuntimeException(e);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        var result = String.format("User с именем – '%s' добавлен в базу данных", name);
        Session session = null;
        Transaction transaction = null;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(new User(name, lastName, age));
            transaction.commit();
            System.out.println(result);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void removeUserById(long id) {

        Session session = null;
        Transaction transaction = null;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.remove(session.find(User.class, id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<User> getAllUsers() {

        Session session = null;
        Transaction transaction = null;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            var users = session.createQuery("from User", User.class).getResultList();
            transaction.commit();
            return users;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void cleanUsersTable() {

        Session session = null;
        Transaction transaction = null;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            var users = session.createQuery("from User", User.class).getResultList();
            for (var user: users) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
