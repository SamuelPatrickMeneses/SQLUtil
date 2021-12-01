package core;




import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OutputTableObject<T> implements OutputTable<T>{
    
    private Connection c;
    private Map<String, AbstractField> fieldsMap;
    private Map<String, Method> methods;
    private Class<T> _class;
    private String[] column;
    private String[] incrementedColumn;
    private PreparedStatement insertInto;
    public OutputTableObject(Connection c, Class<T> _class) throws TableNotExistsException, IncompatibleTableInterfaceException {
        this.c = c;
        this._class = _class;
        fieldsMap = new HashMap<>();
        isTable();
        methods = getGetters(new HashMap<>());
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
                                                               rezultados.getObject("Default"),
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
    
    private String stringInsertInto(){
        String out = "insert into "+_class.getAnnotation(Table.class).value()+" (";
        List<String> columns = new ArrayList(Arrays.asList(column));
        for(int i = 0;i < column.length; i++)
            if(fieldsMap.get(column[i]).getExtra().toLowerCase().equals("auto_increment"))
                columns.remove(i);
        String fields = "";
        int size = columns.size();
        for(int i = 0; i < size;i++){
            fields = fields.concat(columns.get(i));
            if(i < size -1)
                fields = fields.concat(", ");
            else
                fields = fields.concat(") ");
        }
        fields = fields.concat("values (");
        for(int i = 0;i < size ; i++){
            if(i < size -1)
                fields = fields.concat("?,");
            else
                fields = fields.concat("?)");
        }
        incrementedColumn = columns.toArray(new String[columns.size()]);
        return out.concat(fields);
    }
    public void loadInsertInto(String preSearch) throws IncompatibleTableInterfaceException{
         try {
            insertInto = c.prepareStatement(preSearch);
        } catch (SQLException ex) {
            throw new IncompatibleTableInterfaceException("Statement error.");
        }
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
    private Integer parseStringToTypeConstants(String in){
        String out = in;
        out = out.split("[(]")[0];
        out = out.toUpperCase();
        out = out.replace(" ", "_");
        Field[] fs = Types.class.getDeclaredFields();
        for(Field f: fs)
            if(f.getName().equals(out)){
                try {
                    f.setAccessible(true);
                    return   f.getInt(null);
                } catch (IllegalArgumentException ex) {
                } catch (IllegalAccessException ex) {
                }
            }
        return null;        
    }

    @Override
    public void close() throws Exception {
       if(insertInto != null)
            insertInto.close();
       c.close();
    }

    @Override
    public <O extends T> void insertInto(O o) throws RuntimeException, IncompatibleTableInterfaceException  {
        if(insertInto == null)
            loadInsertInto(stringInsertInto());
        Object v = null;
        int type[] = new int[incrementedColumn.length];
        for(int i = 0; i < type.length; i++){
            String columnType = fieldsMap.get(incrementedColumn[i]).getType();
            type[i] = parseStringToTypeConstants(columnType);
        }
        try {
            for(int i = 0; i < incrementedColumn.length;i++){
                String column = incrementedColumn [i];
                v = methods.get(column).invoke(o);
                if(v == null)
                    v = fieldsMap.get(column).getDefault();

                insertInto.setObject(i+1, v,type[i]);
            }
            insertInto.execute();
       } catch (IllegalAccessException ex) {

       } catch (IllegalArgumentException ex) {
                
       } catch (InvocationTargetException ex) {
           
       } catch (SQLException ex) {
           
       }     
    }

    @Override
    public <O extends T> void update(O o, int index) throws RuntimeException {
        
    }

    

    
}
