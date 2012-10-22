
<%@ page import = "org.mitre.mrald.util.Config"%>
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2//EN">
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      MRALD form building restrictions
    </title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <mrald:badForm />
    <h1>
      MRALD Form Building Restrictions
    </h1>
      An MRALD form represents a set of database tables and a unique way of joining them together to retrieve data around a certain concept.  Trying to force a single form to do more than this will cause unpredictable or incorrect results (it is possible to build an MRALD form that will build multiple queries and therefore bypass these issues, however these forms must be hand built).  Therefore, when more than one table is listed in an MRALD form, each listed table must be connected to the others by a single, acyclic path.  There are two problems that give rise to this restriction:
    <h2>
      Incomplete join path
    </h2>
    This occurs when there is not a complete join path amongst all the tables included on the form. The result of an incomplete join path is a cross-product. This will result in a set containing every possible combination of the records in the pair of relations being joined without regard to the context of the relationships. Such a result is basically useless in terms of retrieving useful data from normal data models.
    <h2>
      Non-unique join path
    </h2>
    This occurs when there is more than one join path between any two tables. While the query built will be valid and not produce a cross-product, which join path to use, and therefore the query result, is indeterminate. Should MRALD use both paths, or one? And if only one, which one? When creating an MRALD form, the user should have a single concept of how these tables are to be joined and all other join paths should be set to be ignored in the second step in the form building process.
  </body>
</html>


