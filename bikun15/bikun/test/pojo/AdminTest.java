package pojo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AdminTest {

    // Declare an Admin object to be used in tests
    private Admin admin;

    @Before
    public void setUp() {
        // Initialize a new Admin object before each test
        admin = new Admin();
    }

    @Test
    public void testDefaultConstructor() {
        // Test the default constructor
        Admin defaultAdmin = new Admin();
        
        // Assert that all fields are null when using the default constructor
        assertNull(defaultAdmin.getIdAdmin());
        assertNull(defaultAdmin.getUsername());
        assertNull(defaultAdmin.getPassword());
    }

    @Test
    public void testParameterizedConstructor() {
        // Test the parameterized constructor
        Admin paramAdmin = new Admin("testUser", "testPass");
        
        // Assert that idAdmin is still null (as it's not set in the constructor)
        assertNull(paramAdmin.getIdAdmin());
        
        // Assert that username and password are set correctly
        assertEquals("testUser", paramAdmin.getUsername());
        assertEquals("testPass", paramAdmin.getPassword());
    }

    @Test
    public void testGetAndSetIdAdmin() {
        // Test getter and setter for idAdmin
        Integer expectedId = 123;
        admin.setIdAdmin(expectedId);
        assertEquals(expectedId, admin.getIdAdmin());
    }

    @Test
    public void testGetAndSetUsername() {
        // Test getter and setter for username
        String expectedUsername = "adminUser";
        admin.setUsername(expectedUsername);
        assertEquals(expectedUsername, admin.getUsername());
    }

    @Test
    public void testGetAndSetPassword() {
        // Test getter and setter for password
        String expectedPassword = "adminPass";
        admin.setPassword(expectedPassword);
        assertEquals(expectedPassword, admin.getPassword());
    }
}