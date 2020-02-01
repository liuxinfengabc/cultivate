package Simple_Factory_Pattern;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.*;

public class XMLUtilTV {

	public static String getBrandName() {
		try { 
			// 创建文档对象
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dFactory.newDocumentBuilder();
			Document doc;
			doc = builder.parse(new File("24Design/Simple_Factory_Pattern/configTV.xml"));
			//获取包含品牌名称的文本节点 
			NodeList nl = doc.getElementsByTagName("brandName");
			Node classNode = nl.item(0).getFirstChild();
			String brandName = classNode.getNodeValue().trim();
			return brandName;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}
}
