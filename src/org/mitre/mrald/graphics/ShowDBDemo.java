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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

import javax.swing.JFrame;

import org.mitre.mrald.util.MraldOutFile;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.filter.TreeFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.MultiLineTextItemRenderer;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.berkeley.guir.prefuse.util.ColorLib;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.FocusControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.layout.VerticalTreeLayout;
/**
 *  Creates a new graph and draws it on the screen.
 *
 *@author     gail
 *@created    October 6, 2004
 */
public class ShowDBDemo extends JFrame
{

	private ItemRegistry registry;
	private ActionList actions;
	private Graph graph;

	/**
	 *  Description of the Field
	 */
	public final static String GRAPH_LATTICE = "etc/lattice.xml";

	private ActionList layout, update;//, animate;


	/**
	 *  Constructor for the ShowGraph object
	 */
	public ShowDBDemo()
	{
		super("ShowGraph");
		try
		{
			// creates a new graph
			//Graph g = GraphLib.getClique(5);
			String inputFile = GRAPH_LATTICE;
			//XMLGraphReader gr = new XMLGraphReader();
			graph = (new DBGraphReader()).loadGraph(inputFile);

			// create display and filter

			registry = new ItemRegistry(graph);


			initRenderers();
				// create a new item registry
			//  the item registry stores all the visual
			//  representations of different graph elements

			//registry = new ItemRegistry(g);

			// create a new display component to show the data
			Display display = new Display(registry);
			display.setSize(800, 800);

			// lets users drag nodes around on screen
			display.addControlListener(new DragControl());
			display.addControlListener(new FocusControl());
			update = new ActionList(registry);

			update.add(new DemoColorFunction(3));
			update.add(new RepaintAction());
			display.addControlListener(new NeighborHighlightControl(update));
			display.addControlListener(new PanControl(false));
			display.addControlListener(new ZoomControl(false));

			//display.setBackground(new Color(204, 204, 255) );

			display.setBackground(Color.LIGHT_GRAY );

			// create a new action list that
			// (a) filters visual representations from the original graph
			// (b) performs a random layout of graph nodes
			// (c) calls repaint on displays so that we can see the result

			layout = new ActionList(registry);

			//layout.add(new GridLayout());
			layout.add(new VerticalTreeLayout());
			layout.add(new DemoColorFunction(3));
			layout.add(new RepaintAction());

			actions = new ActionList(registry);
			actions.add(new TreeFilter());
			actions.add(new DemoColorFunction(3));
			actions.add(new RepaintAction());

			// set up this JFrame
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			getContentPane().add(display);
			pack();
			setVisible(true);

			// now execute the actions to visualize the graph
			actions.runNow();
			layout.runNow();


		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*private void keyordSearch()
	{
		KeywordSearchFocusSet searchSet = new KeywordSearchFocusSet();
		//FocusManager fmanager = registry.getFocusManager();
		//fmanager.putFocusSet(FocusManager.SEARCH_KEY, searchSet);
	}*/
	/**
	 *  Description of the Method
	 */
	private void initRenderers()
	{
		try
		{
		//TableNodeRenderer nodeRenderer =
			//new TableNodeRenderer();

		//nodeRenderer.logState("Table Node rendered");

		//nodeRenderer.setRenderType(TableNodeRenderer.RENDER_TYPE_FILL);

		MultiLineTextItemRenderer nodeRenderer =
			new MultiLineTextItemRenderer();

		//nodeRenderer.logState("Table Node rendered");

		nodeRenderer.setRenderType(TableNodeRenderer.RENDER_TYPE_FILL);

		nodeRenderer.setRoundedCorner(8, 8);

		///nodeRenderer.setTextAttributeName(m_textField);
		nodeRenderer.addTextAttribute("label", 3);
		nodeRenderer.addTextAttribute("column1", 3);
		nodeRenderer.addTextAttribute("column2", 3);
		nodeRenderer.addTextAttribute("column3", 3);
		nodeRenderer.addTextAttribute("column4", 3);
		nodeRenderer.addTextAttribute("column5", 3);
		nodeRenderer.addTextAttribute("column6", 3);
		nodeRenderer.addTextAttribute("column7", 3);
		nodeRenderer.addTextAttribute("column8", 3);
		nodeRenderer.addTextAttribute("column9", 3);
		nodeRenderer.addTextAttribute("column10", 3);
		nodeRenderer.addTextAttribute("column11", 3);
		nodeRenderer.addTextAttribute("column12", 3);
		nodeRenderer.addTextAttribute("column13", 3);
		nodeRenderer.addTextAttribute("column14", 3);
		nodeRenderer.addTextAttribute("column15", 3);


		//nodeRenderer.logState("Init renderers. set TextField " + m_textField  );

		Renderer edgeRenderer =
				new DefaultEdgeRenderer()
				{
					protected int getLineWidth(VisualItem item)
					{
						String w = item.getAttribute("weight");
						if (w != null)
						{
							try
							{
								return Integer.parseInt(w);
							} catch (Exception e)
							{}
						}
						return m_width;
					}
					//
				};

		registry.setRendererFactory(new DefaultRendererFactory(
			nodeRenderer, edgeRenderer, null));

		}
		catch(Exception e)
		{
			MraldOutFile.logToFile( e );

		}
	}

	/**
	 *  The main program for the ShowGraph class
	 *
	 *@param  args  The command line arguments
	 */
	public static void main(String[] args)
	{
		new ShowDBDemo();
	}


	/**
	 *  Description of the Class
	 *
	 *@author     gail
	 *@created    October 6, 2004LIGHT_GRAY
	 */
	public class DemoColorFunction extends ColorFunction
	{
		private Color graphEdgeColor = Color.DARK_GRAY;
//		private Color highlightColor = new Color(50, 50, 255);
		private Color nodeColors[];
		private Color edgeColors[];


		/**
		 *  Constructor for the DemoColorFunction object
		 *
		 *@param  thresh  Description of the Parameter
		 */
		public DemoColorFunction(int thresh)
		{
			nodeColors = new Color[thresh];
			edgeColors = new Color[thresh];
			for (int i = 0; i < thresh; i++)
			{
				double frac = i / ((double) thresh);
				nodeColors[i] = ColorLib.getIntermediateColor(Color.RED, Color.BLACK, frac);
				edgeColors[i] = ColorLib.getIntermediateColor(Color.RED, Color.BLACK, frac);
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
			if (item instanceof NodeItem)
			{
				//Color nodeEndCol = new Color(5,5,255);
				Color nodeEndCol = Color.cyan;
				Color nodeStartCol = new Color(255, 153, 0);
				//int b = (int)item.getSize();
				int b = 600;

//				int x =(int)item.getX() ;
//				int y =(int)item.getY() ;
				GradientPaint nodeCol = new GradientPaint( 2,2, nodeStartCol, b-2, b-2, nodeEndCol);

				return nodeCol;
			} else if (item instanceof AggregateItem)
			{
				return Color.LIGHT_GRAY;
			} else if (item instanceof EdgeItem)
			{
				return getColor(item);
			} else
			{
				return Color.BLACK;
			}
		}
		//


		/**
		 *  Gets the color attribute of the DemoColorFunction object
		 *
		 *@param  item  Description of the Parameter
		 *@return       The color value
		 */
		public Paint getColor(VisualItem item)
		{
			if (item.isHighlighted())
			{
//				Color nodeEndCol = new Color(255,255,255);
				Color nodeStartCol = new Color(204, 204, 255);
//				int b = 600;//(int)item.getSize();
//				GradientPaint nodeCol = new GradientPaint(2,2, nodeStartCol, b-2, b-2, nodeEndCol);

				return nodeStartCol;
				//return nodeCol;
			} else if (item instanceof NodeItem)
			{
				int d = ((NodeItem) item).getDepth();
				return nodeColors[Math.min(d, nodeColors.length - 1)];
			} else if (item instanceof EdgeItem)
			{
				EdgeItem e = (EdgeItem) item;
				if (e.isTreeEdge())
				{
					int d;
					int d1;
					int d2;
					d1 = ((NodeItem) e.getFirstNode()).getDepth();
					d2 = ((NodeItem) e.getSecondNode()).getDepth();
					d = Math.max(d1, d2);
					return edgeColors[Math.min(d, edgeColors.length - 1)];
				} else
				{
					return graphEdgeColor;
				}
			} else
			{
				return Color.BLACK;
			}
		}
		//



		//
	}
	// end of inner class DemoColorFunction

}

