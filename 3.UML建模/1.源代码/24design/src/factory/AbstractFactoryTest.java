package factory;

import java.util.Scanner;

public class AbstractFactoryTest
{
    public static void main(String[] args)
    {
        try
        {
            Product a;
            AbstractFactory af;
            System.out.println("请输入工厂名称");
            Scanner sc = new Scanner(System.in);
            af=(AbstractFactory) ReadXML1.getObject(sc.nextLine());
            a=af.newProduct();
            a.show();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
