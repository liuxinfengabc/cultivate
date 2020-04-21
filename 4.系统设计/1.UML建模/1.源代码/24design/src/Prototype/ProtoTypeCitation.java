package Prototype;

public class ProtoTypeCitation
{
    public static void main(String[] args) throws CloneNotSupportedException
    {
        citation obj1=new citation("张三","同学：在2017学年第一学期表现优秀，被评为优秀学生。","山东建筑大学计算机科学与技术学院");
        obj1.display();
        citation obj2=(citation) obj1.clone();
        obj2.setName("李四"); 
        obj2.display();
    }
}