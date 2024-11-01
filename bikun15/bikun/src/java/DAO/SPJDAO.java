package DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import pojo.Fleet;  
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;

public class SPJDAO {
    private SessionFactory sessionFactory;

    public SPJDAO() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public List<Fleet> getAllFleets() {
        Session session = sessionFactory.openSession();  // Open a new session
        try {
            String hql = "FROM Fleet";  // HQL query to select all Fleet records
            Query query = session.createQuery(hql);  // Create the query
            List<Fleet> fleetList = query.list();  // Execute the query

            // Initialize associations if lazy loading is still being attempted
            for (Fleet fleet : fleetList) {
                Hibernate.initialize(fleet.getBus());
                Hibernate.initialize(fleet.getDriver());
                Hibernate.initialize(fleet.getRute());
            }

            return fleetList;  // Return the results
        } finally {
            session.close();  // Ensure the session is closed
        }
    }



    public void addSPJ(Fleet fleet) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(fleet);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback(); // Rollback in case of error
            throw e; // Re-throw the exception for further handling
        } finally {
            session.close(); // Ensure session is closed
        }
    }

    public void updateSPJ(Fleet fleet) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(fleet);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback(); // Rollback in case of error
            throw e; // Re-throw the exception for further handling
        } finally {
            session.close(); // Ensure session is closed
        }
    }

    public void deleteSPJ(Fleet fleet) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(fleet);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback(); // Rollback in case of error
            throw e; // Re-throw the exception for further handling
        } finally {
            session.close(); // Ensure session is closed
        }
    }
    
    public void updateBusStatus(int id_bus, String status) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createQuery("UPDATE Bus SET status = :status WHERE id_bus = :id_bus")
                   .setParameter("status", status)
                   .setParameter("id_bus", id_bus)
                   .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback(); // Rollback in case of error
            throw e; // Re-throw the exception for further handling
        } finally {
            session.close(); // Ensure session is closed
        }
    }
    
    
}
