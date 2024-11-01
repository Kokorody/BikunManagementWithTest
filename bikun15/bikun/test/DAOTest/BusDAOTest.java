package DAOTest;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pojo.Bus;
import DAO.BusDAO;

import java.util.Collections;
import java.util.List;

public class BusDAOTest {

    // Inject mocks into the BusDAO
    @InjectMocks
    private BusDAO busDAO;

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
    public void testGetAllBuses() {
        // Create a sample Bus object
        Bus bus = new Bus("NB123", "PLATE123", "TypeA", "Owner1", "depot");

        // Mock the query result
        when(query.list()).thenReturn(Collections.singletonList(bus));

        // Call the method to be tested
        List<Bus> buses = busDAO.getAllBuses();

        // Assert the results
        assertNotNull(buses);
        assertEquals(1, buses.size());
        assertEquals("NB123", buses.get(0).getNoBody());
        assertEquals("PLATE123", buses.get(0).getPlatNomor());
        assertEquals("TypeA", buses.get(0).getTipeBus());
        assertEquals("Owner1", buses.get(0).getPemilik());
        assertEquals("depot", buses.get(0).getStatus());
        
        System.out.println("testGetAllBuses:");
        System.out.println("No Body: " + buses.get(0).getNoBody());
        System.out.println("Plat Nomor: " + buses.get(0).getPlatNomor());
        System.out.println("Tipe Bus: " + buses.get(0).getTipeBus());
        System.out.println("Pemilik: " + buses.get(0).getPemilik());
        System.out.println("Status: " + buses.get(0).getStatus());
        System.out.println("=============================================");         
    }

    @Test
    public void testAddBus() {
        // Create a sample Bus object
        Bus bus = new Bus();
        bus.setPlatNomor("PLATE124");
        bus.setTipeBus("TypeB");
        bus.setPemilik("Owner2");
        bus.setStatus("active");

//        System.out.println("Initial Bus State:");
//        System.out.println("Plate: " + bus.getPlatNomor());
//        System.out.println("Type: " + bus.getTipeBus());
//        System.out.println("Owner: " + bus.getPemilik());
//        System.out.println("Status: " + bus.getStatus());

        // Mock the behavior of session.save
        when(session.save(bus)).thenReturn(bus.getIdBus());

        // Call the method to be tested
        busDAO.addBus(bus);

        // Verify that save and commit were called
        verify(session).save(bus);
        verify(transaction).commit();

        // Assert that the bus object is not null
        assertNotNull("Bus object should not be null", bus);

        // Assertions for each field
        assertEquals("Bus plate should match", "PLATE124", bus.getPlatNomor());
        assertEquals("Bus type should match", "TypeB", bus.getTipeBus());
        assertEquals("Bus owner should match", "Owner2", bus.getPemilik());
        assertEquals("Bus status should match", "depot", bus.getStatus());
        
        // Print final bus state
        System.out.println("BusDAO AddBus");
        System.out.println("Plate: " + bus.getPlatNomor());
        System.out.println("Type: " + bus.getTipeBus());
        System.out.println("Owner: " + bus.getPemilik());
        System.out.println("Status: " + bus.getStatus());
    }

    @Test
    public void testDeleteBus() {
        // Create a sample Bus object
        Bus bus = new Bus("NB125", "PLATE125", "TypeC", "Owner3", "inactive");

        // Mock the behavior of session.delete
        doNothing().when(session).delete(bus);

        // Call the method to be tested
        busDAO.deleteBus(bus);

        // Verify that delete and commit were called
        verify(session).delete(bus);
        verify(transaction).commit();
    }

    @Test
    public void testUpdateBus() {
        // Create a sample Bus object
        Bus bus = new Bus("NB126", "PLATE126", "TypeD", "Owner4", "depot");
        bus.setIdBus(1); // Set an ID for the bus

        // Mock the behavior of session.update
        doNothing().when(session).update(bus);

        // Call the method to be tested
        busDAO.updateBus(bus);

        // Verify that update and commit were called
        verify(session).update(bus);
        verify(transaction).commit();
    }

    @Test
    public void testGetAvailableBuses() {
        // Create a sample Bus object
        Bus bus = new Bus("NB127", "PLATE127", "TypeE", "Owner5", "depot");

        // Mock the query result
        when(query.list()).thenReturn(Collections.singletonList(bus));

        // Call the method to be tested
        List<Bus> availableBuses = busDAO.getAvailableBuses();

        // Assert the results
        assertNotNull(availableBuses);
        assertEquals(1, availableBuses.size());
        assertEquals("NB127", availableBuses.get(0).getNoBody());
        assertEquals("PLATE127", availableBuses.get(0).getPlatNomor());
        assertEquals("depot", availableBuses.get(0).getStatus());
    }

    @Test
    public void testFindById() {
        // Create a sample Bus object
        Bus bus = new Bus("NB128", "PLATE128", "TypeF", "Owner6", "depot");
        bus.setIdBus(1);

        // Mock the behavior of session.get
        when(session.get(Bus.class, 1)).thenReturn(bus);

        // Call the method to be tested
        Bus result = busDAO.findById(1);

        // Assert the results
        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getIdBus());
        assertEquals("NB128", result.getNoBody());
        assertEquals("PLATE128", result.getPlatNomor());
    }
}