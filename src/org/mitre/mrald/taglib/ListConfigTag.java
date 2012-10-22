/*
 *  Copyright 2008 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mitre.mrald.taglib;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.util.Config;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 19, 2002
 */
public class ListConfigTag extends TagSupport
{
    String filename;
    /**
      *
     *  Constructor for the PersonalFormsListTag object
     */
    public ListConfigTag()
    {
        super();
    }

     public void setFilename(String filename)
     {
         this.filename = filename;
     }


    /**
     *  Retrieves and prints the user's personal forms list
     *
     *@return                   An int. Not used for anything.
     *@exception  JspException  Required by TagSupport. Used ot pass up the
     *      other exceptions.
     */
    public int doStartTag() throws JspException
    {
        try
        {
              filename = pageContext.getRequest().getParameter( "filename" );

              if( filename == null || filename.equals( "" ) ) {
                  filename = Config.PROPS_FILE;
              }

              String base_path = pageContext.getServletContext().getRealPath( "/" );
              File config_file = new File(  base_path + "WEB-INF/props/" + filename );

              if( !config_file.exists() ) {
                  throw new JspException( "Could not locate the " + filename + " file" );
              }

              //
              // Loading the contents of the config.properties into a
              // properties object
              //
              Properties config_props = new Properties();
              InputStream in = new FileInputStream( config_file.toString() );
              config_props.load(in);
              in.close();

              Enumeration config_set = config_props.propertyNames();

//               StringBuffer ret = new StringBuffer();
            TreeSet<String> set = new TreeSet<String>();
            String temp, temp2, temp3;
	    String inputType = "text";
                int counter = 0;

                while( config_set.hasMoreElements() ) {
                    temp = (String) config_set.nextElement();
                    if( temp.equals( "BasePath" ) ) {
                        int length = base_path.length() - 1;
                        temp2 = base_path.substring( 0, length ).replace( '\\', '/' );
                    } else {
                        temp2 = config_props.getProperty( temp );
                    }
		    if (temp.toUpperCase().indexOf("PASSWORD") > -1) inputType = "password";
		    else inputType = "text";
                    temp3 = "<TR><TD align='right'>" + temp + " = </TD><TD><input type='" + inputType + "' size='60' name='NewFieldValue" +
                        counter + "' value='" + temp2 + "'><input type='hidden' name='NewFieldName" + counter +
                        "' value='" + temp + "'></TD><TD ALIGN='center'><input type='checkbox' name='DeleteField" + counter +
                        "' value='yes'></TD></TR>\n";
                    set.add( temp3 );
                    counter++;
                }




                String count_tag = "<input type='hidden' name='count' value='" + counter + "'>\n";
                set.add( count_tag );
                String file_tag = "<input type='hidden' name='filename' value='" + filename + "'>\n";
                set.add( file_tag );

            Iterator iter = set.iterator();
            while ( iter.hasNext() )
            {
                pageContext.getOut().print( iter.next() );
            }
        }
        catch ( IOException e )
        {
            throw new JspException( e.getMessage() );
        }
        return 0;
    }

}

