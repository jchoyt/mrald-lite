<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>

<%@ page import = "org.mitre.mrald.util.User"%>
<%@ page import = "org.mitre.mrald.util.Config"%>
<%@ page import = "org.mitre.mrald.util.MraldOutFile"%>
<%@ page import = "org.mitre.mrald.util.XSLTranslator"%>
<%@ page import = "java.io.File"%>
<%@ page import = "java.util.Enumeration"%>
<%@ page import = "java.util.Properties"%>
<%@ page import = "javax.servlet.http.HttpServletRequest"%>
<%@ page import = "org.apache.commons.fileupload.*"%>
<%@ page import = "org.mitre.mrald.util.*"%>
<%@ page import = "java.util.*"%>
  
<mrald:validate />  
 
  <html>
  <head>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Upload Form
    </title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
<%
		MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "FileUtils. Start here");
        
		if ( !FileUpload.isMultipartContent(request) )
		{
			throw new MraldException("The supplied request did not come from a multipart request - " +
				"there can't be any file buried in here.");
		}
		/*
		 *  parse the request
		 */
		  System.out.println("FileUtils.jsp : start");
           
       	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "FileUtils. Start");
           
		DiskFileUpload upload = new DiskFileUpload();
		List items = upload.parseRequest(request);
		Iterator iter = items.iterator();
		String fileName ="";
										
		while (iter.hasNext())
		{
				
			FileItem fileSetItem= (FileItem) iter.next();
			String itemName = fileSetItem.getFieldName();
			MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "FileUtils. ItemName" + itemName );
        		
			
			if (itemName.startsWith("fileName"))
			{
				fileName = fileSetItem.getString();
				
			}
		}
		
		iter = items.iterator();
		while (iter.hasNext())
		{
			FileItem fileSetItem= (FileItem) iter.next();
			String itemName = fileSetItem.getFieldName();
			
            if (itemName.startsWith("settings"))
			{
            
            	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "FileUtils. FileName " + fileName  );
        		String dirFileName = Config.getProperty("BasePath");
			
            	File fileFinalLocation = new File(dirFileName + "/" + fileName);
        		fileSetItem.write(fileFinalLocation);
				MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "FileUtils. File created "  );
        
			}
		}
	
%>		
</body>

	
%>