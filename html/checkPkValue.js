var passedLabel
var passedValue
function checkPkExists(table, field, value, label) {
    passedValue = value;
    passedLabel = label;
    var url = '../ajaxCheckPkValueExists.jspx?table=' + table + '&field=' + field + '&value=' + value;
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
        alert("Couldn't perform check" + E);
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
            //alert("Couldn't perform check.\nMake sure the subject ID given is unique."); //need to change "subject ID"
        }
    }
}

function parseMessages() {
    // alert(req.responseText);
    PkAlreadyExists = req.responseText;
    if (PkAlreadyExists.indexOf('OK') == -1)
    {
        alert("A " + passedLabel + " of " + passedValue + " already exists.  \nA unique value for " + passedLabel + " must be provided.");
    }
}
