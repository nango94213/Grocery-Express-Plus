package edu.gatech.cs6310.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class PostgresClientImpl implements DatabaseClient {

    private final SessionFactory sessionFactory;

    public PostgresClientImpl() {
        this.sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
    }

    public void save(Object object) {
        Transaction tx = null;

        Session session = sessionFactory.openSession();

        try {
            tx = session.beginTransaction();
            session.save(object);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) {
                tx.rollback();
            }

            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List selectWhere(String table, String property, String value) {

        Session session = sessionFactory.openSession();
        List objects;

        String query = "FROM %s x WHERE x.%s='%s'".formatted(table, property, value);
        objects = session.createQuery(query).list();

        session.close();

        return objects;
    }

    public List selectWhere(String table, String property, String value, String property2, String value2) {

        Session session = sessionFactory.openSession();
        List objects;

        String query = "FROM %s x WHERE x.%s='%s' AND x.%s='%s'".formatted(table, property, value, property2, value2);
        objects = session.createQuery(query).list();

        session.close();

        return objects;
    }

    public List selectAll(String table) {
        Session session = sessionFactory.openSession();
        List objects;

        String query = "FROM %s".formatted(table);
        objects = session.createQuery(query).list();

        session.close();

        return objects;
    }

    public List selectWhereNull(String table, String property) {
        Session session = sessionFactory.openSession();
        List objects;

        String query = "FROM %s x WHERE x.%s is null".formatted(table, property);
        objects = session.createQuery(query).list();

        session.close();

        return objects;
    }

    public List selectLike(String table, String property, String value, String property2, String value2, String property3, String value3) {
        Session session = sessionFactory.openSession();
        List objects;

        String query = "FROM %s x WHERE x.%s LIKE '%s' AND x.%s LIKE '%s' AND x.%s LIKE '%s'".formatted(table, property, value, property2, value2, property3, value3);
        objects = session.createQuery(query).list();

        session.close();

        return objects;
    }

    public Boolean deleteWhere(String table, String property, String value){
        Transaction tx = null;

        Session session = sessionFactory.openSession();

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("delete from %s where %s='%s'".formatted(table, property, value));
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return true;
    }

    public Boolean deleteWhere(String table, String property, String value, String property2, String value2){
        Transaction tx = null;

        Session session = sessionFactory.openSession();

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("delete from %s where %s='%s' AND %s='%s'".formatted(
                    table, property, value, property2, value2));
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return true;
    }

    public Boolean deleteWhere(String table, String property, String value, String property2, String value2, String property3, String value3){
        Transaction tx = null;

        Session session = sessionFactory.openSession();

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("delete from %s where %s='%s' AND %s='%s' AND %s='%s'".formatted(
                    table, property, value, property2, value2, property3, value3));
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return true;
    }

    public Boolean updateWhere(String table, String setProperty, String setValue, String whereProperty, String whereValue){
        Transaction tx = null;

        Session session = sessionFactory.openSession();

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("update %s set %s='%s' where %s='%s'".formatted(
                    table, setProperty, setValue, whereProperty, whereValue));
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return true;
    }
}
