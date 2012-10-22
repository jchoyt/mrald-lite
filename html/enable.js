function Enable(objElement)
{
    objElement.disabled = false;
}

function Disable(objElement)
{
    objElement.disabled = true;
}


function checkEnable(objElement)
{
    var i=0;
    var disable = new Boolean(true);
    while ((disable) && (i < 3))
    {
        i++;
        if ((document.getElementsByName('Stat' + i)[0].selectedIndex > 0) || (document.getElementsByName('Stat' + i)[1].selectedIndex > 0 ) )
        {
            objElement.value='1';
            objElement.disabled = false;
            disable = false;
        }
        if (disable)
        {
            var j =  i + 3;
            if (document.getElementsByName('Group' + j)[0].selectedIndex > 0)
            {
                objElement.value='1';
                disable = false;
            }
        }
    }
    if (disable)
    {
        objElement.value='0';
        objElement.disabled = true;
    }
}


function checkEnableCountOnly(objElement)
{
    var i=0;
    var disableOutput = new Boolean(true);
    if (document.getElementsByName('Stat1')[0].value=0)
    {
        objElement.value='1';
        objElement.disabled = false;
        disableOutput = false;
    }
    if (disable)
    {
        objElement.value='0';
        objElement.disabled = true;
    }
}


/* for processing notification box */

function setProcessing()
{
    document.getElementById("notify").innerHTML='<div class="processing"><p class="processingTitle">Processing your query ...</p><img src="../images/waiting.gif"/></div>';
}


