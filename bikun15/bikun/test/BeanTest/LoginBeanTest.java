package BeanTest;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import bean.LoginBean;
import DAO.AdminDAO;

public class LoginBeanTest {
    // Automatically inject mocked AdminDAO into this LoginBean instance
    @InjectMocks
    private LoginBean loginBean;

    // Create a mock of AdminDAO to simulate database operations
    @Mock
    private AdminDAO adminDAO;

    // Setup method that runs before each test
    @Before
    public void setUp() {
        // Initialize all mock annotations
        MockitoAnnotations.initMocks(this);
    }

    // Test case: Validate empty username input
    @Test
    public void testValidateUsername_EmptyUsername() {
        // Set empty username
        loginBean.setUsername("");
        // Attempt to validate the username
        loginBean.validateUsername();
        // Verify that appropriate error message is set
        assertEquals("Username cannot be empty", loginBean.getMessage());
    }

    // Test case: Validate non-existing username
    @Test
    public void testValidateUsername_NonExistingUsername() {
        // Mock behavior: any username check returns false (username doesn't exist)
        when(adminDAO.isUsernameExists(anyString())).thenReturn(false);
        // Set test username
        loginBean.setUsername("nonexistent");
        // Attempt to validate the username
        loginBean.validateUsername();
        // Verify error message for non-existent username
        assertEquals("Username does not exist", loginBean.getMessage());
    }

    // Test case: Validate existing username
    @Test
    public void testValidateUsername_ExistingUsername() {
        // Mock behavior: any username check returns true (username exists)
        when(adminDAO.isUsernameExists(anyString())).thenReturn(true);
        // Set test username
        loginBean.setUsername("admin");
        // Attempt to validate the username
        loginBean.validateUsername();
        // Verify no error message for valid username
        assertEquals("", loginBean.getMessage());
    }

    // Test case: Validate empty password input
    @Test
    public void testValidatePassword_EmptyPassword() {
        // Set empty password
        loginBean.setPassword("");
        // Attempt to validate the password
        loginBean.validatePassword();
        // Verify appropriate error message for empty password
        assertEquals("Password cannot be empty", loginBean.getMessage());
    }

    // Test case: Validate incorrect password
    @Test
    public void testValidatePassword_IncorrectPassword() {
        // Mock behavior: any password check returns false (incorrect password)
        when(adminDAO.isPasswordCorrect(anyString(), anyString())).thenReturn(false);
        // Set test credentials
        loginBean.setUsername("admin");
        loginBean.setPassword("wrongpassword");
        // Attempt to validate the password
        loginBean.validatePassword();
        // Verify error message for incorrect password
        assertEquals("Incorrect password", loginBean.getMessage());
    }

    // Test case: Validate correct password
    @Test
    public void testValidatePassword_CorrectPassword() {
        // Mock behavior: any password check returns true (correct password)
        when(adminDAO.isPasswordCorrect(anyString(), anyString())).thenReturn(true);
        // Set test credentials
        loginBean.setUsername("admin");
        loginBean.setPassword("password");
        // Attempt to validate the password
        loginBean.validatePassword();
        // Verify no error message for correct password
        assertEquals("", loginBean.getMessage());
    }

    // Test case: Successful login attempt
    @Test
    public void testLogin_Success() {
        // Mock behavior: validation returns true (successful login)
        when(adminDAO.validate(anyString(), anyString())).thenReturn(true);
        // Set valid credentials
        loginBean.setUsername("admin");
        loginBean.setPassword("password");
        // Attempt login
        String result = loginBean.login();
        // Verify successful login result and no error message
        assertEquals("success", result);
        assertEquals("", loginBean.getMessage());
    }

    // Test case: Failed login attempt
    @Test
    public void testLogin_Failure() {
        // Mock behavior: validation returns false (failed login)
        when(adminDAO.validate(anyString(), anyString())).thenReturn(false);
        // Set invalid credentials
        loginBean.setUsername("admin");
        loginBean.setPassword("wrongpassword");
        // Attempt login
        String result = loginBean.login();
        // Verify failed login result and appropriate error message
        assertEquals("error", result);
        assertEquals("Invalid Username or Password", loginBean.getMessage());
    }
    
    // @Test
    // public void testLogin_SpecialChar() {
    //     ...
    // }
}