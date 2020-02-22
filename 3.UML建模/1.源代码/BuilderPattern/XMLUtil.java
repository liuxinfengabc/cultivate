package BuilderPattern;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLUtil {

	// 该方法用于从XML配置文件中提取具体类类名，并返回一个实例对象
	public static Object getBean(String type) {
		try {
			// 创建文档对象
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dFactory.newDocumentBuilder();
			Document doc;
			doc = builder.parse(new File("24Design/BuilderPattern/config.xml"));

			// 获取包含类名的文本节点
			NodeList nl = doc.getElementsByTagName("className");
			for (int i = 0; i < nl.getLength(); i++) {
				String cName = nl.item(i).getFirstChild().getNodeValue();
				if (cName.equals(type)) {
					Class c = Class.forName("BuilderPattern." + cName);
					Object obj = c.newInstance();
					return obj;
				}
			}	
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}
}
