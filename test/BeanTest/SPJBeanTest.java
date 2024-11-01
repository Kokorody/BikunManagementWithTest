package BeanTest;

import bean.SPJBean;                          
import DAO.SPJDAO;                           
import helper.FacesContextHelper;              
import org.junit.Before;                     
import org.junit.Test;                        
import org.mockito.ArgumentCaptor;           
import pojo.Fleet;                            
import pojo.Bus;                             
import pojo.Driver;                          
import pojo.Rute;
//import DAO.RuteDAO;

import javax.faces.application.FacesMessage;   // JSF message class for UI notifications
import java.util.ArrayList;
import java.util.List;

// Static imports for JUnit assertions and Mockito methods
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

// Test class for SPJBean functionality
public class SPJBeanTest {

    // Test class member variables
    private SPJBean spjBean;                  // Main class under test
    private SPJDAO spjDAOMock;                // Mock object for data access
    private FacesContextHelper facesContextHelperMock;  // Mock object for JSF context operations

    // Setup method that runs before each test
    @Before
    public void setUp() {
        // Create mock objects for dependencies
        spjDAOMock = mock(SPJDAO.class);
        facesContextHelperMock = mock(FacesContextHelper.class);
        // Initialize the bean with mocked dependencies using dependency injection
        spjBean = new SPJBean(spjDAOMock, facesContextHelperMock);
    }

    // Test case: Successfully adding a new SPJ (Surat Perintah Jalan)
@Test
public void testAddSPJ_Successful() {
    // Setup test data with valid fleet information
    Fleet newFleet = new Fleet();
    
    // Set up mock Bus data
    Bus mockBus = new Bus();
    mockBus.setIdBus(1); // Set test bus ID
    mockBus.setPlatNomor("B 1234 CD"); // Set test plate number
    mockBus.setTipeBus("MiniBus"); // Set test bus type
    mockBus.setStatus("deploy"); // Set test bus status
    newFleet.setBus(mockBus);

    // Set up mock Driver data
    Driver mockDriver = new Driver();
    mockDriver.setIdDriver(1); // Set test driver ID
    mockDriver.setNamaDriver("John Doe"); // Set test driver name
    mockDriver.setNoSim("SIM123456"); // Set test SIM number
    newFleet.setDriver(mockDriver);

    // Set up mock Route data
    Rute mockRute = new Rute();
    mockRute.setIdRute("Rute 123"); // Set test route ID
    mockRute.setJurusan("Jakarta - Bogor"); // Set test route destination
    newFleet.setRute(mockRute);

    // Set up the bean's new fleet object with test data
    spjBean.getNewFleet().setBus(newFleet.getBus());
    spjBean.getNewFleet().setDriver(newFleet.getDriver());
    spjBean.getNewFleet().setRute(newFleet.getRute());

    // Configure mock behavior - define what should happen when methods are called
    doNothing().when(spjDAOMock).addSPJ(any(Fleet.class));
    doNothing().when(spjDAOMock).updateBusStatus(anyInt(), anyString());

    // Execute the method being tested
    spjBean.addSPJ();

    // Capture the Fleet object passed to addSPJ to verify its state
    ArgumentCaptor<Fleet> fleetCaptor = ArgumentCaptor.forClass(Fleet.class);
    verify(spjDAOMock).addSPJ(fleetCaptor.capture());
    Fleet capturedFleet = fleetCaptor.getValue();

    // Assert that the Fleet object has the expected values
    assertEquals("Fleet bus ID should match", 1, (int) capturedFleet.getBus().getIdBus());
    assertEquals("Fleet driver ID should match", 1, (int) capturedFleet.getDriver().getIdDriver());

    // Verify the bus status was updated correctly
    verify(spjDAOMock).updateBusStatus(eq(capturedFleet.getBus().getIdBus()), eq("deploy"));

    // Capture and verify the success message
    ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
    verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());

    FacesMessage message = messageCaptor.getValue();
    assertEquals(FacesMessage.SEVERITY_INFO, message.getSeverity());
    assertEquals("SPJ berhasil ditambahkan!", message.getSummary());

    // Output ke console
    System.out.println("testAddSPJ_Successful - SPJ berhasil ditambahkan:");
//    System.out.println("Bus ID: " + capturedFleet.getBus().getIdBus());
//    System.out.println("Plate Number: " + capturedFleet.getBus().getPlatNomor());
//    System.out.println("Bus Type: " + capturedFleet.getBus().getTipeBus());
//    System.out.println("Driver Name: " + capturedFleet.getDriver().getNamaDriver());
//    System.out.println("Driver SIM No: " + capturedFleet.getDriver().getNoSim());
//    System.out.println("Route ID: " + capturedFleet.getRute().getIdRute());
//    System.out.println("Route Destination: " + capturedFleet.getRute().getJurusan());
    System.out.println("Bus ID: " + capturedFleet.getBus().getIdBus());
    System.out.println("Driver ID: " + capturedFleet.getDriver().getIdDriver());
    System.out.println("Rute ID: " + capturedFleet.getRute().getIdRute());
    System.out.println("=============================================");
}




    // Test case: Failing to add SPJ due to missing required data
    @Test
    public void testAddSPJ_Failure_NoBusDriverRute() {
        // Reset the fleet object to ensure empty state
        spjBean.resetNewFleet();

        // Execute the method being tested
        spjBean.addSPJ();

        // Capture and verify the error message
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());

        FacesMessage message = messageCaptor.getValue();
        assertEquals(FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("Harap pilih Bus, Driver, dan Rute!", message.getSummary());
    }

//    // Test case: Successfully updating an existing SPJ
//    @Test
//    public void testUpdateSPJ_Successful() {
//        // Setup test data for update
//        Fleet selectedFleet = new Fleet();
//        selectedFleet.setBus(new Bus());
//        selectedFleet.setDriver(new Driver());
//        selectedFleet.setRute(new Rute());
//        
//        // Set up the bean's selected fleet with test data
//        spjBean.getSelectedFleet().setBus(selectedFleet.getBus());
//        spjBean.getSelectedFleet().setDriver(selectedFleet.getDriver());
//        spjBean.getSelectedFleet().setRute(selectedFleet.getRute());
//
//        // Configure mock behavior
//        doNothing().when(spjDAOMock).updateSPJ(any(Fleet.class));
//        
//        // Execute the update method
//        spjBean.update();
//
//        // Verify the update was called
//        verify(spjDAOMock).updateSPJ(any(Fleet.class));
//
//        // Capture and verify the success message
//        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
//        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());
//
//        FacesMessage message = messageCaptor.getValue();
//        assertEquals(FacesMessage.SEVERITY_INFO, message.getSeverity());
//        assertEquals("SPJ berhasil diperbarui!", message.getSummary());
//    }

    // Test case: Successfully deleting an SPJ
    @Test
    public void testDeleteSPJ_Successful() {
        // Setup test data for deletion
        Fleet fleetToDelete = new Fleet();
        fleetToDelete.setBus(new Bus());
        fleetToDelete.getBus().setIdBus(1);
        
        // Configure mock behavior
        doNothing().when(spjDAOMock).deleteSPJ(any(Fleet.class));
        doNothing().when(spjDAOMock).updateBusStatus(anyInt(), anyString());
        
        // Execute the delete method
        spjBean.delete(fleetToDelete);

        // Verify the delete operations were called
        verify(spjDAOMock).deleteSPJ(any(Fleet.class));
        verify(spjDAOMock).updateBusStatus(eq(fleetToDelete.getBus().getIdBus()), eq("depot"));
        
        // Capture and verify the success message
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());

        FacesMessage message = messageCaptor.getValue();
        assertEquals(FacesMessage.SEVERITY_INFO, message.getSeverity());
        assertEquals("SPJ berhasil dihapus!", message.getSummary());
        
        // Output ke console
        System.out.println("testDeleteSPJ_Successful - SPJ berhasil dihapus:");
        System.out.println("=============================================");
    }

    // Test case: Failing to delete an SPJ due to database error
    @Test
    public void testDeleteSPJ_Failure() {
        // Setup test data for deletion
        Fleet fleetToDelete = new Fleet();
        fleetToDelete.setBus(new Bus());
        fleetToDelete.getBus().setIdBus(1);
        
        // Configure mock to throw exception when deleteSPJ is called
        doThrow(new RuntimeException("Database error")).when(spjDAOMock).deleteSPJ(any(Fleet.class));

        // Execute the delete method
        spjBean.delete(fleetToDelete);

        // Verify delete was attempted
        verify(spjDAOMock).deleteSPJ(any(Fleet.class));
        
        // Capture and verify the error message
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());

        FacesMessage message = messageCaptor.getValue();
        assertEquals(FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("Kesalahan saat menghapus SPJ: Database error", message.getSummary());
        
                // Output ke console
        System.out.println("testDeleteSPJ_Failure - SPJ gagal dihapus:");
        System.out.println("=============================================");
    }
}