var passedLabel
var passedValue
var passedid


function showSample(popupid, objid, dir, filterObj)
{

	datasource = "main";
	formObj = document.forms[0];
    datasource=formObj.Datasource.value;
    
	filterObj = document.getElementById(filterObj);
	if (filterObj != null)
		filter = filterObj.value;
	else
		filter= "undefined";

	obj = document.getElementById(objid);

	valueStr = obj.value;

	if (valueStr == "")
	{
		alert("You must supply a column to filter on, from the first list,  to enable this functionality.");
		return;
	}

	splitString = valueStr.split("~");
	var noOfValues = splitString.length;
	var table;
	var field;
	var label;
	var val;
	var id = obj.name;
	for (i=0; i< noOfValues; i++)
	{
		valueChunk = splitString[i];
		//alert("value: " +valueChunk);
		if (valueChunk.indexOf("Table") > -1)
		{
		   valuePair = valueChunk.split(":");
		   table = valuePair[1];
		 }
		if (valueChunk.indexOf("Field") > -1)
		{
		   valuePair = valueChunk.split(":");
		   val = valuePair[1];

		}
		if (valueChunk.indexOf("Label") > -1)
		{
		   valuePair = valueChunk.split(":");
		   label = valuePair[1];
		}

	}
	popupid ="popUp";
	displaySampleValue(objid, dir, popupid, table, val, 5, label, filter);

}

function displaySampleValue(objid, dir, id, table, field, value, label) {
    passedid = id;
    passedValue = value;
    passedLabel = label;
    var url = dir + 'ajaxShowSampleValue.jspx?Datasource=' + datasource + '&objId='+ objid +'&popUpId=' + id + '&table=' + table + '&field=' + field + '&number=' + value + '&label=' + label +'&filter=undefined';

    //alert("url: " + url);
    returnResults(url);

}

function displaySampleValue(objid, dir, id, table, field, value, label, filter) {
    passedid = id;
    passedValue = value;
    passedLabel = label;
    var url = dir + 'ajaxShowSampleValue.jspx?Datasource=' + datasource + '&objId='+ objid +'&popUpId=' + id + '&table=' + table + '&field=' + field + '&number=' + value + '&label=' + label + '&filter=' + filter;
    //alert("url:" + url);
    returnResults(url);

}

function returnResults(url)
{
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
       req = new ActiveXObject("Microsoft.XMLHTTP");
    }
    req.onreadystatechange = processRequest;
    try
    {
        req.open("GET", url, true);
        req.send(null);
    }
    catch(E)
    {
        alert("Start: Couldn't display sample data. " + E);
    }


}

function processRequest() {
    if (req.readyState == 4) {


        if(req.status == 200)
        {
          parseMessages();
        }
        else
        {
            //alert(req.responseText);
            alert("Process Request: Couldn't display Sample data.\n. Status is " + req.status  + ".");
        }
    }
}

function closeList(id)
{
  //alert("closing: " + id);
  obj = document.getElementById(id);
  obj.innerHTML = "";
}

function setFilterValueFromSample(id, filterValue)
{
  obj = document.getElementById(id);
  //alert("id is " + id + " and filter on " + filterValue);

  obj.value = filterValue;
}
function parseMessages() {
    //alert(req.responseText);
    sampleResponse = req.responseText;
   
    obj = document.getElementById(passedid);

  


 	if (window.ActiveXObject)
 	{
 		var xx = sampleResponse;

 		//alert("26- Sorry not supported by IE: Try using Firefox - it's got great features, including Tabs.  www.mozilla.com");
 		//obj.innerHTML= "<![CDATA[";
 		//obj.innerHTML= '<![CDATA[ <p class="popUpBoxText">test<///p>';
 		obj.innerHTML = xx;

 	}
 	else
 	{

 	    var xx = "<style>.popUpBox { right:185px;} </style>" + sampleResponse;
	    obj.innerHTML = xx;
	}


    /*if (sampleResponse.indexOf('No Data') > -1)
    {
    	alert("Parse Message: Could not find sample data");
    }*/
}
