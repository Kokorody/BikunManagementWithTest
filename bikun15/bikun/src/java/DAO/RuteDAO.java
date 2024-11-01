package DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import pojo.Rute;
import java.util.List;

public class RuteDAO {

    private SessionFactory sessionFactory;

    public RuteDAO() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    // Method to retrieve all Rutes
    public List<Rute> getAllRutes() {
        Session session = sessionFactory.openSession();
        List<Rute> rutes = (List<Rute>) session.createQuery("from Rute").list();
        session.close();
        return rutes;
    }

    // Method to add a new rute
    public void addRute(Rute rute) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(rute);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; // Propagate exception
        } finally {
            session.close();
        }
    }

    // Method to delete a rute
    public void deleteRute(Rute rute) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.delete(rute);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; // Propagate exception
        } finally {
            session.close();
        }
    }

    // Method to update a rute
    public void updateRute(Rute rute) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(rute); // Use merge instead of update
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; // Propagate exception
        } finally {
            session.close();
        }
        
    }
    
    public Rute findById(String id) {
        Session session = sessionFactory.openSession();
        Rute rute = (Rute) session.get(Rute.class, id); // Cast to Rute
        session.close();
        return rute;
    }

}
