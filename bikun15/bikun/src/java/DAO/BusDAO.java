package DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;
import pojo.Bus;

public class BusDAO {

    private SessionFactory sessionFactory;

    public BusDAO() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    // Method to retrieve all buses
    public List<Bus> getAllBuses() {
        Session session = sessionFactory.openSession();
        List<Bus> buses = session.createQuery("from Bus").list(); // Corrected for Hibernate 4.3.1
        session.close();
        return buses;
    }

    // Method to add a new bus
    public void addBus(Bus bus) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(bus);
        tx.commit();
        session.close();
    }

    // Method to delete a bus
    public void deleteBus(Bus bus) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(bus);
        tx.commit();
        session.close();
    }

    // Method to update a bus
    public void updateBus(Bus bus) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(bus);
        tx.commit();
        session.close();
    }
    
    // Method to retrieve available buses (in depot status and not used in SPJ)
    public List<Bus> getAvailableBuses() {
        Session session = sessionFactory.openSession();
        List<Bus> availableBuses = session.createQuery(
            "FROM Bus b WHERE b.status = 'depot' AND b.idBus NOT IN (SELECT f.bus FROM Fleet f)"
        ).list(); // Adjusted to join Fleet
        session.close();
        return availableBuses;
    }
    
    public Bus findById(int id) {
        Session session = sessionFactory.openSession();
        Bus bus = (Bus) session.get(Bus.class, id); // Cast the Object to Bus
        session.close();
        return bus;
    }



}