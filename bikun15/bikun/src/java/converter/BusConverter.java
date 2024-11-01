package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import pojo.Bus;
import bean.BusBean;

@FacesConverter("BusConverter")
public class BusConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        BusBean busBean = context.getApplication().evaluateExpressionGet(context, "#{busBean}", BusBean.class);

        for (Bus bus : busBean.getAvailableBuses()) { // <-- PERUBAHAN DI SINI
            if (String.valueOf(bus.getIdBus()).equals(value)) {
                return bus;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Bus) {
            return String.valueOf(((Bus) value).getIdBus());  // Return idBus as String
        } else {
            throw new IllegalArgumentException("Object is not a valid Bus instance");
        }
    }
}
