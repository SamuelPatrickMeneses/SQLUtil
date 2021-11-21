
package core;


public interface OutputTable<T> extends AutoCloseable{

    public void insertInto(Object o)throws RuntimeException;
    public void update(Object o,int index)throws RuntimeException;
}
