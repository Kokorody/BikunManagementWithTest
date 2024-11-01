package pojo;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;

public class RuteTest {

    // Declare a Rute object to be used in tests
    private Rute rute;

    // Setup method to initialize the rute object before each test
    @Before
    public void setUp() {
        rute = new Rute();
    }

    // Test the default constructor
    @Test
    public void testDefaultConstructor() {
        // Assert that idRute and jurusan are null, and fleets is an empty HashSet
        assertEquals(null, rute.getIdRute());
        assertEquals(null, rute.getJurusan());
        assertEquals(new HashSet(), rute.getFleets());
    }

    // Test the constructor with idRute and jurusan parameters
    @Test
    public void testConstructorWithIdAndJurusan() {
        rute = new Rute("R001", "Destination A");
        // Assert that idRute and jurusan are set correctly, and fleets is empty
        assertEquals("R001", rute.getIdRute());
        assertEquals("Destination A", rute.getJurusan());
        assertEquals(new HashSet(), rute.getFleets());
    }

    // Test the constructor with all fields (idRute, jurusan, and fleets)
    @Test
    public void testConstructorWithAllFields() {
        HashSet<String> fleetsSet = new HashSet<>();
        fleetsSet.add("Fleet1");
        rute = new Rute("R002", "Destination B", fleetsSet);
        // Assert that all fields are set correctly
        assertEquals("R002", rute.getIdRute());
        assertEquals("Destination B", rute.getJurusan());
        assertEquals(fleetsSet, rute.getFleets());
    }

    // Test setter and getter for idRute
    @Test
    public void testSetIdRute() {
        rute.setIdRute("R003");
        assertEquals("R003", rute.getIdRute());
    }

    // Test setter and getter for jurusan
    @Test
    public void testSetJurusan() {
        rute.setJurusan("Destination C");
        assertEquals("Destination C", rute.getJurusan());
    }

    // Test setter and getter for fleets
    @Test
    public void testSetFleets() {
        HashSet<String> fleetsSet = new HashSet<>();
        fleetsSet.add("Fleet2");
        rute.setFleets(fleetsSet);
        assertEquals(fleetsSet, rute.getFleets());
    }

    // Test the toString method
    @Test
    public void testToString() {
        rute = new Rute("R004", "Destination D");
        String expectedString = "Route ID: R004, Destination: Destination D";
        assertEquals(expectedString, rute.toString());
    }
}