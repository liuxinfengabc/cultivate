package iterator;

public interface Aggregate
{ 
    public void add(Object obj); 
    public void remove(Object obj); 
    public Iterator getIterator(); 
}
