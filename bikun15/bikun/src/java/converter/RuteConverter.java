package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import pojo.Rute;
import bean.RuteBean;

@FacesConverter("RuteConverter")
public class RuteConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        RuteBean ruteBean = context.getApplication().evaluateExpressionGet(context, "#{ruteBean}", RuteBean.class);
        return ruteBean.getRuteById(value);  // Fetch Rute by idRute
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Rute) {
            return ((Rute) value).getIdRute();  // Return idRute as String
        } else {
            throw new IllegalArgumentException("Object is not a valid Rute instance");
        }
    }
}
