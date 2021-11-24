
package core;


public interface OutputTable< T> extends AutoCloseable{

    public <O extends T> void insertInto(O o)throws RuntimeException;
    public <O extends T> void update(O o,int index)throws RuntimeException;
}
