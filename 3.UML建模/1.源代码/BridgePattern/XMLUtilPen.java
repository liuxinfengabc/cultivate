package BridgePattern;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import java.io.*;

public class XMLUtilPen {
    //该方法用于从XML配置文件中提取具体类类名，并返回一个实例对象
    public static Object getBean(String args) {
        try {
            //创建文档对象
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();//得到创建 DOM 解析器的工厂.

            DocumentBuilder builder = dFactory.newDocumentBuilder();//得到 DOM 解析器对象。
            Document doc;
            doc = builder.parse(new File("24Design/BridgePattern/BridgeconfigPen.xml"));//解析 XML 文档
            NodeList nl = null;
            //Node classNode = null;
            String cName = null;
            nl = doc.getElementsByTagName("className");

//            if (args.equals("color")) {
//                //获取包装类名的文本节点
//                classNode = nl.item(0).getFirstChild();
//
//            } else if (args.equals("pen")) {
//                //获取包含类名的文本节点
//                classNode = nl.item(1).getFirstChild();
//            }
            for(int i = 0;i < nl.getLength();i++) {
                cName = nl.item(i).getFirstChild().getNodeValue();
                //item() 方法可返回节点列表中处于指定索引号的节点。getFirstChild()取得本节点下的第一个节点
                if(cName.equals(args)) {
                    Class c = Class.forName("BridgePattern."+cName);//实例化 = new Red();
                    Object obj = c.newInstance();//实例化为对象
                    return obj;
                }
            }

//            cName = classNode.getNodeValue();
//            //ͨ通过类名生成实例对象并将其返回
//            Class c = Class.forName(cName);
//            Object obj = c.newInstance();
//            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
