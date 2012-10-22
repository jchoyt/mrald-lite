<%@ page import = "org.mitre.mrald.util.*"%>
<%@ page import = "java.io.*"%>
<%@ page import = "org.apache.commons.fileupload.*"%>
<%@ page import = "java.util.*"%>
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<html>
  <head>
    <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
    <title><%=Config.getProperty( "TITLE" ) %>
    <br />
    Multiple DirectQuery Review Page</title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <div id="header">
      <h1 class="headerTitle">Multiple DirectQuery Data Retrieval Form</h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <form method="POST" action="FormSubmit" enctype="x-www-form-urlencoded">
      <jsp:include page="outputSidebar.html" />
      <center>
        <div class="floating-text">
        <strong>Target database:</strong>
        <select name="Datasource">
            <tags:datasourceList />
        </select>
        <br />
        <input type="hidden" name="form" value="dqUpload.jsp" />
        <input type="hidden" name="workflow" value="Direct SQL" />
        <%
          String there = request.getParameter("queryFile");
          if( there !=null || there != "" )  //if there is a file to upload, parse it
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
              if ( item.getFieldName().equals( "queryFile" ) )
              {
                  fileItem = item;
                  break;
              }
            }
            if (fileItem==null)
            {
              %>Gaaahhhhh! FileInfo is null! <%
            }
            BufferedReader br = new BufferedReader(new StringReader(fileItem.getString()));
            String line;
            while ((line = br.readLine()) != null)
            {
            %>
        <input type="checkbox" checked="checked" value="<%= line %>" name="query" />
        <%= line %>
        <br />
        <%
          }
        }
        else
        {
        %>
          There was no file specified. Use the browser's back button and enter the file path and name you want to parse.
        <%
        }
        %>
        <br />
        <br />
        <div style="text-align: center">
          <input value="Retrieve Data" type="submit" />
        </div>
        </div>
      </center>
    </form>
  </body>
</html>
