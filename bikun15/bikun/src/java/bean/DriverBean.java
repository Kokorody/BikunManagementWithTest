package bean;

import DAO.DriverDAO;
import helper.FacesContextHelper; // Assuming you want to use a helper for messages
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import javax.inject.Inject;
import pojo.Driver;

@ManagedBean(name = "driverBean")
@ViewScoped
public class DriverBean implements Serializable {

    private final DriverDAO driverDAO; // Now injected via constructor
    private List<Driver> driverList;
    private List<Driver> availableDrivers;
    private Driver selectedDriver = new Driver();
    private Driver newDriver = new Driver();
    private boolean editFormVisible = false;
    private boolean addFormVisible = false;

    // Inject FacesContextHelper
    private FacesContextHelper facesContextHelper;

    // Regex pattern for No SIM validation (16 digits)
    private static final Pattern NO_SIM_PATTERN = Pattern.compile("^\\d{16}$");

    @Inject
    private SPJBean spjBean;

    // Constructor for dependency injection with both DriverDAO and FacesContextHelper
    public DriverBean(DriverDAO driverDAO, FacesContextHelper facesContextHelper) {
        this.driverDAO = driverDAO;
        this.facesContextHelper = facesContextHelper;
        loadDrivers();
        loadAvailableDriver(); // Load available drivers
    }

    // Default constructor used when no injection
    public DriverBean() {
        this(new DriverDAO(), new FacesContextHelper()); // Instantiate default DAO and Helper
    }

    public void loadDrivers() {
        driverList = driverDAO.getAllDrivers();
    }

    public void loadAvailableDriver() {
        availableDrivers = driverDAO.getAvailableDrivers(); // Get available drivers
    }

    public List<Driver> getAvailableDrivers() {
        return availableDrivers;
    }

    public Driver getDriverById(int idDriver) {
        for (Driver driver : driverList) {
            if (driver.getIdDriver() == idDriver) {
                return driver;
            }
        }
        return null;
    }

    public List<Driver> getDriverList() {
        return driverList;
    }

    public Driver getSelectedDriver() {
        return selectedDriver;
    }

    public Driver getNewDriver() {
        return newDriver;
    }

    public boolean isEditFormVisible() {
        return editFormVisible;
    }

    public boolean isAddFormVisible() {
        return addFormVisible;
    }

    public void setSpjBean(SPJBean spjBean) {
        this.spjBean = spjBean;
    }

    public void addDriver() {
        if (isNoSimValid(newDriver.getNoSim())) {
            try {
                driverDAO.addDriver(newDriver);
                loadDrivers();
                resetNewDriver();
                facesContextHelper.addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Driver berhasil ditambahkan!", null));
            } catch (Exception e) {
                e.printStackTrace();
                facesContextHelper.addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saat menambahkan driver: " + e.getMessage(), null));
            }
        } else {
            facesContextHelper.addMessage("noSim", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nomor SIM harus tepat 16 digit.", null));
        }
    }

    // Validation for No SIM field (exactly 16 digits)
    public boolean isNoSimValid(String noSim) {
        return noSim != null && NO_SIM_PATTERN.matcher(noSim).matches();
    }

    // Method to edit an existing driver
    public void edit(Driver driver) {
        this.selectedDriver = driver;
        this.editFormVisible = true;
    }

    // Method to update driver data
    public void update() {
        try {
            if (isFormValid()) {
                driverDAO.updateDriver(selectedDriver);
                loadDrivers();
                resetSelectedDriver();
                facesContextHelper.addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Driver berhasil diperbarui!", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            facesContextHelper.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error memperbarui driver: " + e.getMessage(), null));
        }
    }

    public void delete(Driver driver) {
        try {
            driverDAO.deleteDriver(driver);
            loadDrivers();
            facesContextHelper.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Driver berhasil dihapus!", null));
        } catch (Exception e) {
            e.printStackTrace();
            facesContextHelper.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kesalahan saat menghapus driver: " + e.getMessage(), null));
        }
    }

    // Reset form for adding a new driver
    public void resetNewDriver() {
        this.newDriver = new Driver();
        this.addFormVisible = false;
    }

    // Reset the selected driver after editing
    public void resetSelectedDriver() {
        this.selectedDriver = new Driver();
        this.editFormVisible = false;
    }

    public void noSimValidator(FacesContext context, UIComponent component, Object value) {
        String noSim = (String) value;
        if (!isNoSimValid(noSim)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Format Nomor SIM tidak valid. Harus tepat 16 digit.", null));
        }
    }

    // Additional form validation for the "Save" action
    private boolean isFormValid() {
        boolean valid = true;

        if (!isNoSimValid(selectedDriver.getNoSim())) {
            facesContextHelper.addMessage("noSim",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Format No SIM tidak valid", null));
            valid = false;
        }

        return valid;
    }
}
