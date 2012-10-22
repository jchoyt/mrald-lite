var http = getHTTPObject();      
var isBusy = false;
var title="";			
		
var useValue = true;				
var parameterList ="";
var listNumber = 0;
var valueStr = "";

function insertData(paramList, listCount) {
try
{
		var myurl = "DropDown.jsp?"
        	document.getElementsByName("listCount")[0].value =listCount;
		//In case - for some reason the listCount has not been set
		if (listCount == 0)
			listCount = listNumber;		  	
		if (isBusy)
		{
			http.onreadystatechange = function () {}
			http.abort();
		}
									
		http.open("GET", myurl + paramList, true);
        isBusy = true;
		http.onreadystatechange = useHttpResponse;
					
		if ( !callInProgress(http) ) {
      		http.send(null);
		} else {
      		alert("I'm busy. Wait a moment");
		}
}
catch(e)
{
        alert("error: " + e);
}
}

function selectData(paramList, listCount) {
try
{
		var myurl = "DropDown.jsp?"
        	document.getElementsByName("listCount")[0].value =listCount;
		//In case - for some reason the listCount has not been set
		if (listCount == 0)
			listCount = listNumber;		  	
		if (isBusy)
		{
			http.onreadystatechange = function () {}
			http.abort();
		}
									
		http.open("GET", myurl + paramList, true);
        isBusy = true;
		http.onreadystatechange = useHttpResponse;
					
		if ( !callInProgress(http) ) {
      		http.send(null);
		} else {
      		alert("I'm busy. Wait a moment");
		}
}
catch(e)
{
        alert("error: " + e);
}
}
 
//This is to get around the fact that the setTimeOut cannot take variables within a function
function selectDataWithoutParams() {
try
{
		var myurl = "DropDown.jsp?"
      
		document.getElementsByName("listCount")[0].value =listNumber;
	
		if (isBusy)
		{
			http.onreadystatechange = function () {}
			http.abort();
		}
									
		http.open("GET", myurl + parameterList, true);
        isBusy = true;
		http.onreadystatechange = useHttpResponse;
					
		if ( !callInProgress(http) ) {
      		http.send(null);
		} else {
      		alert("I'm busy. Wait a moment");
		}
}
catch(e)
{
        alert("error: " + e);
}
}

function loadString(paramStr, lcount) {
try
{
	var ldiv = document.getElementById("list" + lcount);
					   
    var textout = paramStr;
            
    ldiv.innerHTML = textout;
}
catch(e)
{
        alert("error: " + e);
}
}
 
function clickNav(obj, listNo, useValueToFilter)
{
	useValue = useValueToFilter;
	clickNav(obj, listNo);	
}
 
function clickNav(obj, listNo)
{
			

		var selectedValue = obj[1].value;   
		
		if (selectedValue.indexOf("Table") > -1)
		{		
			selectedValue = obj[0].value;
		}
		
		if (selectedValue.indexOf("Operator") > -1)
		{
			selectedValue = obj[0].value;
			if (selectedValue.indexOf("Table") > -1)
			{		
				selectedValue = obj[2].value;
			}
		}
		
		setLink(listNo, selectedValue);
		
					
}
            
function useHttpResponse() {
		var lcount = document.getElementsByName("listCount")[0].value;
                      		  
		if (http.readyState != 4) return;
					
		isBusy = false;
				
		if (http.readyState == 4) {
			/*window.clearTimeout(timeoutId);*/
            var ldiv = document.getElementById("list" + lcount);
					   
            var textout = http.responseText;
            var spacer = "<img alt='' src='images/spacer.gif' width='25' height='1'/>" ;
	    
	  
            ldiv.innerHTML = spacer + textout + title;
	     // alert("textOut; " + textout);
            title="";
         }
              
}
            
function setLink(linkNo, param)
{
		var tg = document.getElementById("dropdownlink" + linkNo);
		
		if (tg != null)
		{
			tg.setAttribute("value", param);	
			var newlinkNo = linkNo *1 + 1;
			listNumber = linkNo;
			getLink(newlinkNo);
		}
		
		
}
			
function buildArg(table, datasource, column, listColumn, filtertable, filtercolumn, value, linkNo)
{
		
			var temp = "tableName=" + table + "&datasource=" + datasource + "&pkColumnName=" + column + "&listColumnName=" + listColumn + "&filterTable=" + filtertable+"&filterColumn=" + filtercolumn + "&filterCategoricalNo=" + linkNo; 
			if (useValue)
			{
				temp = temp + "&filterColumnValue=" + value ; 
			}		
			
			parameterList = temp;
			//var timeoutId = window.setTimeout(selectData(temp, linkNo),5000);
			listNumber = linkNo;
			
			var timeoutId = window.setTimeout("selectDataWithoutParams()",200);
			//selectData(temp, linkNo);
		
}
			
function getLink(linkNo)
{
		var oldlinkNo = linkNo *1 - 1;
				
		var filterTable = "";
		var filterColumn = "";
		var value = "";
        
		if (linkNo!=1)
		{
		    var tg = document.getElementById("dropdownlink" + oldlinkNo);
				
			filterTable = tg.getAttribute("table");
            		filterColumn = tg.getAttribute("column");
			value = tg.getAttribute("value");
			var datasource = tg.getAttribute("datasource");
			
		}	
		//alert("datasource:" + datasource);
		
		var newtg = document.getElementById("dropdownlink" + linkNo);
				
		var listColumn = newtg.getAttribute("listColumnName");
		var tableName = newtg.getAttribute("tableName");
        	var pkColName = newtg.getAttribute("pkColumnName");
        	
		title = newtg.getAttribute("title");
 
		if (title == null) title ="";
		//var datasource = newtg.getAttribute("datasource");
                
		buildArg(tableName, datasource, pkColName, listColumn, filterTable, filterColumn, value,linkNo);
}
			                                                                      
function loadSelect(table, datasource, pkColumn, listColumn, filterNo)
{
         var paramStr ="tableName=" + table + "&datasource=" + datasource + "&pkColumnName=" +pkColumn + "&listColumnName=" + listColumn + "&filterCategoricalNo=" + filterNo;
         listNumber = filterNo;
	 selectData(paramStr, filterNo);
			   
}
            
function loadSelectWithTitle(thisTitle, table, datasource,  pkColumn, listColumn, filterNo)
{
         /* alert("in onload"); */
        title = thisTitle;	   
        loadSelect( table, datasource, pkColumn, listColumn, filterNo);
}		
            
function getHTTPObject() 
{ 
        var xmlhttp; 
        try
        {
           var browser = navigator.appName;
           /* alert("browser" + browser); */
           if(browser == "Microsoft Internet Explorer")
           {
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            else
            {
                xmlhttp = new XMLHttpRequest(); 	
            }
         }
         catch(E)
         {
             alert("not found" + E);
         }
         return xmlhttp; 
}
			
function callInProgress(http) {
    switch ( http.readyState ) 
	{
        case 1, 2, 3:
        return true;
        break;

        // Case 4 and 0
        default:
        return false;
        break;
   	}
}
			
function reportTimeout()
{
		alert("go away! I'm busy!");	
}

/**************new functionality******************/
function returnNumber(f, listCount, dir)
{

	document.getElementsByName("listCount")[0].value =listCount;
		
    var str = getFormValues(f,"validate");
    returnNumberOfResults(str, dir);
}

function returnNumberOfResults(str, dir) {
try
{
		var myurl = dir + "FormSubmit?workflow=Building%20SQL";
	
		//var myurl = "FormSubmit";
		if (isBusy)
		{
			http.onreadystatechange = function () {}
			http.abort();
		}
									
		
		http.open("POST", myurl,true);
               
		isBusy = true;
		http.onreadystatechange = useHttpResponse;
					
		if ( !callInProgress(http) ) {
      		//http.send(null);
		 http.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		http.send(str);
            
		} else {
      		alert("I'm busy. Wait a moment");
		}
}
catch(e)
{
        alert("error: " + e);
}
}
 
function getFormValues(fobj,valFunc)
{

	var f= fobj;
	
	var str = "";

       var valueArr = null;

       var val = "";

       var cmd = "";

       		  
       for(var i = 0;i < f.elements.length;i++)
       {

	   if (f.elements[i] == null)
		   continue;
	      
	     switch(f.elements[i].type)
           {

		  case "text":

                   
                    str += fobj.elements[i].name +

                     "=" + escape(fobj.elements[i].value) + "&";

                     break;

	       case "hidden":

                   
                    str += fobj.elements[i].name +

                     "=" + escape(fobj.elements[i].value) + "&";

                     break;
		     
               case "select":

	       	   alert("select");
	           str += fobj.elements[i].name +

                    "=" + fobj.elements[i].options[fobj.elements[i].selectedIndex].value + "&";

                    break;

	     case "select-one":

	       	   str += fobj.elements[i].name +

                    "=" + fobj.elements[i].options[fobj.elements[i].selectedIndex].value + "&";

                    break;
           }

       }
           
       str = str + valueStr;
       str = str.substr(0,(str.length - 1));
       return str;

}

