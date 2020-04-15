package factory;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
public class ReadXML1
{
    //该方法用于从XML配置文件中提取具体类类名，并返回一个实例对象
    public static Object getObject(String type)
    {
        try
        {
            //创建文档对象
            DocumentBuilderFactory dFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=dFactory.newDocumentBuilder();
            Document doc;                           
            doc=builder.parse(new File("src/factory/config1.xml"));        
            //获取包含类名的文本节点
            NodeList nl=doc.getElementsByTagName("className");
         //   Node classNode=nl.item(0).getFirstChild();
           // String cName="factory."+classNode.getNodeValue();
           // System.out.println("新类名："+cName);
            //通过类名生成实例对象并将其返回
            for(int i = 0;i < nl.getLength();i++) {
				String cName = nl.item(i).getFirstChild().getNodeValue();
				if(cName.equals(type)) {
					Class c = Class.forName("factory."+cName);
					Object obj = c.newInstance();		
					return obj;
				}
			}
            return null;
//            Class c=Class.forName(cName);
//              Object obj=c.newInstance();
//            return obj;
         }  
         catch(Exception e)
         {
                   e.printStackTrace();
                   return null;
         }
    }
}
