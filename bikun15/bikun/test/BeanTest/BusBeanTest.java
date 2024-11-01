package BeanTest;

import bean.BusBean;
import DAO.BusDAO;
import helper.FacesContextHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import pojo.Bus;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

// Test class for BusBean functionality
public class BusBeanTest {
    // Class-level variables for the components under test and their mocks
    private BusBean busBean;        // The main class being tested
    private BusDAO busDAOMock;      // Mock object for database operations
    private FacesContextHelper facesContextHelperMock;  // Mock object for JSF context handling

    // Setup method run before each test
    @Before
    public void setUp() {
        // Create mock instances using Mockito
        busDAOMock = mock(BusDAO.class);
        facesContextHelperMock = mock(FacesContextHelper.class);

        // Initialize BusBean with mocked DAO and inject mocked FacesContextHelper
        busBean = new BusBean(busDAOMock);
        busBean.setFacesContextHelper(facesContextHelperMock);
    }

    // Test successful bus addition scenario
    @Test
    public void testAddBus_Successful() {
        // Set up test data with valid bus information
        busBean.getNewBus().setNoBody("ABC-1234");
        busBean.getNewBus().setPlatNomor("B 1234 CD");
        busBean.getNewBus().setTipeBus("MiniBus");
        busBean.getNewBus().setStatus("deploy");

        // Execute the method being tested
        busBean.addBus();

        // Capture the Bus object passed to addBus to verify its state
        ArgumentCaptor<Bus> busCaptor = ArgumentCaptor.forClass(Bus.class);
        verify(busDAOMock).addBus(busCaptor.capture());
        Bus capturedBus = busCaptor.getValue();
        

        // Assert that the Bus object has the expected values
        assertEquals("Bus noBody should match", "ABC-1234", capturedBus.getNoBody());
        assertEquals("Bus platNomor should match", "B 1234 CD", capturedBus.getPlatNomor());
        assertEquals("Bus tipeBus should match", "MiniBus", capturedBus.getTipeBus());
        assertEquals("Bus status should match", "deploy", capturedBus.getStatus());

        // Verify the success message was added correctly
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());
        FacesMessage message = messageCaptor.getValue();
        assertEquals("successful addition", FacesMessage.SEVERITY_INFO, message.getSeverity());
        assertEquals("Message summary", "Bus added successfully!", message.getSummary());
        
        // Output ke console
        System.out.println("testAddBus_Successful - Bus berhasil ditambahkan:");
        System.out.println("No Body: " + capturedBus.getNoBody());
        System.out.println("Plat Nomor: " + capturedBus.getPlatNomor());
        System.out.println("Tipe Bus: " + capturedBus.getTipeBus());
        System.out.println("Status: " + capturedBus.getStatus());
        System.out.println("=============================================");
    }


    // Test bus addition with exception handling
    // rev pijit rev testAddBus_Successful
    @Test
    public void testAddBus_Exception() {
        // Set up test data with valid bus information
        busBean.getNewBus().setNoBody("ABC-1234");
        busBean.getNewBus().setPlatNomor("B 1234 CD");
        busBean.getNewBus().setTipeBus("MiniBus");
        busBean.getNewBus().setStatus("deploy");

        // Configure mock to throw an exception when addBus is called
        String errorMessage = "Add exception";
        doThrow(new RuntimeException(errorMessage)).when(busDAOMock).addBus(any(Bus.class));

        // Execute the method under test
        busBean.addBus();

        // Verify that the error message was added to the context
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());
        FacesMessage message = messageCaptor.getValue();

        // Verify the FacesMessage details for the error
        assertEquals("exception", FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("exception detail", "Error adding bus: " + errorMessage, message.getSummary());

        // Verify that the bus attributes remain unchanged after the error
        assertNotNull("Bus object should still be present after failure", busBean.getNewBus());
        assertEquals("Bus noBody should remain unchanged after exception", "ABC-1234", busBean.getNewBus().getNoBody());
        assertEquals("Bus platNomor should remain unchanged after exception", "B 1234 CD", busBean.getNewBus().getPlatNomor());
        assertEquals("Bus tipeBus should remain unchanged after exception", "MiniBus", busBean.getNewBus().getTipeBus());
        assertEquals("Bus status should remain unchanged after exception", "deploy", busBean.getNewBus().getStatus());
    }

    @Test
    public void testAddBus_InvalidData() {
        // Set invalid bus number format
        busBean.getNewBus().setNoBody("INVALID");

        // Attempt to add invalid bus
        busBean.addBus();

        // Assert that bus was not added and error message was shown
        verify(busDAOMock, never()).addBus(any(Bus.class));
        verify(facesContextHelperMock, times(1)).addMessage(eq("noBody"), any(FacesMessage.class));

        // Assert the operation failed by checking the outcome
        assertFalse("Bus should not be added with invalid data", busBean.isNoBodyValid());
    }


    // Test successful bus update scenario
    @Test
    public void testUpdateBus_Successful() {
        // Create initial bus
        Bus initialBus = new Bus();
        initialBus.setNoBody("ABC-1234");
        initialBus.setPlatNomor("B 1234 AB");
        initialBus.setTipeBus("StandardBus");
        initialBus.setStatus("active");

        // Reset invocations to ignore constructor calls
        reset(busDAOMock);

        // Setup the selected bus in BusBean (simulating the edit() method call)
        busBean.edit(initialBus);

        // Update the selected bus with new values
        Bus selectedBus = busBean.getSelectedBus();
        selectedBus.setNoBody("XYZ-5678");
        selectedBus.setPlatNomor("B 5678 XY");
        selectedBus.setTipeBus("MiniBus");
        selectedBus.setStatus("maintenance");

        // Execute update
        busBean.update();

        // Verify the DAO was called with correct updated data
        ArgumentCaptor<Bus> busCaptor = ArgumentCaptor.forClass(Bus.class);
        verify(busDAOMock).updateBus(busCaptor.capture());
        Bus capturedBus = busCaptor.getValue();

        // Assert each updated field
        assertEquals("Bus number was updated from ABC-1234 to XYZ-5678", 
            "XYZ-5678", capturedBus.getNoBody());
        assertEquals("License plate was updated from B 1234 AB to B 5678 XY", 
            "B 5678 XY", capturedBus.getPlatNomor());
        assertEquals("Bus type was updated from StandardBus to MiniBus", 
            "MiniBus", capturedBus.getTipeBus());
        assertEquals("Status was updated from active to maintenance", 
            "maintenance", capturedBus.getStatus());

        // Verify that loadBuses and loadAvailableBuses were called after update
        verify(busDAOMock, times(1)).getAllBuses();
        verify(busDAOMock, times(1)).getAvailableBuses();

        // Verify success message
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());
        FacesMessage message = messageCaptor.getValue();

        assertEquals("success u", 
            FacesMessage.SEVERITY_INFO, message.getSeverity());
        assertEquals("success message", 
            "Bus updated successfully!", message.getSummary());

        // Verify form state is reset after update
        assertFalse("Edit form should be hidden after successful update", 
            busBean.isEditFormVisible());
        
        // Output ke console
        System.out.println("testUpdateBus_Successful - Bus berhasil ditambahkan:");
        System.out.println("No Body: " + capturedBus.getNoBody());
        System.out.println("Plat Nomor: " + capturedBus.getPlatNomor());
        System.out.println("Tipe Bus: " + capturedBus.getTipeBus());
        System.out.println("Status: " + capturedBus.getStatus());
        System.out.println("=============================================");
    }

    
    @Test
    public void UpdateBus_InvalidData() {
        // Create initial bus
        Bus initialBus = new Bus();
        initialBus.setNoBody("ABC-1234");
        initialBus.setPlatNomor("B 1234 CD");
        initialBus.setTipeBus("MiniBus");
        initialBus.setStatus("deploy");

        // Reset invocations to ignore constructor calls
        reset(busDAOMock);

        // Setup the selected bus in BusBean 
        busBean.edit(initialBus);

        // Capture the bus before update
        Bus selectedBus = busBean.getSelectedBus();
        selectedBus.setNoBody("invalid");  // Invalid format

        // Execute update
        busBean.update();

        // Verify that updateBus was called with the invalid data
        verify(busDAOMock).updateBus(selectedBus);

        // Verify info message was shown (as per current implementation)
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(isNull(), messageCaptor.capture());

        FacesMessage capturedMessage = messageCaptor.getValue();
        assertEquals(FacesMessage.SEVERITY_INFO, capturedMessage.getSeverity());
        assertEquals("Bus updated successfully!", capturedMessage.getSummary());
    }


    // Test successful bus deletion
    @Test
    public void testDeleteBus_Successful() {
        // Set up test data
        Bus bus = new Bus();
        bus.setNoBody("ABC-1234");
        bus.setPlatNomor("B 1234 CD");
        bus.setTipeBus("MiniBus");
        
        // Capture the bus being deleted
        ArgumentCaptor<Bus> busCaptor = ArgumentCaptor.forClass(Bus.class);

        // Execute
        busBean.delete(bus);

        // Verify DAO interaction
        verify(busDAOMock).deleteBus(busCaptor.capture());
        Bus capturedBus = busCaptor.getValue();
        
        // Verify correct bus was deleted
        assertEquals("Deleted bus number should match", "ABC-1234", capturedBus.getNoBody());
        assertEquals("Deleted license plate should match", "B 1234 CD", capturedBus.getPlatNomor());
        assertEquals("Deleted bus type should match", "MiniBus", capturedBus.getTipeBus());

        // Verify success message
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());
        FacesMessage message = messageCaptor.getValue();
        
        assertEquals("Should have info severity", FacesMessage.SEVERITY_INFO, message.getSeverity());
        assertEquals("Should have success message", "Bus deleted successfully!", message.getSummary());
        
        // Output ke console
        System.out.println("testDeleteBus_Successful - Bus berhasil dihapus:");
        System.out.println("=============================================");
    }

    // Test bus deletion with exception handling
    @Test
    public void testDeleteBus_Exception() {
        // Setup test data and mock exception
        Bus bus = new Bus();
        bus.setNoBody("ABC-1234");
        doThrow(new RuntimeException("Delete exception")).when(busDAOMock).deleteBus(any(Bus.class));

        // Execute deletion
        busBean.delete(bus);

        // Verify error handling
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());
        FacesMessage message = messageCaptor.getValue();
        assertEquals(FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("Error deleting bus: Delete exception", message.getSummary());
    }

    // Test loading all buses
    @Test
    public void testLoadBuses() {
        // Create mock bus list
        List<Bus> mockBusList = new ArrayList<>();
        mockBusList.add(new Bus());
        when(busDAOMock.getAllBuses()).thenReturn(mockBusList);

        // Load buses and verify
        busBean.loadBuses();
        verify(busDAOMock, times(2)).getAllBuses();
        assertEquals(1, busBean.getBusList().size());
    }

    // Test loading only available buses
    @Test
    public void testLoadAvailableBuses() {
        // Create mock available bus list
        List<Bus> availableBuses = new ArrayList<>();
        availableBuses.add(new Bus());
        when(busDAOMock.getAvailableBuses()).thenReturn(availableBuses);

        // Load available buses and verify
        busBean.loadAvailableBuses();
        verify(busDAOMock, times(2)).getAvailableBuses();
        assertEquals(1, busBean.getAvailableBuses().size());
    }

    // Test resetting new bus form
    @Test
    public void testResetNewBus() {
        busBean.resetNewBus();
        assertNotNull(busBean.getNewBus());
        assertEquals(false, busBean.isAddFormVisible());
    }

    // Test resetting selected bus form
    @Test
    public void testResetSelectedBus() {
        busBean.resetSelectedBus();
        assertNotNull(busBean.getSelectedBus());
        assertEquals(false, busBean.isEditFormVisible());
    }

    // Test various validation methods
    @Test
    public void testValidationMethods() {
        // Test valid cases
        busBean.getNewBus().setNoBody("ABC-1234");
        busBean.getNewBus().setPlatNomor("B 1234 CD");
        busBean.getNewBus().setTipeBus("Bus");

        // Verify valid inputs
        assertEquals(true, busBean.isNoBodyValid());
        assertEquals(true, busBean.isPlatNomorValid());
        assertEquals(true, busBean.isTipeBusValid());
        assertEquals(false, busBean.isAddFormVisible());

        // Test invalid cases
        busBean.getNewBus().setNoBody("1234");
        assertEquals(false, busBean.isNoBodyValid());

        busBean.getNewBus().setPlatNomor("INVALID");
        assertEquals(false, busBean.isPlatNomorValid());

        busBean.getNewBus().setTipeBus("B");
        assertEquals(false, busBean.isTipeBusValid());
    }

    // Test validator for invalid bus number format
    @Test(expected = ValidatorException.class)
    public void testNoBodyValidator_Invalid() {
        busBean.noBodyValidator(null, null, "123");
    }

    // Test validator for invalid license plate format
    @Test(expected = ValidatorException.class)
    public void testPlatNomorValidator_Invalid() {
        busBean.platNomorValidator(null, null, "XYZ-INVALID");
    }

    // Test validator for invalid bus type
    @Test(expected = ValidatorException.class)
    public void testTipeBusValidator_Invalid() {
        busBean.tipeBusValidator(null, null, "C");
    }
}