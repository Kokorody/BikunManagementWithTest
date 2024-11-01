package helper;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesContextHelper {

    // Method to get the current FacesContext instance
    public FacesContext getCurrentInstance() {
        return FacesContext.getCurrentInstance();
    }

    // Method to add messages to FacesContext
    public void addMessage(String componentId, FacesMessage message) {
        getCurrentInstance().addMessage(componentId, message);
    }
}
