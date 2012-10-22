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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;

public class MitreParameterOutput extends ParameterOutput
{

    public MitreParameterOutput()
	{
        super();
		dataFileDelimiter = '\t';
	}

	protected String getDropDown( String classType, int instanceNum, int count, boolean canBeInt )
	{

		StringBuffer ret = new StringBuffer();

		if ( classType.equals( "BigDecimal" )  && canBeInt )
			ret.append( "<option value=\"Type:Continuous\">Continuous</option>\n" );

		if ( count <= maxCategories )
			ret.append( "<option value=\"Type:Nominal\">Nominal Specification (Class)</option>\n" );

		ret.append( "<option value=\"Type:Ignore\">Ignore</option>\n" );
		ret.append( "<option value=\"Type:Ordered\">Ordered</option>\n" );

		return ret.toString();
	}

	protected String getFieldDropDown()
	{
        StringBuffer ret = new StringBuffer();
        for ( int p = 0; p < niceNames.length; p++ )
        {
            ret.append( "<option value=\"" + niceNames[p] + "\">" + niceNames[p] + "</option>\n" );
		}
		return ret.toString();
	}

	protected String makeDataFile( MsgObject msg )
		throws ServletException, IOException, SQLException, OutputManagerException
 	{
		String fileLocation = msg.getUserId() + System.currentTimeMillis() + ".data";
		File dir = new File( System.getProperty( "java.io.tmpdir" ) );
		if (!dir.exists())
			dir.mkdir();

		FileOutputStream fout = new FileOutputStream( new File( System.getProperty( "java.io.tmpdir" ) + "/" + fileLocation ) );

        int row_count = 0;
        float fileSize = 0;
        StringBuffer formattedString;
		String currentValue;

		// for ( int i = 0; i < niceNames.length; i++ )
		// {
			// currentValue = niceNames[i];
			// fout.write( currentValue.getBytes() );
			// if ( i < ( niceNames.length - 1 ) )
				// fout.write( ( dataFileDelimiter + "" ).getBytes() );
		// }
		// fout.write( Config.NEWLINE.getBytes() );

		initializeSets( niceNames.length );

        while ( ( rs.next() ) &&
                ( ( lineLimitSize == -1 ) || ( row_count < lineLimitSize ) ) &&
                ( ( mbLimitSize == -1 ) || ( fileSize < mbLimitSize ) ) )
        {

			formattedString = new StringBuffer("");

            for ( int i = 0; i < niceNames.length; i++ )
            {
				if ( ! formattedString.toString().equals("") )
					formattedString.append( dataFileDelimiter );
				//
				// Finding correct formatting of the output
				//
                if ( classNames[i].equals( "Timestamp" ) )
                    currentValue = getAndFormat( rs.getTimestamp( i + 1 ), formats[i] );

                else if ( classNames[i].equals( "BigDecimal" ) )
                    currentValue = getAndFormat( rs.getBigDecimal( i + 1 ), formats[i] );

                else
                    currentValue = rs.getString( i + 1 );

				categories[i].add( rs.getString( i + 1 ) );
				formattedString.append( currentValue );
				//
				// Finding the correct size to be added to the returned file size
				//
                try
				{
                    fileSize += formattedString.length() + 9; // 9 for tags
				}
                catch ( NullPointerException npe )
				{
                    fileSize += 13; // 13 for tags and "null"
				}
            }

            fout.write( formattedString.toString().getBytes() );
            fout.write( Config.NEWLINE.getBytes() );

            row_count++;
            fileSize += 10;

        }
        recordsReturned = row_count;
        bytesReturned = fileSize;
		fout.close();

		return System.getProperty( "java.io.tmpdir" ) + "/" + fileLocation;
	}

}
