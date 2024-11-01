package pojo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

// Test class for the Driver POJO
public class DriverTest {

    // Declaring a Driver object to be used in tests
    private Driver driver;

    // Setup method to initialize the driver object before each test
    @Before
    public void setUp() {
        driver = new Driver();
    }

    // Test the default constructor
    @Test
    public void testDefaultConstructor() {
        Driver defaultDriver = new Driver();
        // Assert that all fields are null or empty
        assertNull(defaultDriver.getIdDriver());
        assertNull(defaultDriver.getNamaDriver());
        assertNull(defaultDriver.getNoSim());
        assertNull(defaultDriver.getOperator());
        assertTrue(defaultDriver.getFleets().isEmpty());
    }

    // Test the parameterized constructor without fleets
    @Test
    public void testParameterizedConstructorWithoutFleets() {
        Driver paramDriver = new Driver("John Doe", "123456789", "City Bus");
        // Assert that fields are set correctly
        assertNull(paramDriver.getIdDriver());
        assertEquals("John Doe", paramDriver.getNamaDriver());
        assertEquals("123456789", paramDriver.getNoSim());
        assertEquals("City Bus", paramDriver.getOperator());
        assertTrue(paramDriver.getFleets().isEmpty());
    }

    // Test the parameterized constructor with fleets
    @Test
    public void testParameterizedConstructorWithFleets() {
        Set<String> testFleets = new HashSet<>();
        testFleets.add("Fleet1");
        testFleets.add("Fleet2");

        Driver paramDriver = new Driver("Jane Smith", "987654321", "Intercity", testFleets);
        // Assert that all fields, including fleets, are set correctly
        assertNull(paramDriver.getIdDriver());
        assertEquals("Jane Smith", paramDriver.getNamaDriver());
        assertEquals("987654321", paramDriver.getNoSim());
        assertEquals("Intercity", paramDriver.getOperator());
        assertEquals(2, paramDriver.getFleets().size());
        assertTrue(paramDriver.getFleets().contains("Fleet1"));
        assertTrue(paramDriver.getFleets().contains("Fleet2"));
    }

    // Test getter and setter for idDriver
    @Test
    public void testGetAndSetIdDriver() {
        Integer expectedId = 101;
        driver.setIdDriver(expectedId);
        assertEquals(expectedId, driver.getIdDriver());
    }

    // Test getter and setter for namaDriver
    @Test
    public void testGetAndSetNamaDriver() {
        String expectedName = "Michael Johnson";
        driver.setNamaDriver(expectedName);
        assertEquals(expectedName, driver.getNamaDriver());
    }

    // Test getter and setter for noSim
    @Test
    public void testGetAndSetNoSim() {
        String expectedSim = "AB123456";
        driver.setNoSim(expectedSim);
        assertEquals(expectedSim, driver.getNoSim());
    }

    // Test getter and setter for operator
    @Test
    public void testGetAndSetOperator() {
        String expectedOperator = "Metro Bus";
        driver.setOperator(expectedOperator);
        assertEquals(expectedOperator, driver.getOperator());
    }

    // Test getter and setter for fleets
    @Test
    public void testGetAndSetFleets() {
        Set<String> testFleets = new HashSet<>();
        testFleets.add("Fleet A");
        driver.setFleets(testFleets);
        assertEquals(testFleets, driver.getFleets());
    }

    // Test the toString method
    @Test
    public void testToString() {
        driver.setNamaDriver("Alice Brown");
        driver.setNoSim("CD987654");

        String expectedString = "Driver Name: Alice Brown, SIM No: CD987654";
        assertEquals(expectedString, driver.toString());
    }

    // Test equals and hashCode methods
    @Test
    public void testEqualsAndHashCode() {
        Driver driver1 = new Driver();
        Driver driver2 = new Driver();
        driver1.setIdDriver(1);
        driver2.setIdDriver(1);

        // Test equals method
        assertTrue(driver1.equals(driver2));
        assertEquals(driver1.hashCode(), driver2.hashCode());

        driver2.setIdDriver(2);
        assertFalse(driver1.equals(driver2));
        assertNotEquals(driver1.hashCode(), driver2.hashCode());
    }

    // Test equals method with null input
    @Test
    public void testEqualsWithNull() {
        assertFalse(driver.equals(null));  // Ensure `equals()` handles null correctly
    }

    // Test equals method with different class input
    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(driver.equals(new Object()));  // Ensure `equals()` handles different class correctly
    }
}