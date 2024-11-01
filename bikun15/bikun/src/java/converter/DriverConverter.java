//package converter;
//
//import javax.faces.component.UIComponent;
//import javax.faces.context.FacesContext;
//import javax.faces.convert.Converter;
//import javax.faces.convert.FacesConverter;
//import pojo.Driver;
//import bean.DriverBean;
//
//@FacesConverter("DriverConverter")
//public class DriverConverter implements Converter {
//
//    @Override
//    public Object getAsObject(FacesContext context, UIComponent component, String value) {
//        if (value == null || value.isEmpty()) {
//            return null;
//        }
//        DriverBean driverBean = context.getApplication().evaluateExpressionGet(context, "#{driverBean}", DriverBean.class);
//        return driverBean.getDriverById(Integer.parseInt(value));  // Fetch Driver by namaDriver
//    }
//
//    @Override
//    public String getAsString(FacesContext context, UIComponent component, Object value) {
//        if (value == null) {
//            return "";
//        }
//        if (value instanceof Driver) {
//            return String.valueOf(((Driver) value).getIdDriver()); // Return id_driver as String
//        } else {
//            throw new IllegalArgumentException("Object is not a valid Driver instance");
//        }
//    }
//}


package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import pojo.Driver;
import bean.DriverBean;

@FacesConverter("DriverConverter")
public class DriverConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
         if (value == null || value.isEmpty()) {
               return null;
           }

           DriverBean driverBean = context.getApplication().evaluateExpressionGet(context, "#{driverBean}", DriverBean.class);
 // debug untuk driver yang dipilih
      System.out.println("Nilai string di  DriverConverter : "+ value);





          Driver theDriver = driverBean.getDriverById(Integer.parseInt(value));



            System.out.println("Driver yang diambil dari getDriverById: "+ theDriver);





 return theDriver;
 }



 @Override

  public String getAsString(FacesContext context, UIComponent component, Object value) {

     if (value == null) {
      return null;


   }


     if (!(value instanceof Driver)) {


             throw new IllegalArgumentException("The object is not a valid Driver instance: " + value);




           }

 // pengecekan driver

           System.out.println("Driver yang dicek di getAsString Converter: "+ value);



       return String.valueOf(((Driver) value).getIdDriver());
  }
}