<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>

<%@ page import = "org.mitre.mrald.util.Config"%>
<%@ page import = "org.mitre.lattice.util.FileUtils"%>
<%@ page import = "java.io.File"%>
<%@ page import = "org.mitre.lattice.graph.GraphSelect"%>
<mrald:validate />
<%
                
        String action = request.getParameter( "action" );

	if (!action.equals("Reset"))
	{
	        response.sendRedirect( "Sample.html" );

	}
        String latticeDir = Config.getProperty("BasePath") + "/WEB-INF/" + Config.getProperty("latticeDir");
        String oldfileName = latticeDir + "lattice_demo_start.srv";
        String newfileName = latticeDir + "lattice.srv";
	
	FileUtils fileUtils = new FileUtils();
        File new_file = new File( newfileName);
        File demofile = new File( oldfileName);
	
	File lastGraph = new File(Config.getProperty("BasePath") + "/" +GraphSelect.getLastGraph() + ".jpg");
	File demoGraph = new File(Config.getProperty("BasePath") + "/" + Config.getProperty("latticeGraph") + "_demo.jpg");
	//File demoGraph = new File(Config.getProperty("BasePath") + "/" + Config.getProperty("latticeGraph") + ".jpg");
	
	fileUtils.copyFile(demofile, new_file);
	fileUtils.copyFile(demoGraph, lastGraph);
	
        response.sendRedirect( "FormSubmit?workflow=Redraw%20Graph" );
    
%>
