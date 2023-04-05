package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {

        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users(
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(60) NOT NULL,
                lastName VARCHAR(60) NOT NULL,
                age TINYINT UNSIGNED NOT NULL
                );""";

        try (var session = Util.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.createNativeQuery(createUsersTable, User.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {

        String dropUsersTable = """
              DROP TABLE IF EXISTS users;
             """;

        try (var session = Util.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.createNativeQuery(dropUsersTable, User.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        Transaction transaction = null;
        var result = String
                .format("User с именем – '%s' добавлен в базу данных", name);

        try (var session = Util.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.persist(new User(name, lastName, age));
                transaction.commit();
                System.out.println(result);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {

        Transaction transaction = null;

        try (var session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.find(User.class, id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {

        try (var session = Util.getSessionFactory().openSession()){
            return session.createQuery("from User", User.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    // I'm confused..
    public void cleanUsersTable() {

        Transaction transaction = null;

        try (var session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery("truncate table users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }
}
