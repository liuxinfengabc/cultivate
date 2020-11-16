package com.lz.ht.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;


import freemarker.template.Template;
import freemarker.template.TemplateException;

public class StaticHtmlUtil {

	 public static String createHtmlByTemplate(String templateName ,String moduleName ,Map<String,Object> templateVars,String javaOutDir) throws IOException, TemplateException {
		 
		    String createPageFileName = ""; 
		    String uuidfileName = ToolKit.uuidString(); 
		    
		    createPageFileName = moduleName+ "_"+  uuidfileName +".html";
	        System.out.println(createPageFileName);
	        Template template = FreeMarkerTemplateUtil.getTemplate(templateName);
	        String dir = createDir(javaOutDir + "/" + moduleName );
	        FileOutputStream fileOutputStream = new FileOutputStream(new File(dir + "/" + createPageFileName));
	        Writer out = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "utf-8"),10240);
	        template.process(templateVars,out);
			return createPageFileName;
	    }
	
	 
	    private static String createDir(String dirStr){
	        File dir  = new File(dirStr);
	        if(!dir.exists()){
	            dir.mkdirs();
	        }
	        return dir.getAbsolutePath();
	    }

}
