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
package org.mitre.mrald.graphics;
import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MraldException;

/**
 *@author    ghamilton
 *@created    April 20, 2005
 */

public class AppletDBViewer extends JApplet
{

   	BorderLayout borderLayout1 = new BorderLayout();

	static DBDemo dbDemo = new DBDemo();
	JMenuBar jMenuBar1 = new JMenuBar();
	JMenu jMenu1 = new JMenu();
	JMenuItem jMenuItem1 = new JMenuItem();
	JButton jButton = new JButton("Freeze");
	static String configFile = null;
	static String file=null;

	URL url = null;
	String fileName=null;

	/**
	 *  Description of the Method
	 */
	public void initApp()
	{
		try
		{
			getParams();
		}
		catch(MraldException me)
		{
			System.err.print("ERROR in GetParams " + me.getMessage());

		}
		JPanel contentPane = new JPanel(borderLayout1);


		setJMenuBar(jMenuBar1);
		jMenu1.setText("File");
		jMenuItem1.setText("Exit");
		jMenuBar1.add(jMenu1);
		jMenu1.add(jMenuItem1);
		this.add(jButton);
		//setSize(800, 600);
		createDBViewer(configFile, contentPane);


	        if (url != null)
			dbDemo.runNow(url);
	}


	/**
	 *  Description of the Method
	 */
	public void init()
	{
		try
		{
			getParams();
		}
		catch(MraldException me)
		{
			System.err.print("ERROR in GetParams " + me.getMessage());

		}
		JPanel contentPane = new JPanel(borderLayout1);


		createDBViewer(configFile, contentPane);


	        if (url != null)
			dbDemo.runNow(url);
		else
			dbDemo.runNow(configFile);
	}
/**
     * Starts the applet.
     */
    public void start() {
        // start force simulation, this will schedule the
        // force simulator to continuously run, as parameterized
        // in the init() method above
       //dbDemo.runNow(configFile);
    } //

    /**
     * Stops the applet.
     */
    public void stop() {
        // stop the force simulator for now.
         dbDemo.cancel();
    } //



	/**
	 *  Gets the params attribute of the AppletVolumeViewer object
	 */
	public void getParams() throws MraldException
	{
		try
		{
			configFile = getParameter("configFile");
			fileName = getParameter("fileName");
			url = new URL (fileName );
		}
		catch(java.net.MalformedURLException me)
		{
			throw new MraldException(me.getMessage());
		}
	}


	private  void createDBViewer(String configFile,JPanel contentPane)
	{
		try
		{
		setContentPane(dbDemo);
		}
		catch (Exception e)
		{
			System.err.print("ERROR in initialization " + e.getMessage());
			//throw e;
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  args  Description of the Parameter
	 */
	public static void main(String args[])
	{
		if (args != null)
			if (args[0] != null)
				 configFile = args[0];
		if (args.length == 2)
				 file = args[1];

		System.out.print( "AppletDBViewer : Config file "+ configFile);

		Config.init(configFile);

		//MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "AppletDBViewer: Initialized Applet");

		java.security.AccessController.doPrivileged(
			new java.security.PrivilegedAction<Object>()
			{
				public Object run()
				{
					//MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "AppletDBViewer: Initialized Applet");

					String[] argsLocal;

					if (file != null)
						argsLocal= new String[]{configFile, file};
					else
						argsLocal= new String[]{configFile};

					DBDemo.main(argsLocal);
					//createDBViewer();

					return null;
				}
			});

	}
}
