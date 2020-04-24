package Composite;

import java.util.ArrayList;
public class CompositePattern
{
    public static void main(String[] args)
    {
        Component c0=new Composite(); 
        Component c1=new Composite(); 
        Component leaf1=new Leaf("1"); 
        Component leaf2=new Leaf("2"); 
        Component leaf3=new Leaf("3");          
        c0.add(leaf1); 
        c0.add(c1);
        c1.add(leaf2); 
        c1.add(leaf3);          
        c0.operation(); 
    }
}
