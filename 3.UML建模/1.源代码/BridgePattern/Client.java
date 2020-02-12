package BridgePattern;



import java.util.Scanner;

public class Client {
    public static void main(String a[]) {
        Color color;
        Pen pen;

        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入您需要用多大的笔：");
            String type = sc.nextLine();
            System.out.println("请输入您需要用哪种颜色：");
            String type1 = sc.nextLine();
            color = (Color) XMLUtilPen.getBean(type1);//color = new Red();
            pen = (Pen) XMLUtilPen.getBean(type);//pen = new BigPen();
            pen.setColor(color);
            System.out.println("请输入您要绘制的东西：");
            pen.draw(sc.nextLine());
        }catch(Exception e) {
            System.out.println("找不到您所输入的类型！");
        }
//        color = (Color) XMLUtilPen.getBean("color");
//        pen = (Pen) XMLUtilPen.getBean("pen");
//        pen.setColor(color);
//        pen.draw("鲜花");
    }
}