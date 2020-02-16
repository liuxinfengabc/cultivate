package PrototypePattern;

public class CloneTest implements Cloneable{
    private byte[] a = {1, 2, 3, 4};
    private byte[] b = {5, 6, 7 ,8};
    public CloneTest clone(){
        CloneTest copy = null;
        try{
            copy = (CloneTest)super.clone();
           copy.a = this.a.clone();

        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return copy;
    }
    public byte[] getA(){
        return this.a;
    }
    public byte[] getB(){
        return this.b;
    }
    public static void main(String[] args){
        CloneTest original = new CloneTest();
        CloneTest cloned = original.clone();
//        original.getA();
        System.out.println(original);
        System.out.println(cloned);
        System.out.println(original.getA());
        System.out.println(cloned.getA());

        System.out.println("original.getA() == cloned.getA():" + (original.getA() == cloned.getA()));
        System.out.println("original.getB() == cloned.getB():" + (original.getB() == cloned.getB()));
    }
}
