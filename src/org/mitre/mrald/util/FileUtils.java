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
package org.mitre.mrald.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.fileupload.*;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *  Description of the Class This is dependent on Oracle 8i.
 *
 *@author     jchoyt
 *@created    August 30, 2001
 */
public class FileUtils
{

	/**
	 *  Constructor for the FormUtils object
	 *
	 *@since
	 */
	public FileUtils() { }



	/**
	 *  Description of the Method
	 *
	 *@param  sourceFile                 Description of the Parameter
	 *@param  destFile                   Description of the Parameter
	 *@exception  FileNotFoundException  Description of the Exception
	 *@exception  IOException            Description of the Exception
	 */
	public void copyFile(File sourceFile, File destFile)
		 throws FileNotFoundException, IOException
	{
		byte[] buffer = new byte[4096];
		FileInputStream in = new FileInputStream(sourceFile);
		FileOutputStream out = new FileOutputStream(destFile);
		int len;
		while ((len = in.read(buffer)) != -1)
		{
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		in.close();
		in = null;
		out = null;
	}


	/**
	 *  This method prepares the output file for the HTML format data
	 *
	 *@param  sourceFilename      Description of the Parameter
	 *@param  backupFilename      Description of the Parameter
	 *@exception  IOException     Description of the Exception
	 *@since
	 */
	public void backupFile(String sourceFilename, String backupFilename)
		 throws IOException
	{

		String backupDir = backupFilename.substring(0, backupFilename.lastIndexOf("/"));

		if (!new File(backupDir).exists())
		{
			boolean created = new File(backupDir).mkdirs();
			if (!created)
			{
				throw new IOException("Couldn't create the directory \"" + backupDir +
					"\".  Check your file system, though, some intermediate directories may have been created");
			}
		}


		File backupFile = new File(backupFilename);
		File sourcefile = new File(sourceFilename);

		copyFile(sourcefile, backupFile);


	}

	/**
	 *  Stores the image to disk. Note this will handle only one file at a time. If
	 *  you want to upload more than one image, you'll have to rework this. The
	 *  first non-form item encountered (a file field) is the one taken.
	 *
	 *@param  req                      Request that contains the upload
	 *@param  user                     Description of the Parameter
	 *@return                          Description of the Return Value
	 *@exception  JspException         Description of the Exception
	 *@exception  FileUploadException  Description of the Exception
	 *@exception  IOException          Description of the Exception
	 *@exception  Exception            Description of the Exception
	 *@since
	 */
	@Deprecated
	public void uploadFile(HttpServletRequest req, User user)
		 throws MraldException, FileUploadException, IOException, Exception
	{
		if ( !FileUpload.isMultipartContent(req) )
		{
			throw new MraldException("The supplied request did not come from a multipart request - " +
				"there can't be any file buried in here.");
		}
		/*
		 *  parse the request
		 */
		DiskFileUpload upload = new DiskFileUpload();
		List items = upload.parseRequest(req);
		Iterator iter = items.iterator();

		String fileName ="";

		while (iter.hasNext())
		{

			FileItem fileSetItem= (FileItem) iter.next();
			String itemName = fileSetItem.getFieldName();


			if (itemName.startsWith("fileName"))
			{
				fileName = fileSetItem.get().toString();

			}
		}

		items.iterator();
		while (iter.hasNext())
		{
			FileItem fileSetItem= (FileItem) iter.next();
			String itemName = fileSetItem.getFieldName();

            if (itemName.startsWith("settings"))
			{

        		String dirFileName = Config.getProperty("BasePath");

            	File fileFinalLocation = new File(dirFileName + "/" + fileName);
        		fileSetItem.write(fileFinalLocation);
			}
		}
	}
}

