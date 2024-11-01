package bean;

import DAO.RuteDAO;
import helper.FacesContextHelper; // Assuming you want to use a helper for messages
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import pojo.Rute;

@ManagedBean(name = "ruteBean")
@ViewScoped
public class RuteBean implements Serializable {

    private final RuteDAO ruteDAO;
    private final FacesContextHelper facesContextHelper; // Injected helper for messages
    private List<Rute> ruteList;
    private Rute selectedRute = new Rute();
    private Rute newRute = new Rute();
    private boolean editFormVisible = false;
    private boolean addFormVisible = false;

    // Constructor for dependency injection with RuteDAO and FacesContextHelper
    @Inject
    public RuteBean(RuteDAO ruteDAO, FacesContextHelper facesContextHelper) {
        this.ruteDAO = ruteDAO;
        this.facesContextHelper = facesContextHelper;
        loadRutes();
    }

    // Default constructor used when no injection
    public RuteBean() {
        this(new RuteDAO(), new FacesContextHelper()); // Instantiate default DAO and Helper
    }

    // Method to load all routes from the DAO
    public void loadRutes() {
        ruteList = ruteDAO.getAllRutes();
    }

    // Getter methods
    public List<Rute> getRuteList() {
        return ruteList;
    }

    public Rute getSelectedRute() {
        return selectedRute;
    }

    public Rute getNewRute() {
        return newRute;
    }

    public boolean isEditFormVisible() {
        return editFormVisible;
    }

    public boolean isAddFormVisible() {
        return addFormVisible;
    }

    // Method to add a new route
    public void addRute() {
        try {
            ruteDAO.addRute(newRute);
            loadRutes();
            resetNewRute();
            facesContextHelper.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Rute added successfully!", null));
        } catch (Exception e) {
            e.printStackTrace();
            facesContextHelper.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding rute: " + e.getMessage(), null));
        }
    }

    // Method to edit an existing route
    public void edit(Rute rute) {
        this.selectedRute = rute;
        this.editFormVisible = true;
    }

    // Method to update route data
    public void update() {
        try {
            ruteDAO.updateRute(selectedRute);
            loadRutes();
            resetSelectedRute();
            facesContextHelper.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Rute updated successfully!", null));
        } catch (Exception e) {
            e.printStackTrace();
            facesContextHelper.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating rute: " + e.getMessage(), null));
        }
    }

    // Method to delete a route
    public void delete(Rute rute) {
        try {
            ruteDAO.deleteRute(rute);
            loadRutes();
            facesContextHelper.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Rute deleted successfully!", null));
        } catch (Exception e) {
            e.printStackTrace();
            facesContextHelper.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error deleting rute: " + e.getMessage(), null));
        }
    }

    // Reset form for adding a new route
    public void resetNewRute() {
        this.newRute = new Rute();
        this.addFormVisible = false;
    }

    // Reset the selected route after editing
    public void resetSelectedRute() {
        this.selectedRute = new Rute();
        this.editFormVisible = false;
    }

    // Fetch a Rute by its idRute (used by the Converter)
    public Rute getRuteById(String idRute) {
        for (Rute rute : ruteList) {
            if (rute.getIdRute().equals(idRute)) {
                return rute;
            }
        }
        return null;  // Return null if no matching Rute is found
    }
}
