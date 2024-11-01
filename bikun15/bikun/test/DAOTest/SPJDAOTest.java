package DAOTest;

import DAO.SPJDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pojo.Fleet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SPJDAOTest {

    // Mock objects for Hibernate classes
    @Mock
    private SessionFactory mockSessionFactory;
    @Mock
    private Session mockSession;
    @Mock
    private Transaction mockTransaction;
    @Mock
    private Query mockQuery;

    // Inject mocks into the SPJDAO
    @InjectMocks
    private SPJDAO spjDAO;

    // Mock Fleet object for testing
    private Fleet mockFleet;

    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        
        // Set up mock behavior for SessionFactory and Session
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);

        // Initialize a mock Fleet object
        mockFleet = new Fleet();
        mockFleet.setIdFleet(1);
    }

    @Test
    public void testGetAllFleets() {
        // Create a mock list of Fleet objects
        List<Fleet> mockFleetList = new ArrayList<>();
        mockFleetList.add(mockFleet);

        // Mock the behavior of query creation and execution
        when(mockSession.createQuery("FROM Fleet")).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(mockFleetList);

        // Call the method to be tested
        List<Fleet> result = spjDAO.getAllFleets();

        // Verify method calls and assertions
        verify(mockSession, times(1)).createQuery("FROM Fleet");
        verify(mockQuery, times(1)).list();
        verify(mockSession, times(1)).close();
        assertEquals(mockFleetList, result);
    }

    @Test
    public void testAddSPJ() {
        // Call the method to be tested
        spjDAO.addSPJ(mockFleet);

        // Verify method calls
        verify(mockSession, times(1)).save(mockFleet);
        verify(mockTransaction, times(1)).commit();
        verify(mockSession, times(1)).close();
    }

    @Test
    public void testAddSPJ_Exception() {
        // Simulate an exception during save
        doThrow(new RuntimeException("Database error")).when(mockSession).save(mockFleet);

        // Test exception handling
        try {
            spjDAO.addSPJ(mockFleet);
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

        // Verify rollback and session closure
        verify(mockTransaction).rollback();
        verify(mockSession).close();
    }

    @Test
    public void testUpdateSPJ() {
        // Mock the update behavior
        doNothing().when(mockSession).update(mockFleet);

        // Call the method to be tested
        spjDAO.updateSPJ(mockFleet);

        // Verify method calls
        verify(mockSession, times(1)).update(mockFleet);
        verify(mockTransaction, times(1)).commit();
        verify(mockSession, times(1)).close();
    }

    @Test
    public void testUpdateSPJ_Exception() {
        // Simulate an exception during update
        doThrow(new RuntimeException("Database error")).when(mockSession).update(mockFleet);

        // Test exception handling
        try {
            spjDAO.updateSPJ(mockFleet);
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

        // Verify rollback and session closure
        verify(mockTransaction).rollback();
        verify(mockSession).close();
    }

    @Test
    public void testDeleteSPJ() {
        // Mock the delete behavior
        doNothing().when(mockSession).delete(mockFleet);

        // Call the method to be tested
        spjDAO.deleteSPJ(mockFleet);

        // Verify method calls
        verify(mockSession, times(1)).delete(mockFleet);
        verify(mockTransaction, times(1)).commit();
        verify(mockSession, times(1)).close();
    }

    @Test
    public void testDeleteSPJ_Exception() {
        // Simulate an exception during delete
        doThrow(new RuntimeException("Database error")).when(mockSession).delete(mockFleet);

        // Test exception handling
        try {
            spjDAO.deleteSPJ(mockFleet);
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

        // Verify rollback and session closure
        verify(mockTransaction).rollback();
        verify(mockSession).close();
    }

    @Test
    public void testUpdateBusStatus() {
        int idBus = 10;
        String status = "Active";

        // Mock the update query creation and execution
        when(mockSession.createQuery("UPDATE Bus SET status = :status WHERE id_bus = :id_bus"))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("status", status)).thenReturn(mockQuery);
        when(mockQuery.setParameter("id_bus", idBus)).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);

        // Call the method to be tested
        spjDAO.updateBusStatus(idBus, status);

        // Verify method calls
        verify(mockSession, times(1)).createQuery("UPDATE Bus SET status = :status WHERE id_bus = :id_bus");
        verify(mockQuery, times(1)).setParameter("status", status);
        verify(mockQuery, times(1)).setParameter("id_bus", idBus);
        verify(mockQuery, times(1)).executeUpdate();
        verify(mockTransaction, times(1)).commit();
        verify(mockSession, times(1)).close();
    }

    @Test
    public void testUpdateBusStatus_Exception() {
        int idBus = 10;
        String status = "Active";

        // Mock the update query creation
        when(mockSession.createQuery("UPDATE Bus SET status = :status WHERE id_bus = :id_bus"))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("status", status)).thenReturn(mockQuery);
        when(mockQuery.setParameter("id_bus", idBus)).thenReturn(mockQuery);

        // Simulate an exception during query execution
        doThrow(new RuntimeException("Database error")).when(mockQuery).executeUpdate();

        // Test exception handling
        try {
            spjDAO.updateBusStatus(idBus, status);
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

        // Verify rollback and session closure
        verify(mockTransaction).rollback();
        verify(mockSession).close();
    }
}