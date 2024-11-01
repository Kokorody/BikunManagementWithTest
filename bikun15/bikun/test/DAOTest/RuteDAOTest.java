package DAOTest;

import DAO.RuteDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pojo.Rute;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class RuteDAOTest {

    // Inject mocks into the RuteDAO
    @InjectMocks
    private RuteDAO ruteDAO;

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
    public void testGetAllRutes() {
        // Create a sample Rute object
        Rute rute = new Rute();

        // Mock the query result
        when(query.list()).thenReturn(Collections.singletonList(rute));

        // Call the method to be tested
        List<Rute> rutes = ruteDAO.getAllRutes();

        // Assert the results
        assertNotNull(rutes);
        assertEquals(1, rutes.size());
        verify(session).close(); // Verify session closure
    }

    @Test
    public void testAddRute() {
        // Create a sample Rute object
        Rute rute = new Rute();

        // Mock the behavior of session.save
        when(session.save(rute)).thenReturn(rute);

        // Call the method to be tested
        ruteDAO.addRute(rute);

        // Verify method calls
        verify(session).save(rute);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    public void testAddRute_Exception() {
        // Create a sample Rute object
        Rute rute = new Rute();

        // Simulate an exception during save
        doThrow(new RuntimeException("Database error")).when(session).save(rute);

        // Test exception handling
        try {
            ruteDAO.addRute(rute);
            fail("Expected exception was not thrown");
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

        // Verify rollback and session closure
        verify(transaction).rollback();
        verify(session).close();
    }

    @Test
    public void testDeleteRute() {
        // Create a sample Rute object
        Rute rute = new Rute();

        // Call the method to be tested
        ruteDAO.deleteRute(rute);

        // Verify method calls
        verify(session).delete(rute);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    public void testDeleteRute_Exception() {
        // Create a sample Rute object
        Rute rute = new Rute();

        // Simulate an exception during delete
        doThrow(new RuntimeException("Database error")).when(session).delete(rute);

        // Test exception handling
        try {
            ruteDAO.deleteRute(rute);
            fail("Expected exception was not thrown");
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

        // Verify rollback and session closure
        verify(transaction).rollback();
        verify(session).close();
    }

    @Test
    public void testUpdateRute() {
        // Create a sample Rute object
        Rute rute = new Rute();
        rute.setIdRute("R001");

        // Call the method to be tested
        ruteDAO.updateRute(rute);

        // Verify method calls
        verify(session).merge(rute);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    public void testUpdateRute_Exception() {
        // Create a sample Rute object
        Rute rute = new Rute();
        rute.setIdRute("R001");

        // Simulate an exception during update
        doThrow(new RuntimeException("Database error")).when(session).merge(rute);

        // Test exception handling
        try {
            ruteDAO.updateRute(rute);
            fail("Expected exception was not thrown");
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

        // Verify rollback and session closure
        verify(transaction).rollback();
        verify(session).close();
    }

    @Test
    public void testFindById() {
        // Create a sample Rute object
        Rute rute = new Rute();
        rute.setIdRute("R001");

        // Mock session.get to return the rute
        when(session.get(Rute.class, "R001")).thenReturn(rute);

        // Call the method to be tested
        Rute result = ruteDAO.findById("R001");

        // Assert the results
        assertNotNull(result);
        assertEquals("R001", result.getIdRute());
        verify(session).close();
    }

    @Test
    public void testFindById_NotFound() {
        // Mock session.get to return null
        when(session.get(Rute.class, "R002")).thenReturn(null);

        // Call the method to be tested
        Rute result = ruteDAO.findById("R002");

        // Assert the result
        assertNull(result);
        verify(session).close();
    }
}