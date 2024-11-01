package pojo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;

// Test class for the Bus POJO
public class BusTest {

    // Declaring a Bus object to be used in tests
    private Bus bus;

    // Setup method to initialize the bus object before each test
    @Before
    public void setUp() {
        bus = new Bus();
    }

    // Test the default constructor
    @Test
    public void testDefaultConstructor() {
        Bus defaultBus = new Bus();
        // Assert that all fields are null or empty
        assertNull(defaultBus.getIdBus());
        assertNull(defaultBus.getNoBody());
        assertNull(defaultBus.getPlatNomor());
        assertNull(defaultBus.getTipeBus());
        assertNull(defaultBus.getPemilik());
        assertNull(defaultBus.getStatus());
        assertTrue(defaultBus.getFleets().isEmpty());
    }

    // Test the parameterized constructor without fleets
    @Test
    public void testParameterizedConstructorWithoutFleets() {
        Bus paramBus = new Bus("NB001", "D1234XYZ", "Luxury", "John Doe", "Available");
        // Assert that fields are set correctly
        assertNull(paramBus.getIdBus());
        assertEquals("NB001", paramBus.getNoBody());
        assertEquals("D1234XYZ", paramBus.getPlatNomor());
        assertEquals("Luxury", paramBus.getTipeBus());
        assertEquals("John Doe", paramBus.getPemilik());
        assertEquals("Available", paramBus.getStatus());
        assertTrue(paramBus.getFleets().isEmpty());
    }

    // Test the parameterized constructor with fleets
    @Test
    public void testParameterizedConstructorWithFleets() {
        Set<String> testFleets = new HashSet<>();
        testFleets.add("Fleet1");
        testFleets.add("Fleet2");

        Bus paramBus = new Bus("NB002", "B5678XYZ", "Standard", "Jane Doe", "Unavailable", testFleets);
        // Assert that all fields, including fleets, are set correctly
        assertNull(paramBus.getIdBus());
        assertEquals("NB002", paramBus.getNoBody());
        assertEquals("B5678XYZ", paramBus.getPlatNomor());
        assertEquals("Standard", paramBus.getTipeBus());
        assertEquals("Jane Doe", paramBus.getPemilik());
        assertEquals("Unavailable", paramBus.getStatus());
        assertEquals(2, paramBus.getFleets().size());
        assertTrue(paramBus.getFleets().contains("Fleet1"));
        assertTrue(paramBus.getFleets().contains("Fleet2"));
    }

    // Test getter and setter for idBus
    @Test
    public void testGetAndSetIdBus() {
        Integer expectedId = 321;
        bus.setIdBus(expectedId);
        assertEquals(expectedId, bus.getIdBus());
    }

    // Test getter and setter for noBody
    @Test
    public void testGetAndSetNoBody() {
        String expectedNoBody = "NB123";
        bus.setNoBody(expectedNoBody);
        assertEquals(expectedNoBody, bus.getNoBody());
    }

    // Test getter and setter for platNomor
    @Test
    public void testGetAndSetPlatNomor() {
        String expectedPlatNomor = "C9876ABC";
        bus.setPlatNomor(expectedPlatNomor);
        assertEquals(expectedPlatNomor, bus.getPlatNomor());
    }

    // Test getter and setter for tipeBus
    @Test
    public void testGetAndSetTipeBus() {
        String expectedTipeBus = "Economy";
        bus.setTipeBus(expectedTipeBus);
        assertEquals(expectedTipeBus, bus.getTipeBus());
    }

    // Test getter and setter for pemilik
    @Test
    public void testGetAndSetPemilik() {
        String expectedPemilik = "Jake Smith";
        bus.setPemilik(expectedPemilik);
        assertEquals(expectedPemilik, bus.getPemilik());
    }

    // Test getter and setter for status
    @Test
    public void testGetAndSetStatus() {
        String expectedStatus = "In Maintenance";
        bus.setStatus(expectedStatus);
        assertEquals(expectedStatus, bus.getStatus());
    }

    // Test getter and setter for fleets
    @Test
    public void testGetAndSetFleets() {
        Set<String> testFleets = new HashSet<>();
        testFleets.add("Fleet A");
        bus.setFleets(testFleets);
        assertEquals(testFleets, bus.getFleets());
    }

    // Test the toString method
    @Test
    public void testToString() {
        bus.setIdBus(555);
        bus.setPlatNomor("Z4321PQR");
        bus.setTipeBus("Double-Decker");

        String expectedString = "Bus ID: 555, Plate Number: Z4321PQR, Type: Double-Decker";
        assertEquals(expectedString, bus.toString());
    }
}