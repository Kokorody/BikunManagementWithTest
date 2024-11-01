package bean;

import DAO.SPJDAO;
import helper.FacesContextHelper; // Import the helper
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject; // Import for dependency injection
import java.io.Serializable;
import java.util.List;
import pojo.Fleet;

@ManagedBean(name = "spjBean")
@ViewScoped
public class SPJBean implements Serializable {

    private final SPJDAO spjDAO;
    private final FacesContextHelper facesContextHelper; // Helper for managing messages
    private List<Fleet> fleetList;
    private Fleet selectedFleet = new Fleet();
    private Fleet newFleet = new Fleet();

    // Constructor for dependency injection with SPJDAO and FacesContextHelper
    @Inject
    public SPJBean(SPJDAO spjDAO, FacesContextHelper facesContextHelper) {
        this.spjDAO = spjDAO;
        this.facesContextHelper = facesContextHelper;
        loadFleet();
    }

    // Default constructor used when no injection
    public SPJBean() {
        this(new SPJDAO(), new FacesContextHelper()); // Instantiate default DAO and Helper
    }

    public void loadFleet() {
        fleetList = this.spjDAO.getAllFleets();
        System.out.println("fleetList di loadFleet(): " + fleetList); // debug fleetList
    }

    public List<Fleet> getFleetList() {
        return fleetList;
    }

    public Fleet getSelectedFleet() {
        return selectedFleet;
    }

    public Fleet getNewFleet() {
        return newFleet;
    }

    public void addSPJ() {
        System.out.println("Bus yang dipilih: " + newFleet.getBus());
        System.out.println("Driver yang dipilih: " + newFleet.getDriver());
        System.out.println("Rute yang dipilih: " + newFleet.getRute());

        if (newFleet.getBus() == null || newFleet.getDriver() == null || newFleet.getRute() == null) {
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Harap pilih Bus, Driver, dan Rute!", null));
            return;
        }
        try {
            spjDAO.addSPJ(newFleet);
            spjDAO.updateBusStatus(newFleet.getBus().getIdBus(), "deploy");
            loadFleet(); // perbarui fleetlist
            resetNewFleet();
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "SPJ berhasil ditambahkan!", null));
        } catch (Exception e) {
            e.printStackTrace();
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error saat menambahkan SPJ: " + e.getMessage(), null));
        }
    }

    public void update() {
        try {
            spjDAO.updateSPJ(selectedFleet);
            loadFleet(); // memanggil loadFleet untuk memperbarui daftar fleet
            resetSelectedFleet();
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "SPJ berhasil diperbarui!", null));
        } catch (Exception e) {
            e.printStackTrace();
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Kesalahan saat memperbarui SPJ: " + e.getMessage(), null));
        }
    }

    public void delete(Fleet fleet) {
        try {
            spjDAO.deleteSPJ(fleet);
            spjDAO.updateBusStatus(fleet.getBus().getIdBus(), "depot");
            loadFleet();
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "SPJ berhasil dihapus!", null));
        } catch (Exception e) {
            e.printStackTrace();
            facesContextHelper.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Kesalahan saat menghapus SPJ: " + e.getMessage(), null));
        }
    }

    public void resetNewFleet() {
        newFleet = new Fleet();
    }

    public void resetSelectedFleet() {
        selectedFleet = new Fleet();
    }
}
