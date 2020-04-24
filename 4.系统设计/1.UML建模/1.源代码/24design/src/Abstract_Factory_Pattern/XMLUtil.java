package Abstract_Factory_Pattern;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLUtil {

	public static Object getBean(String type) {
		try {
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dFactory.newDocumentBuilder();
			Document doc;
			doc = builder.parse(new File("src/Abstract_Factory_Pattern/config.xml"));
			
			NodeList nl = doc.getElementsByTagName("className");
			//System.out.println(nl);
			for(int i = 0;i < nl.getLength();i++) {
				String cName = nl.item(i).getFirstChild().getNodeValue();
				if(cName.equals(type)) {
					Class c = Class.forName("Abstract_Factory_Pattern."+cName);
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
