
package testsql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OutputTableObject<T> implements OutputTable{
    
    private Connection c;
    private Map<String, AbstractField> fieldsMap;
    private Map<String, Method> methods;
    private Class<T> _class;
    private String[] column;
    private String[] incrementedColumn;
    private String preSearch;
    public OutputTableObject(Connection c, Class<T> _class) throws TableNotExistsException, IncompatibleTableInterfaceException {
        this.c = c;
        this._class = _class;
        fieldsMap = new HashMap<>();
        isTable();
        methods = getGetters(new HashMap<>());;
        preSearch = StringPrefactory();
        
    }
    private void isTable()throws TableNotExistsException{ 
        Table an = _class.getAnnotation(Table.class);
        if(an != null){
            String table = an.value();
            List<String> l = new ArrayList<>();
            try (
                PreparedStatement desc = c.prepareStatement("describe "+table);
                ResultSet rezultados = desc.executeQuery();
            ){
                while(rezultados.next()){
                    String f = rezultados.getString("Field");
                    AbstractField field = new FieldDescription(f,
                                                               rezultados.getString("Type"),
                                                               rezultados.getString("Null"),
                                                               rezultados.getString("Key"),
                                                               rezultados.getString("Default"),
                                                               rezultados.getString("Extra"));
                    fieldsMap.put(f,field);
                    l.add(f);
                }
                column =  l.toArray(new String[l.size()]);
            } catch (SQLException ex) {
                throw new TableNotExistsException("This object does not represent a table.");
            }
        }

    }
    
    private String StringPrefactory(){
        String out = "insert into "+_class.getAnnotation(Table.class).value()+" (";
        List<String> columns = Arrays.asList(column);
        for(String col :columns)
            if(fieldsMap.get(col).getExtra().toLowerCase().equals("auto_incements"))
                columns.remove(col);
        String fields = "";
        int size = columns.size();
        for(int i = 0; i < size;i++){
            fields = fields.concat(columns.get(i));
            if(i < size -1)
                fields = fields.concat(", ");
            else
                fields = fields.concat(") ");
        }
        incrementedColumn = columns.toArray(new String[columns.size()]);
        return out.concat(fields);
    }

    @Override
    public void insertInto(Object o) throws RuntimeException {
        
        
    }

    @Override
    public void update(Object o, int index) throws RuntimeException {
      
    }
    
    private Map<String,Method> getGetters(Map<String,Method> map) throws IncompatibleTableInterfaceException{
        for(Method m: _class.getMethods())
            if(m.isAnnotationPresent(ColumnGetter.class)){
                int modifier = m.getModifiers();
                if((!Modifier.isAbstract(modifier) && !Modifier.isStatic(modifier)) && 
                (Modifier.isPublic(modifier) && !m.getReturnType().equals(Void.class)))
                        if(m.getParameterCount() == 0)
                            label1:for(String col: column)
                                if(m.getAnnotation(ColumnGetter.class).value().equals(col)){
                                    map.put(col, m);
                                    break label1;  
                                }    
            }             
        Set<String> set = map.keySet();
        for(String s :column)
            if(!set.contains(s))
                throw new IncompatibleTableInterfaceException("incompatible table interface.");
        return map;
    }

    @Override
    public void close() throws Exception {
       c.close();
    }
}
