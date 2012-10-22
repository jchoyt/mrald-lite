<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
    </title>
    <meta http-equiv="CACHE-CONTROL" content="NO-CACHE">
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <meta name="GENERATOR" content="Mozilla/4.79 [en]C-20020130M (Windows NT 5.0; U) [Netscape]">
    <title>
      MRALD FAQ
    </title>
    <script language="JavaScript1.2" type="text/javascript">
    function writeMenu()
    {
      document.write( "<a href='#top'>Top of Page</a> | <a href='#what'>What is it and how do I get it?</a> | <a href='#configuration'>Configuration</a>");
    }
    </script>
  </head>
  <body>
    <mrald:validate />
    <%@ page import="org.mitre.mrald.util.Config" %>
    <%=Config.getProperty( "CSS" ) %>
    <a name="top">
    <h1>
      MRALD FAQ
    </h1>
    <script>writeMenu();</script>
    <a name="what"></a><h2>What is it and how to I get it?</h2>
    <b><i>Q: What is MRALD?</i></b>
    <div class="answer">
      A: MRALD is an automated enterprise data delivery system. It runs on almost any platform and will access any database with a reasonably complete JDBC driver. It is domain independent and is ready to deliver data, filtered based on user needs/selections, right after installation.
    </div>
    <br>
     <b><i>Q: Why did MITRE build it? Aren't there COTS products to do this?</i></b>
    <div class="answer">
      A: Yes, but none that met all the goals of the project that created it. Some of the features below you can get from shrink-wrap products, but others you can't. The original goal was to provide tools that disseminate information:<br>

      <ul>
        <li>
          Based on user needs/selections
        </li>
        <li>
          Retrieving information from an RDBMS
        </li>
        <li>
          Providing several standard output formats and some not-so-standard
        </li>
        <li>
          Be accessible to most researchers
        </li>
        <li>
          Be flexible enough to change effortlessly with new schemas, new data sets, new user requirements, and new security and reporting requirements
        </li>
      </ul>
    </div>
    <b><i>Q: Nice, but what, specifically, can it do?</i></b>
    <div class="answer">
      A: Primary features:<br>

      <ul>
        <li>
          Instant, automated delivery
        </li>
        <li>
          Flexible enough to change effortlessly with new schemas, new data sets, new user requirements, and new security and reporting requirements
        </li>
        <li>
          Domain independent - works with any data model
        </li>
        <li>
          Platform-independent, browser-based delivery works on any client with a web browser.
        </li>
        <li>
          No downloads or installation needed
        </li>
        <li>
          Rich set of output formats - Get the data the way you want it - the system is easily extendible to provide any format not provided.
        </li>
        <li>
          Easy to override built-in output formats with customized versions
        </li>
        <li>
          Server-side filtering - Get only the data you want
        </li>
        <li>
          Filter on any field
        </li>
        <li>
          Filter out unwanted fields to get only the data you want
        </li>
        <li>
          Easy to extend the system to provide complex, compound GUI widgets
        </li>
        <li>
          Dramatically reduces IT resource burden (no need to tweak things to satisfy some new requirement)
        </li>
        <li>
          Retrieval consistency across different data sets &amp; types
        </li>
        <li>
          Data input facilities
        </li>
        <li>
          Consistent retrieval &amp; delivery
        </li>
        <li>
          Shield users from data model details
        </li>
        <li>
          Reduces skill requirements of your users - users don't need to be experts in SQL or databases to get the data they need
        </li>
        <li>
          Ability to build and share personalized forms - reduces confusion and clutter on the web site, while giving certain users special access forms
        </li>
        <li>
          XSL, jsp, and css-based look and feel for easy customization for each deployment
        </li>
        <li>
          plus more
        </li>
      </ul>
    </div>
    <br>
     <b><i>Q: Can I get a copy and use it on my project?</i></b>
    <div class="answer">
      A: Yes, go to the MRALD <a href="http://developer.mitre.org/projects/mrald">iSF project page</a> to download the latest release. Make sure you read the current release notes for the latest information. You can get them by clicking on the release number on the <a href="http://developer.mitre.org/project/showfiles.php?group_id=38">download page</a>.
    </div>
    <br>
     <br>
     <b><i>Q: What's the licensing?</i></b>
    <div class="answer">
      A: NOTICE This is the copyright work of The MITRE Corporation, and was produced for the U. S. Government under Contract Number DTFA01-01-C-00001, and is subject to Federal Aviation Administration Acquisition Management System Clause 3.5-13, Rights In Data-General, Alt. III and Alt. IV (Oct., 1996). No other use other than that granted to the U. S. Government, or to those acting on behalf of the U. S. Government, under that Clause is authorized without the express written permission of The MITRE Corporation. For further information, please contact The MITRE Corporation, Contracts Office, 7515 Colshire Drive, McLean, VA 22102-7508, (703) 883-6000. Copyright 2003 The MITRE Corporation.
    </div>
    <br>
    <script>writeMenu();</script>
    <a name="configuration"></a><h2>Configuration</h2>
    <b><i>Q: I have everything set up, but when I try to build a new form, no tables show up.</i></b> <br>
    <br>
    A: Try connecting via the command line, using the username and password from the config.properties file. If you can do a 'SELECT * FROM PEOPLE' and get data, it's likely your SCHEMA is incorrect. Case matters! <br>
    <br>
    <b><i>Q: How do I set up ORACLE so that users can only see specific tables, and they can't log in via SQL*PLUS an get all access to the tables?</i></b> <br>
    <br>
    A: Try this: <br>
    <blockquote>
    1) set up a new user in Oracle, say WEBUSER. <br>
    <br>
    2) create the people table in your own controlled table space in Oracle as such: <br>
    <blockquote>CREATE TABLE People ( <br>
    peopleId NUMBER(3) NOT NULL, <br>
    email VARCHAR2(50) NULL, <br>
    peopleTypeId NUMBER(3) NULL <br>
    ); </blockquote>
    3) grant select, insert, update privileges on PEOPLE to WEBUSER <br>
    <br>
    4) create any other tables you want the system to access <br>
    <br>
    5) for all tables you want the system to access, grant select to WEBUSER and create a public synonym with the same name as the table in your table space. <i> It is important that WEBUSER is created BEFORE the synonyms are created, even though the synonyms are public!</i> <br>
    <br>
    6) set config.properties as follows: <br>
    <blockquote>DBLOGIN: WEBUSER <br>
    DBPASSWORD: [WEBUSER's password] <br>
    SCHEMA: [login name where the tables are - ALL CAPS]
    </blockquote>
    7) Once you successfully log in to mrald the first time, go into the people table (through SQL*PLUS, or similar) and change your peopletypeid from 1 to 3. This will give you additional capabilities over run of the mill users. <br>
    </blockquote>
  </body>
</html>


