package DAOTest;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pojo.Admin;
import DAO.AdminDAO;
import java.util.Collections;

public class AdminDAOTest {
    @InjectMocks
    private AdminDAO adminDAO;    // The class being tested, will have mocks injected

    @Mock
    private Session session;      // Mock for Hibernate session

    @Mock
    private Transaction transaction; // Mock for database transaction

    @Mock
    private Query query;          // Mock for Hibernate queries

    // Setup method runs before each test
    @Before
    public void setUp() {
        // Initialize all mock annotations
        MockitoAnnotations.initMocks(this);
        
        // Configure default behavior for session mock
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createQuery(anyString())).thenReturn(query);
    }

    // Test case: Successful admin validation
    @Test
    public void testValidate_Success() {
        // Create test admin object
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("password");

        // Configure mock to return the test admin
        when(query.list()).thenReturn(Collections.singletonList(admin));
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Execute validation method
        boolean result = adminDAO.validate("admin", "password");

        // Verify the result and session closure
        assertTrue(result);
        verify(session).close();
    }

    // Test case: Failed admin validation
    @Test
    public void testValidate_Failure() {
        // Configure mock to return empty result
        when(query.list()).thenReturn(Collections.emptyList());
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test validation with wrong credentials
        boolean result = adminDAO.validate("admin", "wrongpassword");

        // Verify failed validation and session closure
        assertFalse(result);
        verify(session).close();
    }

    // Test case: Check if username exists (positive case)
    @Test
    public void testIsUsernameExists_UsernameExists() {
        // Configure mock to return an admin instance
        when(query.list()).thenReturn(Collections.singletonList(new Admin()));
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test username existence check
        boolean exists = adminDAO.isUsernameExists("admin");

        // Verify positive result and session closure
        assertTrue(exists);
        verify(session).close();
    }

    // Test case: Check if username exists (negative case)
    @Test
    public void testIsUsernameExists_UsernameNotExists() {
        // Configure mock to return empty result
        when(query.list()).thenReturn(Collections.emptyList());
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test non-existent username
        boolean exists = adminDAO.isUsernameExists("nonexistent");

        // Verify negative result and session closure
        assertFalse(exists);
        verify(session).close();
    }

    // Test case: Get admin by username (successful case)
    @Test
    public void testGetByUsername_UsernameExists() {
        // Create expected admin object
        Admin expectedAdmin = new Admin();
        expectedAdmin.setUsername("admin");
        expectedAdmin.setPassword("password");

        // Configure mock to return the expected admin
        when(query.list()).thenReturn(Collections.singletonList(expectedAdmin));
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test retrieving admin by username
        Admin result = adminDAO.getByUsername("admin");

        // Verify retrieved admin and session closure
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        verify(session).close();
    }

    // Test case: Get admin by username (unsuccessful case)
    @Test
    public void testGetByUsername_UsernameNotExists() {
        // Configure mock to return empty result
        when(query.list()).thenReturn(Collections.emptyList());
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test retrieving non-existent admin
        Admin result = adminDAO.getByUsername("nonexistent");

        // Verify null result and session closure
        assertNull(result);
        verify(session).close();
    }

    // Test case: Password verification (successful case)
    @Test
    public void testIsPasswordCorrect_Success() {
        // Create test admin with correct password
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("password");

        // Configure mock to return the test admin
        when(query.list()).thenReturn(Collections.singletonList(admin));
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test password verification
        boolean result = adminDAO.isPasswordCorrect("admin", "password");

        // Verify successful verification and session closure
        assertTrue(result);
        verify(session).close();
    }

    // Test case: Password verification (unsuccessful case)
    @Test
    public void testIsPasswordCorrect_Failure() {
        // Create test admin with different password
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("wrongpassword");

        // Configure mock to return the test admin
        when(query.list()).thenReturn(Collections.singletonList(admin));
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test password verification with wrong password
        boolean result = adminDAO.isPasswordCorrect("admin", "password");

        // Verify failed verification and session closure
        assertFalse(result);
        verify(session).close();
    }

    // Test case: Password verification (admin not found)
    @Test
    public void testIsPasswordCorrect_AdminNotFound() {
        // Configure mock to return empty result
        when(query.list()).thenReturn(Collections.emptyList());
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test password verification for non-existent admin
        boolean result = adminDAO.isPasswordCorrect("admin", "password");

        // Verify failed verification and session closure
        assertFalse(result);
        verify(session).close();
    }

    // Test case: Username existence check with exception
    @Test
    public void testIsUsernameExists_Exception() {
        // Configure mock to throw exception
        when(query.list()).thenThrow(new RuntimeException("Database error"));
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test username check with database error
        boolean exists = adminDAO.isUsernameExists("admin");

        // Verify negative result and session closure
        assertFalse(exists);
        verify(session).close();
    }

    // Test case: Get admin by username with exception
    @Test
    public void testGetByUsername_Exception() {
        // Configure mock to throw exception
        when(query.list()).thenThrow(new RuntimeException("Database error"));
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test retrieving admin with database error
        Admin result = adminDAO.getByUsername("admin");

        // Verify null result and session closure
        assertNull(result);
        verify(session).close();
    }

    // Test case: Password verification with exception
    @Test
    public void testIsPasswordCorrect_Exception() {
        // Configure mock to throw exception
        when(query.list()).thenThrow(new RuntimeException("Database error"));
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // Test password verification with database error
        boolean result = adminDAO.isPasswordCorrect("admin", "password");

        // Verify negative result and session closure
        assertFalse(result);
        verify(session).close();
    }
}