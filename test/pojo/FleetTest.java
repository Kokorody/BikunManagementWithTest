package pojo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FleetTest {

    // Declare a Fleet object to be used in tests
    private Fleet fleet;

    // Setup method to initialize the fleet object before each test
    @Before
    public void setUp() {
        fleet = new Fleet();
    }

    // Test the default constructor
    @Test
    public void testDefaultConstructor() {
        // Assert that all fields are null in the default constructor
        assertEquals(null, fleet.getIdFleet());
        assertEquals(null, fleet.getBus());
        assertEquals(null, fleet.getDriver());
        assertEquals(null, fleet.getRute());
    }

    // Test the parameterized constructor with Bus, Driver, and Rute
    @Test
    public void testConstructorWithBusDriverRute() {
        Bus bus = new Bus(); // Create a default Bus object for testing
        Driver driver = new Driver(); // Create a default Driver object for testing
        Rute rute = new Rute("R001", "Destination A"); // Create an example Rute

        fleet = new Fleet(bus, driver, rute);
        // Assert that fields are set correctly in the parameterized constructor
        assertEquals(bus, fleet.getBus());
        assertEquals(driver, fleet.getDriver());
        assertEquals(rute, fleet.getRute());
    }

    // Test setter and getter for idFleet
    @Test
    public void testSetIdFleet() {
        fleet.setIdFleet(1);
        assertEquals(Integer.valueOf(1), fleet.getIdFleet());
    }

    // Test setter and getter for Bus
    @Test
    public void testSetBus() {
        Bus bus = new Bus(); // Create a new Bus object
        fleet.setBus(bus);
        assertEquals(bus, fleet.getBus());
    }

    // Test setter and getter for Driver
    @Test
    public void testSetDriver() {
        Driver driver = new Driver(); // Create a new Driver object
        fleet.setDriver(driver);
        assertEquals(driver, fleet.getDriver());
    }

    // Test setter and getter for Rute
    @Test
    public void testSetRute() {
        Rute rute = new Rute("R002", "Destination B"); // Create an example Rute
        fleet.setRute(rute);
        assertEquals(rute, fleet.getRute());
    }
}