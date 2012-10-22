
<%@ page import="org.mitre.mrald.util.Config" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%= Config.getProperty("CSS")%><META HTTP-EQUIV="CACHE-CONTROL" CONTENT = "NO-CACHE"></HEAD>
<BODY>
<TABLE SUMMARY="" WIDTH="100%" BORDER="0" CELLSPACING="0" CELLPADDING="0">
<TR>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr><TD CLASS="bord">
<TABLE SUMMARY="" WIDTH="100%" BORDER="0" CELLSPACING="1" CELLPADDING="0">

<TR><!--td width="12%" class="nav" onMouseOver="this.className='navhilite';">Test</td-->
<form ENCTYPE="x-www-form-urlencoded" ACTION="../LatticePanel.jsp" METHOD="POST" target="groupList">
<input type="hidden" name="showLattice" value="1">
<TH><B><input name="ModifyNode" value="Add" type="submit"></input></B></TH>
<TH><b><input name="ModifyNode" value="Delete" type="submit"></input></b></th>
<TH><b><input name="ModifyNode" value="Add Link" type="submit"></input></b></th>
<TH><b><input name="ModifyNode" value="Delete Link" type="submit"></input></b></th>
<TH><b><input name="ModifyNode" value="Focus" type="submit"></input></b></th>
<TH><b><input name="ModifyNode" value="Unfocus" type="submit"></input></b></th>
</form>
<form ENCTYPE="x-www-form-urlencoded" action="../latticePrefs.jsp" METHOD="POST"  target="groupList"><TH><b><input name="Prefs" value="Preferences" type="submit" ></input></b></th></form>
</TR>
</TABLE>
</TD>
</TR>
</TABLE>
</TR>
<BR>
</TABLE><img src="graph_1.jpg" width="900" height="800" order="0" align="center" ismap usemap="#map">
</form>
</body>
</HTML>