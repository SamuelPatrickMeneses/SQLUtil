


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
import Tables.Product;
import core.ConectionFactory;
import core.IncompatibleTableInterfaceException;
import core.OutputTableObject;
import core.TableNotExistsException;


public class TestSQL {

    public TestSQL() {
        System.out.println("hola mundo!");
    }

    
    public static void main(String[] args)  {
        Connection  c = ConectionFactory.getIstance("jdbc:mysql://localhost/açai_stor", "root", "MySqlKey");
        
       try(
            OutputTableObject<Product> out = new OutputTableObject<>(
                     c,Product.class);
        ){
            Product p = new Product(1,5.5f,"","");
            out.insertInto(p);
       } catch (TableNotExistsException ex) {
            Logger.getLogger(TestSQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IncompatibleTableInterfaceException ex) {
            Logger.getLogger(TestSQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
