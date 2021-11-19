
package testsql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import testsql.Tables.Product;


public class TestSQL {

    public TestSQL() {
        System.out.println("hola mundo!");
    }

    
    public static void main(String[] args)  {
       /* Connection  c = ConectionFactory.getIstance();
        
       try(
            OutputTableObject<Product> out = new OutputTableObject<>(
                    c,Product.class);
        ){
            Product p = new Product(1,5.5f,"","");
            Map<String, Method> ms = new HashMap<>();
            for(Method m :out.getClass().getMethods())
                if(m.getName().equals("getGetters")){
                   ms = (Map<String, Method>) m.invoke(out, ms);
                }
            for(String s:ms.keySet())
                System.out.println(ms.get(s).getName());
            System.out.println("invoco");
        } catch (TableNotExistsException ex) {
            throw new RuntimeException(ex);
        } catch (IncompatibleTableInterfaceException ex) {
             throw new RuntimeException(ex);   
        } catch (SQLException ex) {
              ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }catch(NullPointerException ex){
           
        }catch(Exception ex){
            ex.printStackTrace();
        }*/
        
        /*Conect c = new Conect();
        c.show("filme");
        c.close();*/
    }
}
