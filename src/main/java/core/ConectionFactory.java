package core;




import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ConectionFactory {
    private static Connection c;
    private ConectionFactory(String db, String u, String p) {
        
        try {
            ConectionFactory.c = DriverManager.getConnection(db, u, p);
        } catch (SQLException ex) {
            
        }
        
    
    }

    private ConectionFactory() {
        this("jdbc:mysql://localhost/açai_stor", "root", "MySqlKey");
    }
    
    public static Connection getIstance(){
        if(ConectionFactory.c == null)
            return (new ConectionFactory()).c;
        else
            return ConectionFactory.c;
    }
    public static Connection getIstance(String db, String u, String p){
        if(ConectionFactory.c == null)
            return (new ConectionFactory(db,u,p)).c;
        else
            return ConectionFactory.c;
    }
    public void close(){
        try {
            c.close();
            c = null;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private PreparedStatement getSearch(String q) {
        PreparedStatement p = null;
        try{
           p = c.prepareStatement(q); 
        }catch(SQLException ex){
            throw new RuntimeException("PreparedSatement Error.");
        }
        return p;
    }
    
}
