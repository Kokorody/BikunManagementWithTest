package bean;

import DAO.BusDAO;
import helper.FacesContextHelper;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;
import pojo.Bus;

@ManagedBean(name = "busBean")
@ViewScoped
public class BusBean implements Serializable {

    private final BusDAO busDAO; // Now injected via constructor
    private List<Bus> busList;
    private List<Bus> availableBuses; // List for available buses
    private Bus selectedBus = new Bus();
    private Bus newBus = new Bus();
    private boolean editFormVisible = false;
    private boolean addFormVisible = false;

    // Inject FacesContextHelper (Constructor Injection or Setter Injection can be used)
    private FacesContextHelper facesContextHelper;

    // Regex patterns for validation
    private static final Pattern NO_BODY_PATTERN = Pattern.compile("^[A-Z]{3}-[0-9]{4}$"); 
    private static final Pattern PLAT_NOMOR_PATTERN = Pattern.compile("^[A-Z]{1,2} [0-9]{1,4} [A-Z]{1,2}$");

    // Constructor for dependency injection with both BusDAO and FacesContextHelper
    public BusBean(BusDAO busDAO, FacesContextHelper facesContextHelper) {
        this.busDAO = busDAO;
        this.facesContextHelper = facesContextHelper;
        loadBuses();
        loadAvailableBuses(); // Load available buses
    }

    // Constructor for injecting only BusDAO (use default FacesContextHelper)
    public BusBean(BusDAO busDAO) {
        this(busDAO, new FacesContextHelper()); // Call the main constructor with default FacesContextHelper
    }

    // Default constructor used when no injection (can still be instantiated with default DAO and Helper)
    public BusBean() {
        this(new BusDAO(), new FacesContextHelper()); // Instantiate default DAO and Helper
    }

    // Setter to inject FacesContextHelper (Useful for testing)
    public void setFacesContextHelper(FacesContextHelper facesContextHelper) {
        this.facesContextHelper = facesContextHelper;
    }

    // Method to load all buses from the DAO
    public void loadBuses() {
        busList = busDAO.getAllBuses();
    }


    // Method to load available buses from the DAO
    public void loadAvailableBuses() {
        availableBuses = busDAO.getAvailableBuses(); // Get available buses
    }

    // Getter methods
    public List<Bus> getBusList() {
        return busList;
    }

    public List<Bus> getAvailableBuses() {
        return availableBuses;
    }

    public Bus getSelectedBus() {
        return selectedBus;
    }

    public Bus getNewBus() {
        return newBus;
    }

    public boolean isEditFormVisible() {
        return editFormVisible;
    }

    public boolean isAddFormVisible() {
        return addFormVisible;
    }

    // Method to add a new bus
    public void addBus() {
        try {
            // Perform validation checks before adding
            if (isFormValid()) {
                busDAO.addBus(newBus);
                loadBuses();
                loadAvailableBuses(); // Reload available buses
                resetNewBus();
                facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bus added successfully!", null));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding bus: " + e.getMessage(), null));
        }
    }

    // Method to edit an existing bus
    public void edit(Bus bus) {
        this.selectedBus = bus;
        this.editFormVisible = true;
    }

    // Method to update bus data
    public void update() {
        try {
            busDAO.updateBus(selectedBus);
            loadBuses();
            loadAvailableBuses(); // Reload available buses
            resetSelectedBus();
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bus updated successfully!", null));
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating bus: " + e.getMessage(), null));
        }
    }

    // Method to delete a bus
    public void delete(Bus bus) {
        try {
            busDAO.deleteBus(bus);
            loadBuses();
            loadAvailableBuses(); // Reload available buses
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bus deleted successfully!", null));
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error deleting bus: " + e.getMessage(), null));
        }
    }

    // Reset form for adding a new bus
    public void resetNewBus() {
        this.newBus = new Bus();
        this.addFormVisible = false;
    }

    // Reset the selected bus after editing
    public void resetSelectedBus() {
        this.selectedBus = new Bus();
        this.editFormVisible = false;
    }

    // Validation for noBody field
    public void noBodyValidator(FacesContext context, UIComponent component, Object value) {
        String noBody = (String) value;
        if (noBody == null || !NO_BODY_PATTERN.matcher(noBody).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Invalid No Body format. Use: XXX-0000", null));
        }
    }

    // Validation for platNomor field
    public void platNomorValidator(FacesContext context, UIComponent component, Object value) {
        String platNomor = (String) value;
        if (platNomor == null || !PLAT_NOMOR_PATTERN.matcher(platNomor).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Invalid Plat Nomor format. Use: A-Z 0-9 A-Z", null));
        }
    }

    // Validation for tipeBus field
    public void tipeBusValidator(FacesContext context, UIComponent component, Object value) {
        String tipeBus = (String) value;
        if (tipeBus == null || tipeBus.length() < 3) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Tipe Bus must be at least 3 characters", null));
        }
    }

    // Additional form validation for the "Save" action
    private boolean isFormValid() {
        boolean valid = true;

        if (!isNoBodyValid()) {
            facesContextHelper.addMessage("noBody", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid No Body format", null));
            valid = false;
        }
        if (!isPlatNomorValid()) {
            facesContextHelper.addMessage("platNomor", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Plat Nomor format", null));
            valid = false;
        }
        if (!isTipeBusValid()) {
            facesContextHelper.addMessage("tipeBus", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipe Bus must be at least 3 characters", null));
            valid = false;
        }
        if (!isStatusValid()) {
            facesContextHelper.addMessage("status", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a valid status", null));
            valid = false;
        }

        return valid;
    }

    // Check if noBody is valid
    public boolean isNoBodyValid() {
        String noBody = newBus.getNoBody();
        return noBody != null && NO_BODY_PATTERN.matcher(noBody).matches();
    }

    // Check if platNomor is valid
    public boolean isPlatNomorValid() {
        String platNomor = newBus.getPlatNomor();
        return platNomor != null && PLAT_NOMOR_PATTERN.matcher(platNomor).matches();
    }

    // Check if tipeBus is valid
    public boolean isTipeBusValid() {
        String tipeBus = newBus.getTipeBus();
        return tipeBus != null && tipeBus.length() >= 3;
    }

    // Check if the status field is valid
    public boolean isStatusValid() {
        String status = newBus.getStatus();
        return status != null && (status.equals("deploy") || status.equals("depot") || status.equals("maintenance"));
    }

    // Fetch a Bus by its idBus (used by the Converter)
    public Bus getBusById(int idBus) {
        for (Bus bus : busList) {
            if (bus.getIdBus().equals(idBus)) { // Use equals instead of ==
                return bus;
            }
        }
        return null;  // Return null if no matching Bus is found
    }
}
