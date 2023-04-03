package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public final class Util {

    private static SessionFactory sessionFactory;

    static {

        initSessionFactory();
    }

    private Util () {

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static void initSessionFactory() {

        var serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        serviceRegistryBuilder
                .applySetting("hibernate-dialect", "org.hibernate.dialect.MySQLDialect")
                .applySetting("connection.driver_class", "com.mysql.cj.jdbc.Driver")
                .applySetting("hibernate.hbm2ddl.auto", "update")
                .applySetting("hibernate.connection.url", "jdbc:mysql://localhost:3306/live")
                .applySetting("hibernate.connection.username","root")
                .applySetting("hibernate.connection.password", "root")
                .applySetting("show_sql", "false")
                .applySetting("hibernate.format_sql", "true");

        var serviceRegistry = serviceRegistryBuilder.build();
        var metadataSources = new MetadataSources(serviceRegistry).addAnnotatedClass(User.class);
        var metadataBuilder = metadataSources.getMetadataBuilder();
        var metadata = metadataBuilder.build();

        sessionFactory = metadata.buildSessionFactory();
    }
}
