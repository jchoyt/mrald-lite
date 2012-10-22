<%@ page import="org.mitre.mrald.util.Config" %>
    <div class="leftSideBar">
      <p class="sideBarTitle">Simple Form</p>
      <p class="sideBarText">This form selects a fixed set of data according to the filter selections chosen by the user.  For more control, use the <a href="<%=Config.getProperty("BaseUrl")%>/switchForm.jsp">advanced version of this form</a></p>
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
      <input type="hidden" name="Format" value="browserHtml" />
      </p>
    </div>
