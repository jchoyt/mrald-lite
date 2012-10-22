<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>

<%@ page import = "java.util.*"%>
<%@ page import = "org.apache.commons.fileupload.*"%>
<%@ page import = "org.mitre.mrald.util.*"%>
<%@ page import = "java.io.File"%>
<mrald:validate />
<%
    FormUtils fu = new FormUtils();
    String formId = request.getParameter( "formid" );

    String action = request.getParameter( "action" );
    User mraldUser = (User)session.getValue(Config.getProperty( "cookietag" ));
    String userId = mraldUser.getEmail();
    String formAccess = request.getParameter( "formAccess" );
    
    if (formAccess==null || formAccess.equals(""))
     		formAccess="Personal";
   
    if (action!=null && action.equalsIgnoreCase("delete"))
    {
    	if (formAccess.equals("Public") || (formAccess.equals("PublicEdit")) )
	   userId= "public";
	   
	   fu.deleteForm(formId, userId);
	request.setAttribute("formAccess","");
       
        response.sendRedirect( Config.getProperty("URL" ) );
    }
     else if  (action!=null && action.equalsIgnoreCase("download"))
    {
        fu.downloadForm(response, formId, userId );
    }
    else if (action!=null && action.equalsIgnoreCase("edit"))
    {
    	if (formAccess.equals("Public") || (formAccess.equals("PublicEdit")) )
	        userId= "public";
	        
	        
        String xml= Config.getProperty( "customForms" ) + userId + "_" + formId + ".xml";
        
        MraldOutFile.logToFile( Config.getProperty("LOGFILE") , "FormServer.jsp edit aml file is: " + xml );			
			
        String xsl= Config.getProperty( "editStylesheet" );
        String htmlToReturn = XSLTranslator.xslTransform( new File( xml ), new File( xsl ) ).toString() ;
        /*
         *  need to add the formid to the html form so the form will
         *  be overwritten instead of creating a new form
         */
        //pageContext.getOut().print( htmlToReturn.replaceAll("</form>", "<input type=\"hidden\" name=\"formid\" value=\""+formId+"\"></form>") );
        pageContext.getOut().print( htmlToReturn.replaceAll("</form>", "<input type=\"hidden\" name=\"formid\" value=\""+formId+"\"><input type=\"hidden\" name=\"formaccess\" value=\""+formAccess+"\"></form>") );	
	
    }
    else if  (action!=null && action.equalsIgnoreCase("upload"))
    {
        /*
         *  parse the request
         */
        DiskFileUpload upload = new DiskFileUpload();
        List items = upload.parseRequest( request );
        Iterator iter = items.iterator();
        FileItem fileItem = null;
        while ( iter.hasNext() )
        {
            FileItem item = ( FileItem ) iter.next();
            if ( item.getFieldName().equals( "test" ) )
            {
                fileItem = item;
                break;
            }
        }
        if (formId== null || formId.equals("null"))
        {
            fu.storeForm( Long.toString(System.currentTimeMillis()), fileItem.getString(), userId);
        }
        else
        {
            fu.storeForm(formId, fileItem.getString(), userId);
        }
        response.sendRedirect( Config.getProperty("URL" ));
     }
    else if (action!=null && action.equalsIgnoreCase("publish"))
    {
    
    	String[] formIds = request.getParameterValues( "formid" );

        fu.publishForm(formIds, userId);
        response.sendRedirect( Config.getProperty("URL" ) );
    }
    else
    {

//        long formid = Long.parseLong(formId);
//formType="Select"
%>
<mrald:DecisionMaker />
<%
    }
%>