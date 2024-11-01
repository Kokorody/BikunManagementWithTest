import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import org.mockito.Mockito;
import javax.faces.context.ExternalContext;
import javax.faces.context.Flash;
import javax.faces.context.PartialViewContext;

public abstract class FacesContextMocker extends FacesContext {
    private static final FacesContext facesContext = Mockito.mock(FacesContext.class);
    
    public static FacesContext mockFacesContext() {
        setCurrentInstance(facesContext);
        
        ExternalContext externalContext = Mockito.mock(ExternalContext.class);
        Flash flash = Mockito.mock(Flash.class);
        PartialViewContext partialViewContext = Mockito.mock(PartialViewContext.class);
        
        Mockito.when(facesContext.getExternalContext()).thenReturn(externalContext);
        Mockito.when(facesContext.getExternalContext().getFlash()).thenReturn(flash);
        Mockito.when(facesContext.getPartialViewContext()).thenReturn(partialViewContext);
        
        return facesContext;
    }

    public static void cleanUp() {
        setCurrentInstance(null);
    }
}
