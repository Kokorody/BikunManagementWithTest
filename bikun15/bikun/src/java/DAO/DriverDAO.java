package DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;
import pojo.Driver;

public class DriverDAO {

    private SessionFactory sessionFactory;

    public DriverDAO() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    // Method to retrieve all drivers
    public List<Driver> getAllDrivers() {
        Session session = sessionFactory.openSession();
        List<Driver> drivers = session.createQuery("from Driver").list();
        session.close();
        return drivers;
    }

    // Method to add a new driver
    public void addDriver(Driver driver) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(driver);
        tx.commit();
        session.close();
    }

    // Method to delete a driver
    public void deleteDriver(Driver driver) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(driver);
        tx.commit();
        session.close();
    }

    // Method to update a driver
    public void updateDriver(Driver driver) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(driver);
        tx.commit();
        session.close();
    }
    
    public Driver findByName(String name) {
        Session session = sessionFactory.openSession();
        // Assume you are using HQL or Criteria to fetch the Driver based on name
        Driver driver = (Driver) session.createQuery("FROM Driver WHERE namaDriver = :name")
                            .setParameter("name", name)
                            .uniqueResult(); // Cast to Driver
        session.close();
        return driver;
    }
   
    
    public List<Driver> getAvailableDrivers() {
        Session session = sessionFactory.openSession();
        List<Driver> availableBuses = session.createQuery(
            "FROM Driver d WHERE d.idDriver NOT IN (SELECT f.driver FROM Fleet f)"
        ).list(); // Adjusted to join Fleet
        session.close();
        return availableBuses;
    }
    

    

}
