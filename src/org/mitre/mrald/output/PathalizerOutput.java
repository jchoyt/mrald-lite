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
package org.mitre.mrald.output;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.mitre.mrald.util.MraldOutFile;
import org.mitre.mrald.util.*;
import org.mitre.mrald.query.*;

/**
 *  This PathalizerOutput Class specializes the OutputManager class. It specifically
 *  gathers information pertaining to the output columns desired by the user.
 *  As the query is constructed, this class adds output information and updates
 *  the columns and joins necessary to obtain this output information
 *
 * @author     Hanh Vuong
 * @created    June 2, 2005
 * @version    1.0
 */

public class PathalizerOutput extends OutputManager
{
    /**
     *  Constructor for the TextOutput object
     */
    private String ext=".db";
    private String PREFUSE    = "PREFUSE";
    private String PATHROOT   = "PATHROOT";
    private String PERL_EXE   = "PERL_EXE";
    private String PATHALIZER = "PATHALIZER";
	private String PATHCONFIG = "PATHCONFIG";
    private String delim = " ";

	public @Override void printLimit(){}


    /**
     * This method prepares the headers for the file types based on the contents of the MsgObject Instance
     */

    public @Override void prepareHeaders( )
        throws OutputManagerException
    {
        //don't need this - printBody() redirects the output
    }

    /**
     *  Cycles through the result set and prints the rows to out.
     *
     */
    public @Override void printBody( )
        throws SQLException, IOException
    {


        Process extProcess = null;
        //write to temp dir - must be the same dir with pathalizer program
        String dbfile = Config.getProperty(PATHROOT) + "/" + msg.getUserId() + ext;

        //write query result to output file

        File dbf = new File(dbfile);

		PrintWriter dbout = new PrintWriter( new FileWriter(dbf));

        writeDBFile(delim, dbout);

        String Exec  = Config.getProperty(PERL_EXE);
        String args  = " ";
        args += Config.getProperty(PATHROOT)+"/"+Config.getProperty(PATHALIZER); //program file
        //args += " --config=" + Config.getProperty(PATHROOT)+"/"+Config.getProperty(PATHCONFIG); //config file
        args += " --config=" + Config.getProperty(PATHCONFIG); //config file
        args += " "+ dbfile;  //input file for pathlizer


        String args2  = " ";
        args2 += Config.getProperty(PATHROOT)+"/"+Config.getProperty(PATHALIZER) + "_v2"; //program file
        //args += " --config=" + Config.getProperty(PATHROOT)+"/"+Config.getProperty(PATHCONFIG); //config file
        args2 += " --config=" + Config.getProperty(PATHCONFIG); //config file
        args2 += " "+ dbfile;  //input file for pathlizer

        //the command line cannot longer than 256 chars in some OS
        //path cannot have space between directory seperator in some OS
        String PathScript = Exec + args ;
        String PathScript2 = Exec + args2 ;
        System.out.println(PathScript);


        MraldOutFile.logToFile( "PathalizerOutput: performAnalysis(): Sending to the System Command" );
        MraldOutFile.logToFile( "PathalizerOutput: performAnalysis(): " + PathScript  );
        MraldOutFile.logToFile( "PathalizerOutput: performAnalysis() 2: " + PathScript2  );
         extProcess = Runtime.getRuntime().exec( PathScript );

        //write pathalizer output to outfile
        try {

            /*
             * Read data from process inputStream and write to outStream
             */
            //remoe @ from the file name before send the name to JSP
        	String fname = msg.getUserId().replace('@','.') +  ".pfs";

        	String fname_version2 = msg.getUserId().replace('@','.') +  ".pfs_v2";

            //the path must be set in BasePath because prefuse program config to look input in BasePath
            String prefuseDir = Config.getProperty( "BasePath") +"/"+PREFUSE;
            File dir = new File(prefuseDir);
            if ( !dir.isDirectory() ){
            	dir.mkdir();
            }

            String prefuseFile = prefuseDir + "/" + fname;
            File pfile = new File(prefuseFile);

            PrintWriter fout = new PrintWriter( new FileWriter(pfile));

            InputStream in = extProcess.getInputStream();
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = buf.readLine()) != null) {
			   fout.println(line);
            }
            in.close();
            fout.close();
			//delete temporary db output file

            //dbf.delete();

            extProcess.waitFor();

            //Do again
            extProcess = Runtime.getRuntime().exec( PathScript2 );
            prefuseFile = prefuseDir + "/" + fname_version2;
            pfile = new File(prefuseFile);

            fout = new PrintWriter( new FileWriter(pfile));

            in = extProcess.getInputStream();
            buf = new BufferedReader(new InputStreamReader(in));

            while ((line = buf.readLine()) != null) {
			   fout.println(line);
            }
            in.close();
            fout.close();

            //get ServletRequest and ServletResponse
            HttpServletRequest req =  msg.getReq();
			HttpServletResponse res =  msg.getRes();
			//Forward request to new jsp page
			//the fileName must locate in BaseUrl because prefuse config to look program at this dir
			String URL = "/vizOut.jsp?fileName="+PREFUSE+"/"+ fname ;
			System.out.println(URL);

            RequestDispatcher rd = req.getRequestDispatcher(URL);
            rd.forward(req,res);
            //cannot cleanup pathalizer output
            //pfile.deleteOnExit();
        }catch (InterruptedException e){
            throw new MraldError( e );
        }catch (ServletException se){
			throw new MraldError( se );
        }

    }



  /**
   * Write result query to file system. This file is needed for execute the pathalizer
   * @param delim
   * @param dbfile
   * @throws SQLException
   * @throws IOException
   */

  private void writeDBFile(String delim,PrintWriter out)
          throws SQLException, IOException
  {
      int row_count = 0;
      float fileSize = 0;
      //write query output to system file
      String formattedString;


      while ( ( rs.next() ) &&
              ( ( lineLimitSize == -1 ) || ( row_count < lineLimitSize ) ) &&
              ( ( mbLimitSize == -1 ) || ( fileSize < mbLimitSize ) ) )
      {
          for ( int i = 0; i < niceNames.length; i++ )
          {

              /*
               * Custom formatting here
               */
              if ( classNames[i].equals( "Timestamp" ) )
              {
                  formattedString = getAndFormat( rs.getTimestamp( i + 1 ), formats[i] );
              }
              else if ( classNames[i].equals( "BigDecimal" ) )
              {
                  formattedString = getAndFormat( rs.getBigDecimal( i + 1 ), formats[i] );
              }
              else
              {
                  formattedString = rs.getString( i + 1 );
              }
              out.print( formattedString );
              if ( i < niceNames.length - 1 )
              {
                  out.print( delim );
              }else {
                  out.println();
              }
              try
              {
                  fileSize += formattedString.length() + 1;    //1 for delimiter
              }
              catch ( NullPointerException npe )
              {
                  fileSize += 5;
              }
          }// ENDFOR
		row_count++;
      }// ENDWHILE
      recordsReturned = row_count;
      bytesReturned = fileSize;

      out.close();
  }//method
}

