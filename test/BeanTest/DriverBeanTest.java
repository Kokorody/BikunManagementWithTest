package BeanTest;

import bean.DriverBean;
import DAO.DriverDAO;
import helper.FacesContextHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import pojo.Driver;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DriverBeanTest {
    // Class fields for the test dependencies
    private DriverBean driverBean;        // The main class being tested
    private DriverDAO driverDAOMock;      // Mock object for database operations
    private FacesContextHelper facesContextHelperMock;  // Mock object for JSF context operations

    // Setup method that runs before each test
    @Before
    public void setUp() {
        // Create mock objects for dependencies
        driverDAOMock = mock(DriverDAO.class);
        facesContextHelperMock = mock(FacesContextHelper.class);
        // Initialize the main test subject with mock dependencies
        driverBean = new DriverBean(driverDAOMock, facesContextHelperMock);
    }

    // Test successful driver addition
    @Test
    public void testAddDriver_Successful() {
        // Setup test data with valid driver details
        Driver newDriver = new Driver();
        newDriver.setNamaDriver("John Doe");
        newDriver.setNoSim("1234567890123456");
        newDriver.setOperator("Operator XYZ");

        // Set the driver information in the bean
        driverBean.getNewDriver().setNamaDriver(newDriver.getNamaDriver());
        driverBean.getNewDriver().setNoSim(newDriver.getNoSim());
        driverBean.getNewDriver().setOperator(newDriver.getOperator());

        // Execute the add driver operation
        driverBean.addDriver();

        // Capture the Driver object passed to addDriver to verify its state
        ArgumentCaptor<Driver> driverCaptor = ArgumentCaptor.forClass(Driver.class);
        verify(driverDAOMock).addDriver(driverCaptor.capture());
        Driver capturedDriver = driverCaptor.getValue();

        // Assert that the Driver object has the expected values
        assertEquals("Driver name should match", "John Doe", capturedDriver.getNamaDriver());
        assertEquals("Driver SIM number should match", "1234567890123456", capturedDriver.getNoSim());
        assertEquals("Driver operator should match", "Operator XYZ", capturedDriver.getOperator());

        // Verify the success message was added correctly
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());
        FacesMessage message = messageCaptor.getValue();
        assertEquals("successful addition", FacesMessage.SEVERITY_INFO, message.getSeverity());
        assertEquals("Message summary", "Driver berhasil ditambahkan!", message.getSummary());

        // Output ke console
        System.out.println("testAddDriver_Successful - Driver berhasil ditambahkan:");
        System.out.println("Nama Driver: " + capturedDriver.getNamaDriver());
        System.out.println("No SIM: " + capturedDriver.getNoSim());
        System.out.println("Operator: " + capturedDriver.getOperator());
        System.out.println("=============================================");
    }

        // Test driver addition with invalid SIM number
        @Test
        public void testAddDriver_Invalid() {
            // Setup test with invalid SIM number
            driverBean.getNewDriver().setNoSim("INVALID");

            // Execute the add driver operation
            driverBean.addDriver();

            // Verify that DAO method was not called and error message was shown
            verify(driverDAOMock, never()).addDriver(any(Driver.class));
            verify(facesContextHelperMock, times(1)).addMessage(eq("noSim"), any(FacesMessage.class));
            
            // add message 2 console
            System.out.println("Driver Failed to Add due to invalid data()");
        }

    // Test exception handling during driver addition
    @Test
    public void testAddDriver_ExceptionHandling() {
        // Setup test data and mock exception
        Driver newDriver = new Driver();
        newDriver.setNoSim("1234567890123456");
        driverBean.getNewDriver().setNoSim(newDriver.getNoSim());
        doThrow(new RuntimeException("Test Exception")).when(driverDAOMock).addDriver(any(Driver.class));

        // Execute the add driver operation
        driverBean.addDriver();

        // Verify exception handling behavior
        verify(driverDAOMock, times(1)).addDriver(any(Driver.class));
        verify(facesContextHelperMock, times(1)).addMessage(eq(null), any(FacesMessage.class));
    }

    // Test successful driver update
    @Test
    public void testUpdateDriver_Successful() {
        // Setup initial driver data
        Driver initialDriver = new Driver();
        initialDriver.setNoSim("1234567890123456");
        initialDriver.setNamaDriver("John Doe");
        initialDriver.setOperator("Operator A");

        // Reset invocations to ignore constructor calls
        reset(driverDAOMock);

        // Simulate the edit() method call in DriverBean
        driverBean.edit(initialDriver);

        // Update the selected driver with new values
        Driver selectedDriver = driverBean.getSelectedDriver();
        selectedDriver.setNoSim("6543210987654321");
        selectedDriver.setNamaDriver("Jane Doe");
        selectedDriver.setOperator("Operator B");

        // Mock void method behavior for the update operation
        doNothing().when(driverDAOMock).updateDriver(any(Driver.class));

        // Execute update operation
        driverBean.update();

        // Verify the DAO was called with the correct updated data
        ArgumentCaptor<Driver> driverCaptor = ArgumentCaptor.forClass(Driver.class);
        verify(driverDAOMock).updateDriver(driverCaptor.capture());
        Driver capturedDriver = driverCaptor.getValue();

        // Assert each updated field
        assertEquals("Driver's SIM number should be updated", 
            "6543210987654321", capturedDriver.getNoSim());
        assertEquals("Driver's name should be updated", 
            "Jane Doe", capturedDriver.getNamaDriver());
        assertEquals("Driver's operator should be updated", 
            "Operator B", capturedDriver.getOperator());

        // Verify success message
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());
        FacesMessage message = messageCaptor.getValue();

        assertEquals("success ", 
            FacesMessage.SEVERITY_INFO, message.getSeverity());
        assertEquals("success message", 
            "Driver berhasil diperbarui!", message.getSummary());

        // Verify form state is reset after update
        assertFalse("Edit form should be hidden after successful update", 
            driverBean.isEditFormVisible());

        // Output ke console
        System.out.println("testUpdateDriver_Successful - Driver berhasil diperbarui:");
        System.out.println("No SIM: " + capturedDriver.getNoSim());
        System.out.println("Nama Driver: " + capturedDriver.getNamaDriver());
        System.out.println("Operator: " + capturedDriver.getOperator());
        System.out.println("=============================================");
    }


    // Test driver update with invalid SIM
    @Test
    public void testUpdateDriver_Invalid() {
        // Setup test with invalid Driver Data
        Driver driver = new Driver();
        driver.setNoSim("INVALID");
        driverBean.edit(driver);

        // Execute update operation
        driverBean.update();

        // Verify validation behavior
        verify(driverDAOMock, never()).updateDriver(any(Driver.class));
        verify(facesContextHelperMock, times(1)).addMessage(eq("noSim"), any(FacesMessage.class));
        
        // Print message to console if the updateDriver method fails due to invalid data
        System.out.println("Update Driver failed: Invalid data");
        System.out.println("=============================================");
    }

    // Test exception handling during driver update
    @Test
    public void testUpdateDriver_ExceptionHandling() {
        // Setup test data and mock exception
        Driver driver = new Driver();
        driver.setNoSim("1234567890123456");
        driverBean.edit(driver);
        doThrow(new RuntimeException("Test Exception")).when(driverDAOMock).updateDriver(any(Driver.class));

        // Execute update operation
        driverBean.update();

        // Verify exception handling behavior
        verify(driverDAOMock, times(1)).updateDriver(driver);
        verify(facesContextHelperMock, times(1)).addMessage(eq(null), any(FacesMessage.class));
    }

    // Test successful driver deletion
    @Test
    public void testDeleteDriver_Successful() {
        // Setup test data
        Driver driver = new Driver();
        driver.setNoSim("1234567890123456");

        // Mock void method behavior
        doNothing().when(driverDAOMock).deleteDriver(any(Driver.class));

        // Execute delete operation
        driverBean.delete(driver);

        // Verify deletion behavior and success message
        verify(driverDAOMock, times(1)).deleteDriver(driver);
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock, times(1)).addMessage(eq(null), messageCaptor.capture());

        FacesMessage message = messageCaptor.getValue();
        assertEquals(FacesMessage.SEVERITY_INFO, message.getSeverity());
        assertEquals("Driver berhasil dihapus!", message.getSummary());
        
        // Output ke console
        System.out.println("testDeleteDriver_Successful - Driver berhasil dihapus:");
        System.out.println("=============================================");
    }

    // Test exception handling during driver deletion
    @Test
    public void testDeleteDriver_ExceptionHandling() {
        // Setup test data and mock exception
        Driver driver = new Driver();
        driver.setNoSim("1234567890123456");
        doThrow(new RuntimeException("Test Exception")).when(driverDAOMock).deleteDriver(any(Driver.class));

        // Execute delete operation
        driverBean.delete(driver);

        // Verify exception handling behavior
        verify(driverDAOMock, times(1)).deleteDriver(any(Driver.class));
        verify(facesContextHelperMock, times(1)).addMessage(eq(null), any(FacesMessage.class));
    }

    // Test form visibility toggling functionality
    @Test
    public void testFormVisibility_Toggles() {
        // Setup test data
        Driver driver = new Driver();

        // Test edit form visibility
        driverBean.edit(driver);
        assertTrue(driverBean.isEditFormVisible());

        driverBean.resetSelectedDriver();
        assertFalse(driverBean.isEditFormVisible());

        // Test add form visibility
        driverBean.getNewDriver().setNoSim("1234567890123456");
        driverBean.addDriver();
        assertFalse(driverBean.isAddFormVisible());
    }

    // Test valid SIM number validation
    @Test
    public void testIsNoSimValid_ValidSim() {
        // Test valid SIM number
        boolean result = driverBean.isNoSimValid("1234567890123456");
        assertTrue(result);
    }

    // Test invalid SIM number validation
    @Test
    public void testIsNoSimValid_InvalidSim() {
        // Test invalid SIM number
        boolean result = driverBean.isNoSimValid("INVALID_SIM");
        assertFalse(result);
    }

    // Test SIM number validator with valid input
    @Test
    public void testNoSimValidator_Valid() {
        // Test valid SIM validation
        boolean isValid = driverBean.isNoSimValid("1234567890123456");
        assertTrue(isValid);
    }

    // Test SIM number validator with invalid input
    @Test(expected = ValidatorException.class)
    public void testNoSimValidator_Invalid() {
        // Setup invalid SIM number
        String invalidNoSim = "INVALID_SIM";

        // Execute validation (expects exception)
        driverBean.noSimValidator(null, null, invalidNoSim);
    }

    // Test loading all drivers
    @Test
    public void testLoadDrivers() {
        // Setup mock data
        List<Driver> mockDriverList = new ArrayList<>();
        mockDriverList.add(new Driver());
        when(driverDAOMock.getAllDrivers()).thenReturn(mockDriverList);

        // Execute load operation
        driverBean.loadDrivers();

        // Verify loading behavior
        verify(driverDAOMock, times(2)).getAllDrivers();
        assertEquals(1, driverBean.getDriverList().size());
    }

    // Test loading available drivers
    @Test
    public void testLoadAvailableDrivers() {
        // Setup mock data
        List<Driver> availableDrivers = new ArrayList<>();
        availableDrivers.add(new Driver());
        when(driverDAOMock.getAvailableDrivers()).thenReturn(availableDrivers);

        // Execute load operation
        driverBean.loadAvailableDriver();

        // Verify loading behavior
        verify(driverDAOMock, times(2)).getAvailableDrivers();
        assertEquals(1, driverBean.getAvailableDrivers().size());
    }
}