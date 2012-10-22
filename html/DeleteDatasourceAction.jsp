<%@ page import="org.mitre.mrald.admin.*,org.mitre.mrald.util.*,java.util.*,java.io.*" %>
<mrald:validate doAdminCheck="yes" />
<%
  String ds = WebUtils.getRequiredParameter(request, "datasource");
  String sure = WebUtils.getOptionalParameter(request, "imsure");
  if(sure.equals(""))
  {
      %>
        <html>
            <head>
                <title>Delete Data Source</title>
                <%=Config.getProperty( "CSS" )%>
                <style>
                td
                {
                    background-color: transparent;
                    font-size: 100%;
                    vertical-align: top;
                }
            </style>
            </head>
            <body>
                <div id="header">
                    <h1 class="headerTitle">
                        <jsp:expression>Config.getProperty( "TITLE" )</jsp:expression>
                    </h1>
                </div>
                <div class="subHeader">
                <span class="doNotDisplay">Navigation:</span>
                MRALD - Finding the gems in your data |
                <a href="index.jsp" target="_top">Home</a>
                |
                <a href="mailto:mrald-dev-lists@lists.mitre.org">Contact MITRE</a>
                </div>
                <div class="floating-text" style="font-size:1.5em;">
                You must specify that you are sure you want to delete the datasource.  Use the browser back button and check the
                "Yes, I'm sure" box.
                </div>
            </body>
        </html>
      <%
  }
   else
  {
      if( ds.equals("main") )
      {
      %>
        <html>
            <head>
                <title>Delete Data Source</title>
                <%=Config.getProperty( "CSS" )%>
                <style>
                td
                {
                    background-color: transparent;
                    font-size: 100%;
                    vertical-align: top;
                }
            </style>
            </head>
            <body>
                <div id="header">
                    <h1 class="headerTitle">
                        <jsp:expression>Config.getProperty( "TITLE" )</jsp:expression>
                    </h1>
                </div>
                <div class="subHeader">
                <span class="doNotDisplay">Navigation:</span>
                MRALD - Finding the gems in your data |
                <a href="index.jsp" target="_top">Home</a>
                |
                <a href="mailto:mrald-dev-lists@lists.mitre.org">Contact MITRE</a>
                </div>
                <div class="floating-text" style="font-size:1.5em;">
                You can't delete the main database through this interface.
                Delete the database properties from
                <a href="AdminConfig.jsp?filename=config.properties">config.properties</a>
                and reload the database metadata.
                </div>
            </body>
        </html>
      <%
      }
      else
      {
        File dsFile = new File(Config.getProperty("BasePath") + "/WEB-INF/props", ds  );
        File destFile = new File(Config.getProperty("BasePath") + "/WEB-INF/props", ds + ".deleted" );
        if( !dsFile.exists() )
        {
            throw new RuntimeException("ARGH.  File doesn't exist: " + dsFile );
        }
        boolean moved = dsFile.renameTo(destFile);
        if( moved )
        {
            MetaData.reload();
        }
        else
        {
            throw new RuntimeException("ARGH.  couldn't rename: " + dsFile );
        }
        response.sendRedirect(Config.getProperty("AdminUrl" ));
      }
  }
%>
