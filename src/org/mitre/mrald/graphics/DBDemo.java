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
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JFrame;

import org.mitre.mrald.util.MraldOutFile;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.filter.GraphFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.Activity;
import edu.berkeley.guir.prefuse.event.ControlAdapter;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.FocusControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.force.DragForce;
import edu.berkeley.guir.prefusex.force.ForcePanel;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
import edu.berkeley.guir.prefusex.force.NBodyForce;
import edu.berkeley.guir.prefusex.force.SpringForce;
import edu.berkeley.guir.prefusex.layout.ForceDirectedLayout;
/**
 *  Application demo of a graph visualization using an interactive force-based
 *  layout.
 *
 *@author     <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 *@created    October 7, 2004
 *@version    1.0
 */
public class DBDemo extends Display
{

	private JFrame frame;
	private ForcePanel fpanel;

	private ForceSimulator m_fsim;
	private String m_textField;
	private ItemRegistry m_registry;
	private Activity m_actionList;

//	private  FreezeAction freezeAction = new FreezeAction();
//	private Font frameCountFont = new Font("SansSerif", Font.PLAIN, 14);


	/**
	 *  Constructor for the ForceDemo object
	 *
	 *@param  g     Description of the Parameter
	 *@param  fsim  Description of the Parameter
	 */
	public DBDemo()
	{
		this.addControlListener(new NeighborHighlightControl());
		this.addControlListener(new DragControl(false));
		this.addControlListener(new FocusControl(0));
		this.addControlListener(new MouseOverControl());
		this.addControlListener(new PanControl(false));
		this.addControlListener(new ZoomControl(false));
		pan(350, 350);
	}


	/**
	 *  Constructor for the ForceDemo object
	 *
	 *@param  g     Description of the Parameter
	 *@param  fsim  Description of the Parameter
	 */
	public DBDemo(Graph g, ForceSimulator fsim)
	{
		this(g, fsim, "label");
	}
	//


	/**
	 *  Constructor for the ForceDemo object
	 *
	 *@param  g          Description of the Parameter
	 *@param  fsim       Description of the Parameter
	 *@param  textField  Description of the Parameter
	 */
	public DBDemo(Graph g, ForceSimulator fsim, String textField)
	{
		// set up component first
		m_fsim = fsim;
		m_textField = textField;
		m_registry = new ItemRegistry(g);
		this.setItemRegistry(m_registry);
		initRenderers();
		m_actionList = initActionList();
		setSize(900, 900);
		pan(350, 350);
		this.addControlListener(new NeighborHighlightControl());
		this.addControlListener(new DragControl(false));
		this.addControlListener(new FocusControl(0));
		this.addControlListener(new MouseOverControl());
		this.addControlListener(new PanControl(false));
		this.addControlListener(new ZoomControl(false));

	}
	//

	/*public ActionListener getFreezeListener()
	{
		return (ActionListener)freezeAction;
	}
	*/

	/**
	 *  Description of the Method
	 */
	public void runDemoApp()
	{
		fpanel = new ForcePanel(m_fsim);
		frame = new JFrame("Force Simulator Demo");
		Container c = frame.getContentPane();

		c.setLayout(new BorderLayout());
		c.add(this, BorderLayout.CENTER);
		c.add(fpanel, BorderLayout.EAST);


		//bPanel = new MraldPanel(m_fsim);
		//c.add(bPanel, BorderLayout.WEST);


		frame.addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			});
		frame.addComponentListener(
			new ComponentAdapter()
			{
				public void componentResized(ComponentEvent e)
				{
					Dimension d = frame.getSize();
					Dimension p = fpanel.getSize();
					//Dimension b = bPanel.getSize();
					Insets in = frame.getInsets();
					DBDemo.this.setSize(d.width - in.left - in.right-p.width ,
						d.height - in.top - in.bottom);

				}
				//

			});
		frame.pack();
		frame.setVisible(true);

		// start force simulation
		m_actionList.runNow();
	}
	//


	/**
	 *  Description of the Method
	 */
	public void runDemo()
	{
		fpanel = new ForcePanel(m_fsim);
		this.add(fpanel, BorderLayout.WEST);
		//this.setLayout(new BorderLayout());
		m_actionList.runNow();

	}
	//


	/**
	 *  Description of the Method
	 */
	private DefaultRendererFactory initRenderers(boolean returnVal)
	{
		try
		{
			System.out.println("  ********InitRenderer**********");
		TableNodeRenderer nodeRenderer =
			new TableNodeRenderer();

		nodeRenderer.logState("Table Node rendered");

		nodeRenderer.setRenderType(TableNodeRenderer.RENDER_TYPE_FILL);
		nodeRenderer.setRoundedCorner(8, 8);
		Font mfont = new Font("SansSerif", Font.BOLD, 12);
		nodeRenderer.setTextAttributeName(m_textField);
		nodeRenderer.addTextAttribute(m_textField, 3, mfont);
		int noOfEntries = 35;
		for (int i=1; i< noOfEntries + 1; i++)
		{
			nodeRenderer.addTextAttribute("column" + i , 3);
		}
		for (int i=1; i< noOfEntries + 1; i++)
		{
			nodeRenderer.addTextAttribute("type" + i, 3);
		}

		nodeRenderer.addTextAttribute("pkey", 3);
		nodeRenderer.addTextAttribute("fkey", 3);

		//nodeRenderer.logState("Init renderers. set TextField " + m_textField  );
		System.out.println("Init Rendered: About to return renderers");
		DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();

		return new DefaultRendererFactory(
			nodeRenderer, edgeRenderer, null);
		}
		catch(Exception e)
		{
			MraldOutFile.logToFile( e );
			System.out.println("Exception : INit Renderers: " + e.getMessage());
		}
		return null;
	}
	//

	/**
	 *  Description of the Method
	 */
	private void initRenderers()
	{
		try
		{
			System.out.println("  ********InitRenderer**********");
		TableNodeRenderer nodeRenderer =
			new TableNodeRenderer();

		nodeRenderer.logState("Table Node rendered");

		nodeRenderer.setRenderType(TableNodeRenderer.RENDER_TYPE_FILL);
		nodeRenderer.setRoundedCorner(8, 8);
		Font mfont = new Font("SansSerif", Font.BOLD, 12);
		nodeRenderer.setTextAttributeName(m_textField);
		nodeRenderer.addTextAttribute(m_textField, 3, mfont);
		int noOfEntries = 35;
		for (int i=1; i< noOfEntries + 1; i++)
		{
			nodeRenderer.addTextAttribute("column" + i , 3);
		}
		for (int i=1; i< noOfEntries + 1; i++)
		{
			nodeRenderer.addTextAttribute("type" + i, 3);
		}

		nodeRenderer.addTextAttribute("pkey", 3);
		nodeRenderer.addTextAttribute("fkey", 3);


		//nodeRenderer.logState("Init renderers. set TextField " + m_textField  );
		System.out.println("Init Rendered: About to set renderers");
		DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();
		m_registry.setRendererFactory(new DefaultRendererFactory(
			nodeRenderer, edgeRenderer, null));
		System.out.println("Init Rendered: Set renderers successfully");

		}
		catch(Exception e)
		{
			MraldOutFile.logToFile( e );
			System.out.println("Exception : INit Renderers: " + e.getMessage());
		}
	}
	//


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	private ActionList initActionList()
	{
		ActionList actionList = new ActionList(m_registry, -1, 20);
		actionList.add(new GraphFilter());
		actionList.add(new ForceDirectedLayout(m_fsim, false, false));
		actionList.add(new DemoColorFunction());
		actionList.add(new RepaintAction());
		actionList.add(new FreezeAction());
		return actionList;
	}
	//

	public void runNow(URL inputUrl)
	{
		Graph g;
		try
		{
			g = (new DBGraphReader()).loadGraph(inputUrl);

			ForceSimulator fsim = new ForceSimulator();
			fsim.addForce(new NBodyForce(-0.4f, -1f, 0.9f));
			fsim.addForce(new SpringForce(4E-6f, 75f));
			fsim.addForce(new DragForce(-0.005f));

			m_fsim = fsim;

			fpanel = new ForcePanel(m_fsim);
			 this.setLayout(new BorderLayout());

			m_registry = new ItemRegistry(g);
			this.setItemRegistry(m_registry);
			m_registry.setRendererFactory(initRenderers(true));

			//this.setItemRegistry(m_registry);

			m_actionList = initActionList();
			m_actionList.runNow();

		} catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		System.out.println("Visualizing Graph: Using URL: "
			 + g.getNodeCount() + " nodes, " + g.getEdgeCount() + " edges");



		//runDemo();
	}

	public void runNow(String file)
	{
		Graph g;

		ForceSimulator fsim = new ForceSimulator();
		fsim.addForce(new NBodyForce(-0.4f, -1f, 0.9f));
		fsim.addForce(new SpringForce(4E-6f, 75f));
		fsim.addForce(new DragForce(-0.005f));

		m_fsim = fsim;
		try
		{
			g = (new DBGraphReader()).loadGraph(file);
			m_registry = new ItemRegistry(g);
			this.setItemRegistry(m_registry);
			initRenderers();
			m_actionList = initActionList();

		} catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		System.out.println("Visualizing Graph: using file: "
			 + g.getNodeCount() + " nodes, " + g.getEdgeCount() + " edges");

		runDemoApp();
	}


	public void cancel()
	{

	}


	public void freezeAll()
	{

	}
	/**
	 *  Description of the Method
	 *
	 *@param  argv  Description of the Parameter
	 */
	public static void main(String argv[])
	{
		//String file = (argv.length==0 ? "etc/friendster.xml" : argv[0]);
		//String file = (argv.length==0 ? "etc/terror.xml" : argv[0]);

		String file =null;
		if (argv != null)
			if (argv.length == 2)
				 file = argv[1];

		System.out.println("NO OF ARGS : " + argv.length);

		Graph g;
		try
		{
			if (file != null)
				g = (new DBGraphReader()).loadGraph(file);
			else
				g = (new DBGraphReader()).loadGraph();
		} catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		//Graph g = GraphLib.getGrid(15,15);

		System.out.println("Visualizing Graph in Main: "
			 + g.getNodeCount() + " nodes, " + g.getEdgeCount() + " edges");

		ForceSimulator fsim = new ForceSimulator();
		fsim.addForce(new NBodyForce(-0.4f, -1f, 0.9f));
		fsim.addForce(new SpringForce(4E-8f, 75f));
		fsim.addForce(new DragForce(-0.005f));

		DBDemo fdemo = new DBDemo(g, fsim);
		fdemo.runDemoApp();

	}
	//


	/**
	 *  Description of the Class
	 *
	 *@author     gail
	 *@created    October 7, 2004
	 */
	public class DemoColorFunction extends ColorFunction
	{
		private Color pastelRed = new Color(255, 125, 125);
		private Color pastelOrange = new Color(255, 200, 125);
		private Color lightGray = new Color(220, 220, 255);


		/**
		 *  Gets the color attribute of the DemoColorFunction object
		 *
		 *@param  item  Description of the Parameter
		 *@return       The color value
		 */
		public Paint getColor(VisualItem item)
		{
			if (item instanceof EdgeItem)
			{
				if (item.isHighlighted())
				{
					return pastelOrange;
				} else
				{
					return Color.LIGHT_GRAY;
				}
			} else
			{
				return Color.BLACK;
			}
		}
		//


		/**
		 *  Gets the fillColor attribute of the DemoColorFunction object
		 *
		 *@param  item  Description of the Parameter
		 *@return       The fillColor value
		 */
		public Paint getFillColor(VisualItem item)
		{
			if (item.isHighlighted())
			{
				return pastelOrange;
			} else if (item instanceof NodeItem)
			{
				if (item.isFixed())
				{
					return pastelRed;
				} else
				{
					return lightGray;
				}
			} else
			{
				return Color.BLACK;
			}
		}
		//
	}
	//




	/**
	 *  Tags and fixes the node under the mouse pointer.
	 *
	 *@author     gail
	 *@created    October 7, 2004
	 */
	public class MouseOverControl extends ControlAdapter
	{

		/**
		 *  Description of the Method
		 *
		 *@param  item  Description of the Parameter
		 *@param  e     Description of the Parameter
		 */
		public void itemEntered(VisualItem item, MouseEvent e)
		{
			((Display) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			item.setFixed(true);
		}
		//


		/**
		 *  Description of the Method
		 *
		 *@param  item  Description of the Parameter
		 *@param  e     Description of the Parameter
		 */
		public void itemExited(VisualItem item, MouseEvent e)
		{
			((Display) e.getSource()).setCursor(Cursor.getDefaultCursor());
			item.setFixed(false);
		}
		//


		/**
		 *  Description of the Method
		 *
		 *@param  item  Description of the Parameter
		 *@param  e     Description of the Parameter
		 */
		public void itemReleased(VisualItem item, MouseEvent e)
		{
			item.setFixed(false);
		}
		//

	}


}

