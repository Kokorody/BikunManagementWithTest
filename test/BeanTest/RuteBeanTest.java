// Test class
package BeanTest;

import bean.RuteBean;
import DAO.RuteDAO;
import helper.FacesContextHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import pojo.Rute;

import javax.faces.application.FacesMessage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RuteBeanTest {
    // Test class fields
    private RuteBean ruteBean;
    private RuteDAO ruteDAOMock;
    private FacesContextHelper facesContextHelperMock;

    // Setup method that runs before each test
    @Before
    public void setUp() {
        ruteDAOMock = mock(RuteDAO.class);
        facesContextHelperMock = mock(FacesContextHelper.class);
        ruteBean = new RuteBean(ruteDAOMock, facesContextHelperMock);
    }

    // Test successful route addition
@Test
public void testAddRute_Successful() {
    // Set up test data with valid route information
    ruteBean.getNewRute().setIdRute("RUTE123");
    ruteBean.getNewRute().setJurusan("Jakarta - Bogor");

    // Execute the method being tested
    ruteBean.addRute();

    // Capture the Rute object passed to addRute to verify its state
    ArgumentCaptor<Rute> ruteCaptor = ArgumentCaptor.forClass(Rute.class);
    verify(ruteDAOMock).addRute(ruteCaptor.capture());
    Rute capturedRute = ruteCaptor.getValue();
    
    // Assert that the Rute object has the expected values
    assertEquals("Rute idRute should match", "RUTE123", capturedRute.getIdRute());
    assertEquals("Rute jurusan should match", "Jakarta - Bogor", capturedRute.getJurusan());

    // Verify the success message was added correctly
    ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
    verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());
    FacesMessage message = messageCaptor.getValue();
    assertEquals("successful addition", FacesMessage.SEVERITY_INFO, message.getSeverity());
    assertEquals("Message summary", "Rute added successfully!", message.getSummary());

    // Output to console
    System.out.println("testAddRute_Successful - Rute berhasil ditambahkan:");
    System.out.println("ID Rute: " + capturedRute.getIdRute());
    System.out.println("Jurusan: " + capturedRute.getJurusan());
    System.out.println("=============================================");
    
    // Check that new route is not null and form visibility
    assertNotNull("New route should not be null after addition.", ruteBean.getNewRute());
    assertFalse("Add form should be hidden after successful addition.", ruteBean.isAddFormVisible());
}


    // Test route addition with exception
    @Test
    public void testAddRute_Exception() {
        Rute newRute = new Rute();
        newRute.setIdRute("RUTE123");
        ruteBean.getNewRute().setIdRute(newRute.getIdRute());

        doThrow(new RuntimeException("DAO error")).when(ruteDAOMock).addRute(any(Rute.class));
        ruteBean.addRute();

        verify(ruteDAOMock).addRute(any(Rute.class));
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());

        FacesMessage message = messageCaptor.getValue();
        assertEquals("error message ", FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("message summary", "Error adding rute: DAO error", message.getSummary());
    }

    // Test successful route update
    @Test
    public void testUpdateRute_Successful() {
        // Create initial route
        Rute initialRute = new Rute();
        initialRute.setIdRute("RUTE123");
        // You might want to set other fields here if they exist

        // Reset invocations to ignore constructor calls
        reset(ruteDAOMock);

        // Simulating the edit() method call
        ruteBean.edit(initialRute);

        // Update the selected route with new values
        Rute selectedRute = ruteBean.getSelectedRute();
        selectedRute.setIdRute("RUTE456"); // Example of changing the ID
        // Set other fields of selectedRute as needed here

        // Execute update
        doNothing().when(ruteDAOMock).updateRute(any(Rute.class));
        ruteBean.update();

        // Verify the DAO was called with correct updated data
        ArgumentCaptor<Rute> ruteCaptor = ArgumentCaptor.forClass(Rute.class);
        verify(ruteDAOMock, times(1)).updateRute(ruteCaptor.capture());
        Rute capturedRute = ruteCaptor.getValue();

        // Assert each updated field (update these assertions based on your actual fields)
        assertEquals("Route ID should be updated.", "RUTE456", capturedRute.getIdRute());
        // Add other assertions for additional fields if necessary

        // Verify success message
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock, times(1)).addMessage(eq(null), messageCaptor.capture());
        FacesMessage message = messageCaptor.getValue();

        assertEquals("success ", FacesMessage.SEVERITY_INFO, message.getSeverity());
        assertEquals("success message", "Rute updated successfully!", message.getSummary());

        // Verify that selected route is not null after update
        assertNotNull("Selected route should not be null after update.", ruteBean.getSelectedRute());

        // Verify form state is reset after update
        assertFalse("Edit form should be hidden after successful update.", ruteBean.isEditFormVisible());

        // Output to console for verification
        System.out.println("testUpdateRute_Successful - Route successfully updated:");
        System.out.println("Route ID: " + capturedRute.getIdRute());
        System.out.println("=============================================");
    }


    // Test route update with exception
    @Test
    public void testUpdateRute_Exception() {
        Rute ruteToUpdate = new Rute();
        ruteToUpdate.setIdRute("RUTE123");
        ruteBean.edit(ruteToUpdate);

        doThrow(new RuntimeException("Update error")).when(ruteDAOMock).updateRute(any(Rute.class));
        ruteBean.update();

        verify(ruteDAOMock).updateRute(any(Rute.class));
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());

        FacesMessage message = messageCaptor.getValue();
        assertEquals("failed route update.", FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("message summary ", "Error updating rute: Update error", message.getSummary());
    }

    // Test successful route deletion
    @Test
    public void testDeleteRute_Successful() {
        Rute ruteToDelete = new Rute();
        ruteToDelete.setIdRute("RUTE123");

        doNothing().when(ruteDAOMock).deleteRute(any(Rute.class));
        ruteBean.delete(ruteToDelete);

        verify(ruteDAOMock, times(1)).deleteRute(ruteToDelete);
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock, times(1)).addMessage(eq(null), messageCaptor.capture());

        FacesMessage message = messageCaptor.getValue();
        assertEquals("deleting route.", FacesMessage.SEVERITY_INFO, message.getSeverity());
        assertEquals("message summary", "Rute deleted successfully!", message.getSummary());
        
        // Output ke console
        System.out.println("testDeleteRute_Successful - Rute berhasil dihapus:");
        System.out.println("=============================================");        
    }

    // Test route deletion with exception
    @Test
    public void testDeleteRute_Exception() {
        Rute ruteToDelete = new Rute();
        ruteToDelete.setIdRute("RUTE123");

        doThrow(new RuntimeException("Delete error")).when(ruteDAOMock).deleteRute(any(Rute.class));
        ruteBean.delete(ruteToDelete);

        verify(ruteDAOMock).deleteRute(any(Rute.class));
        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(facesContextHelperMock).addMessage(eq(null), messageCaptor.capture());

        FacesMessage message = messageCaptor.getValue();
        assertEquals("failed route deletion.", FacesMessage.SEVERITY_ERROR, message.getSeverity());
        assertEquals("message summary", "Error deleting rute: Delete error", message.getSummary());
    }

    // Test loading all routes
    @Test
    public void testLoadRutes() {
        List<Rute> mockRuteList = new ArrayList<>();
        mockRuteList.add(new Rute());

        when(ruteDAOMock.getAllRutes()).thenReturn(mockRuteList);
        ruteBean.loadRutes();

        verify(ruteDAOMock, times(2)).getAllRutes();
        assertEquals("Expected one route to be loaded into rute list.", 1, ruteBean.getRuteList().size());
    }

    // Test resetting new route form
    @Test
    public void testResetNewRute() {
        ruteBean.resetNewRute();

        assertNotNull("New route should be reinitialized after reset.", ruteBean.getNewRute());
        assertFalse("Add form should be hidden after reset.", ruteBean.isAddFormVisible());
    }

    // Test resetting selected route form
    @Test
    public void testResetSelectedRute() {
        ruteBean.resetSelectedRute();

        assertNotNull("Selected route should be reinitialized after reset.", ruteBean.getSelectedRute());
        assertFalse("Edit form should be hidden after reset.", ruteBean.isEditFormVisible());
    }
}
