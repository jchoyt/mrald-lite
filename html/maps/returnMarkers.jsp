<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.mitre.mrald.util.*"%>
<%

	try
	{
	
		
	    String markerText = "<marker name=\"<:name:>\" info=\"<:info:>\" latitude=\"<:latVal:>\" longitude=\"<:longVal:>\" />";
	    StringBuffer returnResults = new StringBuffer("<markers>");
	    String query = WebUtils.getRequiredParameter( request, "query" );
	    String lastName = WebUtils.getRequiredParameter( request, "name" );
    
	    String lessThan = "&lt;";
	    String greaterThan = "&gt;";
	    String lineSplit = lessThan+ "br/" + greaterThan;
 	     String datasource = "db_carrolls.props";
	    
	    query = query +  "'" + lastName + "%'";
            MraldConnection mconn = new MraldConnection(datasource);
	    ResultSet rs = mconn.executeQuery(query);
	    while(rs.next())
            {
	    	String fname = rs.getString(1);
               String lname = rs.getString(2);
               String latitude = rs.getString("latitude");
               String longitude = rs.getString("longitude");
	       String branch = rs.getString("branch");
	       String title = rs.getString("title");
	       branch = branch.replaceAll("&", "&amp;");
	       title = title.replaceAll("&", "&amp;");
	       String info = title + lineSplit + branch;
	      
	       for (int i=1; i < 8; i++)
	       {
	       		String levelData= rs.getString("level_" + i);
			//Add to info : if not null
			if ( (levelData != null) && (!levelData.equals(""))) 
			{
				 if (levelData.contains("&"))
				 {
				 	levelData = levelData.replaceAll("&", "&amp;");
				}
	       
				info +=  lineSplit + " Level "+ i +":  "  + levelData;
			}
			else break;
				
	       }
	        String phoneNum = "(" + rs.getString("phoneac") + ") " + rs.getString("phonenum");
		
	       info += lineSplit + " " + phoneNum + " ";
	       
	       String resultText = markerText.replaceAll("<:name:>", fname + " " + lname);
	        resultText = resultText.replaceAll("<:info:>",  info );
		resultText = resultText.replaceAll("<:latVal:>", latitude);
		resultText = resultText.replaceAll("<:longVal:>", longitude);
		
		returnResults.append(resultText);
	    }
	       
	    returnResults.append("</markers>");
	    
	    mconn.close();
	    out.print( returnResults.toString());
	    }
	 catch(Exception e)
        {
            throw new RuntimeException(e);
        }

%>

