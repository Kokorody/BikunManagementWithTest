package DAOTest;

// Import necessary libraries and classes
import DAO.DriverDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pojo.Driver;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DriverDAOTest {

    // Inject mocks into the DriverDAO
    @InjectMocks
    private DriverDAO driverDAO;

    // Mock the SessionFactory
    @Mock
    private SessionFactory sessionFactory;

    // Mock the Session
    @Mock
    private Session session;

    // Mock the Transaction
    @Mock
    private Transaction transaction;

    // Mock the Query
    @Mock
    private Query query;

    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        
        // Set up mock behavior for session factory, session, and query
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createQuery(anyString())).thenReturn(query);
    }

    @Test
    public void testGetAllDrivers() {
        // Create a sample Driver object
        Driver driver = new Driver();

        // Mock the query result
        when(query.list()).thenReturn(Collections.singletonList(driver));

        // Call the method to be tested
        List<Driver> drivers = driverDAO.getAllDrivers();

        // Assert the results
        assertNotNull(drivers);
        assertEquals(1, drivers.size());
    }

    @Test
    public void testAddDriver() {
        // Create a sample Driver object
        Driver driver = new Driver();

        // Mock the behavior of session.save
        when(session.save(driver)).thenReturn(driver);

        // Call the method to be tested
        driverDAO.addDriver(driver);

        // Verify that save and commit were called
        verify(session).save(driver);
        verify(transaction).commit();
    }

    @Test
    public void testDeleteDriver() {
        // Create a sample Driver object
        Driver driver = new Driver();

        // Mock the behavior of session.delete
        doNothing().when(session).delete(driver);

        // Call the method to be tested
        driverDAO.deleteDriver(driver);

        // Verify that delete and commit were called
        verify(session).delete(driver);
        verify(transaction).commit();
    }

    @Test
    public void testUpdateDriver() {
        // Create a sample Driver object
        Driver driver = new Driver();
        driver.setIdDriver(1); // Set an ID for the driver

        // Mock the behavior of session.update
        doNothing().when(session).update(driver);

        // Call the method to be tested
        driverDAO.updateDriver(driver);

        // Verify that update and commit were called
        verify(session).update(driver);
        verify(transaction).commit();
    }

    @Test
    public void testFindByName() {
        // Create a sample Driver object with a name
        Driver driver = new Driver();
        driver.setIdDriver(1);
        driver.setNamaDriver("Rizaq");

        // Mock the query behavior for finding a driver by name
        when(session.createQuery("FROM Driver WHERE namaDriver = :name")).thenReturn(query);
        when(query.setParameter("name", "Rizaq")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(driver);

        // Call the method to be tested
        Driver result = driverDAO.findByName("Rizaq");

        // Assert the results
        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getIdDriver());
        assertEquals("Rizaq", result.getNamaDriver());
    }

    @Test
    public void testGetAvailableDrivers() {
        // Create a sample Driver object
        Driver driver = new Driver();

        // Mock the query result
        when(query.list()).thenReturn(Collections.singletonList(driver));

        // Call the method to be tested
        List<Driver> availableDrivers = driverDAO.getAvailableDrivers();

        // Assert the results
        assertNotNull(availableDrivers);
        assertEquals(1, availableDrivers.size());
    }
}