<%@ page import="org.mitre.mrald.util.Config" %>
<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>
    <div class="leftSideBar">
    <p class="sideBarTitle">Current COI</p>    
       <center>
       <lattice:showCOI />
       </center>
       <br/>
      <p class="sideBarTitle">Simple Label Form</p>
      <p class="sideBarText">This form allows for changing the COI that is associated with data. 
    Once the data is retieved, you will be presented with a page that will allow you to update the COI associated with the retieved data.
     For more control over what data is retrieved, use the <a href="<%=Config.getProperty("BaseUrl")%>/switchForm.jsp">advanced version of this form</a></p>
    </div>
    <div class="leftSideBar" style="text-align:left;">
      <p class="sideBarTitle">Output options</p>
      <p class="sideBarText">
      <input type="radio" value="none" name="outputSize" />
      Show all rows
      <br />
      <input type="radio" value="lines" name="outputSize" checked="checked" />
      Show only <input name="outputLinesCount" type="text" size="6" value="500" /> rows
      <br />
      </p>
      <p class="sideBarText">
      <input type="hidden" name="Format" value="setLabel" />
      </p>
      
    </div>
