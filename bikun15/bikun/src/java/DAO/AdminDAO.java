package DAO;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query; // Use this import for older Hibernate versions
import pojo.Admin;
import util.HibernateUtil;

public class AdminDAO {
    private Session session; 

    // Default constructor
    public AdminDAO() {
        this.session = HibernateUtil.getSessionFactory().openSession(); // Obtain the session
    }

    // Constructor for testing purposes
    public AdminDAO(Session session) {
        this.session = session;
    }

    // Method to validate username and password for login
    public boolean validate(String username, String password) {
        Admin admin = getByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }

    // Method to check if a username exists
    public boolean isUsernameExists(String username) {
        Transaction trans = null; // Transaction for the method
        boolean exists = false;

        try {
            trans = session.beginTransaction(); // Use the injected session
            Query query = session.createQuery("FROM Admin WHERE username = :username");
            query.setParameter("username", username);
            List<Admin> results = query.list();

            exists = !results.isEmpty(); // Check if the list is not empty
            trans.commit();
        } catch (Exception e) {
            if (trans != null) {
                trans.rollback(); // Rollback on error
            }
            System.out.println("Error: " + e);
        } finally {
            session.close(); // Ensure the session is closed
        }

        return exists;
    }

    // Method to retrieve an Admin by username
    public Admin getByUsername(String username) {
        Transaction trans = null; // Transaction for the method
        Admin admin = null;

        try {
            trans = session.beginTransaction(); // Use the injected session
            Query query = session.createQuery("FROM Admin WHERE username = :username");
            query.setParameter("username", username);
            List<Admin> results = query.list();

            if (!results.isEmpty()) {
                admin = (Admin) results.get(0); // Get the first result
            }
            trans.commit();
        } catch (Exception e) {
            if (trans != null) {
                trans.rollback(); // Rollback on error
            }
            System.out.println("Error: " + e);
        } finally {
            session.close(); // Ensure the session is closed
        }

        return admin; // Return the Admin object or null if not found
    }

    // Additional method to check if password is correct for a username
    public boolean isPasswordCorrect(String username, String password) {
        Admin admin = getByUsername(username);
        return admin != null && admin.getPassword().equals(password); // Compare stored password with provided password
    }
}
