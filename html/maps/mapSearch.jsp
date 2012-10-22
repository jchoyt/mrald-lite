<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.mitre.mrald.util.*"%>
<%
  	  ArrayList<String> latitudes = new ArrayList<String>();
          ArrayList<String> longitudes = new ArrayList<String>();
          ArrayList<String> names = new ArrayList<String>();
         ArrayList<String> infoList = new ArrayList<String>();
        
	
 	try{
	   
 	    String query = "Select fname, lname, title, branch, latitude,longitude, level_1, level_2, level_3, level_4, level_5, level_6, level_7,phoneac, phonenum from federal where addrstate= 'DC'";
            String datasource = "db_carrolls.props";
            MraldConnection mconn = new MraldConnection(datasource);
	    ResultSet rs = mconn.executeQuery(query);
	       while(rs.next())
            {
	    	 returnRowSet(rs, names, infoList, latitudes, longitudes);
	    
            }
	    mconn.close();
         } 
	    catch(Exception e)
        {
            throw new RuntimeException(e);
        }
	int noOfItems = names.size();
	 float av_lat = 0;
	 float av_long = 0;
	av_lat = av_lat / noOfItems;
	av_long= av_long / noOfItems;
	%>
	
	<%!
	void returnRowSet(ResultSet rs, ArrayList<String> names, ArrayList<String> infoList , ArrayList<String> latitudes, ArrayList<String> longitudes)
	{
		try{
			 double angleInc = Math.PI/10;
			double sinVal = 1;
			double cosVal = 1;
			double angleCount =0;
			float av_lat = 0;
			float av_long = 0;
			String lineSplit = "<br/>";
			String fname = rs.getString(1);
		       String lname = rs.getString(2);
		       String latitude = rs.getString("latitude");
		       String longitude = rs.getString("longitude");
		       String info = rs.getString("title") + lineSplit + rs.getString("branch");
		      
		       for (int i=1; i < 8; i++)
		       {
				String levelData= rs.getString("level_" + i);
				//Add to info : if not null
				if ( (levelData != null) && (!levelData.equals(""))) 
				{
					info +=  lineSplit + "<b>Level "+ i +":</b> "  + levelData;
				}
				else break;
					
		       }
			String phoneNum = "(" + rs.getString("phoneac") + ") " + rs.getString("phonenum");
			
		       info += lineSplit + "<b>" + phoneNum + "</b>";
		       if (latitude.equals("")) return;
		       if (longitude.equals("")) return;
		       
		       float lat_fl = Float.parseFloat(latitude);
		       float long_fl = Float.parseFloat(longitude);
		       
		       av_lat += lat_fl;
		       av_long += long_fl;
		       if ( (fname != null) && (lname != null) && (latitude != null) && (longitude!=null) )
		       {
				names.add( fname + " " + lname );
				//Check to see if lat/long exists
				//if it does then out the other points in a circle around the original point
				if ((latitudes.contains(latitude)) && (longitudes.contains(longitude)))
				{
					angleCount++;
					angleInc = angleInc * angleCount;
					float x_plus = (float)Math.sin(angleInc);
					float y_plus = (float)Math.cos(angleInc);
					lat_fl = lat_fl + 0.0002f * x_plus;
					latitude = lat_fl +"";
					long_fl = long_fl + 0.0002f* y_plus;
					longitude = long_fl +"";
				}
				latitudes.add(latitude);
				longitudes.add(longitude);
				infoList.add(info);
			}
		} 
	    catch(Exception e)
		{
		    throw new RuntimeException(e);
		}
	}
	%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
     
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Google Maps JavaScript API Example</title>
    
  <script type="text/javascript" src="http://www.google.com/jsapi?key=ABQIAAAAw0h_JFH1VjmwAuDc8RkhhxT1Rq-ubx00myVIyhzyK_zU7CPCRBTdagSrGAYGXLyv8ZCYBRFbclIhjA"
      type="text/javascript"></script>

     <script src="http://www.google.com/uds/solutions/localsearch/gmlocalsearch.js" type="text/javascript"></script>
 
<style type="text/css">
  @import url("http://www.google.com/uds/css/gsearch.css");
  @import url("http://www.google.com/uds/solutions/localsearch/gmlocalsearch.css");
</style>
 <script type="text/javascript">
 google.load("maps", "2.x");
  google.load("search", "1.x");
  //alert("average point " + <%=av_lat%> + " : "  + <%=av_long%>);
    //<![CDATA[
    <%
    	String name = names.get(0);
	String latitude = latitudes.get(0);
	String longitude = longitudes.get(0);
	String info = infoList.get(0);
	

    %>
    var map;
		
    function initialize() 
    {
      if (google.maps.BrowserIsCompatible()) 
      {
		map = new google.maps.Map2(document.getElementById("map"));
	
		map.setCenter(new google.maps.LatLng(38.90,-1*77.04),14);
		map.setMapType(G_HYBRID_MAP);
		   
		 map.addControl(new google.maps.LargeMapControl());
		 map.addControl(new google.maps.ScaleControl());
		 map.addControl(new google.maps.MapTypeControl());
		 map.addControl(new google.maps.OverviewMapControl());
		 map.enableScrollWheelZoom();
		 map.doubleClickZoomEnabled();
		 map.enableGoogleBar();
		 var bounds = map.getBounds();
		 <%
		 for (int i =0; i< 100;  i++)
		 {
		 	latitude = latitudes.get(i);
			
			longitude = longitudes.get(i);
			name = names.get(i);
			info =infoList.get(i);
			if (latitude.equals("")) continue;
			if (longitude.equals("")) continue;
		 %>
			var point = new google.maps.LatLng(<%=latitude%>,-1*<%=longitude%>);
			var marker = new google.maps.Marker(point);
		
			google.maps.Event.addListener(marker, "click", 
				function() {
				var myHtml = "<b><%=name%></b>";
				 var   myHtml2 = "<br/><%=info%>" ;
				map.openInfoWindowHtml(new google.maps.LatLng(<%=latitude%>,-1*<%=longitude%>), myHtml + myHtml2);
				});
				
			map.addOverlay(marker);
		  <%
		  }
		  %>
		map.addControl(new google.maps.LocalSearch());
		
	}
    }
    google.setOnLoadCallback(initialize);
    //]]>
 
      function createMarker(point, name, info)
      {
		
		var marker = new google.maps.Marker(point);
      
	       GEvent.addListener(marker, "click", function() {
		  var myHtml = "<b>" + name +"</b>";
		 var   myHtml2 = "<br/>" + info ;
		 map.openInfoWindowHtml(point, myHtml + myHtml2);
		});
      
		return marker;
      }
      
      function searchNames() 
      {
      
      		var query = "Select fname, lname, title, branch, latitude,longitude, level_1, level_2, level_3, level_4, level_5, level_6, level_7,phoneac, phonenum from federal where lname like ";
           
		var columnVal = document.getElementById('columnSearch').value;
		var val = document.getElementById('valueInput').value;
		//var nameVal = document.getElementById('nameInput').value;
		
		if (columnVal == "FirstName")
		{
			query = "Select fname, lname, title, branch, latitude,longitude, level_1, level_2, level_3, level_4, level_5, level_6, level_7,phoneac, phonenum from federal where fname like ";
			
		
		}
      		
		if (columnVal == "LastName")
		{
			query = "Select fname, lname, title, branch, latitude,longitude, level_1, level_2, level_3, level_4, level_5, level_6, level_7,phoneac, phonenum from federal where lname like ";
			
		}
		
		if (columnVal == "Branch")
		{
			query = "Select fname, lname, title, branch, latitude,longitude, level_1, level_2, level_3, level_4, level_5, level_6, level_7,phoneac, phonenum from federal where branch like ";
			
		}
		
		if (columnVal == "FirstLevel")
		{
			query = "Select fname, lname, title, branch, latitude,longitude, level_1, level_2, level_3, level_4, level_5, level_6, level_7,phoneac, phonenum from federal where level_1 like ";
			
		}
		
		if (columnVal == "Title")
		{
			query = "Select fname, lname, title, branch, latitude,longitude, level_1, level_2, level_3, level_4, level_5, level_6, level_7,phoneac, phonenum from federal where title like ";
			
		}
		var searchUrl = 'returnMarkers.jsp?name=' + val + "&query=" + query;
		alert("query" + query);
		GDownloadUrl(searchUrl, function(data) 
		{
			
			var xml = GXml.parse(data);
		
			
			var markers = xml.documentElement.getElementsByTagName('marker');
			map.clearOverlays();
	
			//var sidebar = document.getElementById('sidebar');
			//sidebar.innerHTML = '';
			if (markers.length == 0) 
			{
				//map.setCenter(new google.maps.LatLng(38.90,-1*77.04), 14);
				return;
			}
			
			var bounds = new GLatLngBounds();
			for (var i = 0; i < markers.length; i++)
			{
				var name = markers[i].getAttribute('name');
				var info = markers[i].getAttribute('info');
				var latVal = markers[i].getAttribute('latitude');
				var longVal = markers[i].getAttribute('longitude');
				var point = new google.maps.LatLng(latVal, -1 * longVal);
					      
				var marker = createMarker(point, markers[i].getAttribute('name'),  markers[i].getAttribute('info'));
				map.addOverlay(marker);
			}
		});	
       }
   </script>
   
  </head>
  <body onunload="google.maps.Unload();">
   
  	<Select name="column" id="columnSearch">
		<option value="FirstName">First Name</option>
		<option value="LastName">Last Name</option>
		<option value="Title">Title</option>
		<option value="FirstLevel">First Level</option>
		<option value="Branch">Branch</option>
	</Select>
  	<input type="text" id="valueInput"/>
     <input type="button" onclick="searchNames()" value="Search Names"/>
    <div id="map" style="width:800px; height: 600px"></div>
  

  </body>
</html>