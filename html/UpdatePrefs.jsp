<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>

<%@ page import = "java.util.Iterator"%>
<%@ page import = "java.util.Properties"%>
<%@ page import = "org.mitre.mrald.util.User"%>
<%@ page import = "org.mitre.mrald.util.Config"%>
<%@ page import = "org.mitre.mrald.util.MraldOutFile"%>
<%@ page import = "org.mitre.mrald.util.WebUtils"%>
<%@ page import = "org.mitre.mrald.util.MiscUtils"%>
<%@ page import = " org.mitre.lattice.custom.LatticeFactory"%>
<%@ page import = " org.mitre.lattice.util.Constants"%>
<%@ page import = "java.io.File"%>
<%@ page import = "java.io.FileOutputStream"%>
<%@ page import = "java.io.PrintStream"%>
<mrald:validate />
<%
       LatticeFactory latticeFactory = Config.getLatticeFactory();
   
	Properties latticeProps = latticeFactory.getProperties();

	Iterator keys = (Iterator)latticeProps.propertyNames();
	StringBuffer contents = new StringBuffer();
	while (keys.hasNext())
	{
	    String key = keys.next().toString();
	    String result = WebUtils.getOptionalParameter(pageContext.getRequest(),  key );
	    String temp =null;
	    //if no changes made then just go with what already was there
	    if (result.equals(""))
	    {
	            temp = key + "=" + latticeProps.getProperty( key ) + "\n";
                    contents.append( temp );
	    }
	    else
	    {
	    	    temp = key + "=" + result + "\n";
                    contents.append( temp );

	    }
	    //Set the property
        }	  
	
	 File config_file = new File( Config.getProperty("latticeProps") );

         FileOutputStream fout = new FileOutputStream( config_file );
         PrintStream pout = new PrintStream( fout );

         pout.println( contents.toString() );

         pout.close();
         fout.close();

	 //Trigger the reload.
	 Constants.loadProperties();
	 Config.fireChangeEvent();
	 
	 response.sendRedirect( "LatticeManagement.jsp" );

%>
