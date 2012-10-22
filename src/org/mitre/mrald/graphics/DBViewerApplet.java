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
import java.awt.Font;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;

/**
 * Application demo of a graph visualization using an interactive
 * force-based layout.
 *
 * @version 1.0
 * @author ghamilton*/
public class DBViewerApplet extends MraldVizApplet {


    public void init() {
        // get the file containing the input data
       String inputFile = this.getParameter("fileName");


       // load the graph file
       //  the applet expects the input data to be at a top-level
       //  in the classpath
       Graph g = null;
       try {
		URL inputUrl = DBViewerApplet.class.getResource("/"+inputFile);
           	g = (new DBGraphReader()).loadGraph(inputUrl);

//        URL url = getCodeBase();
        URL baseUrl =  new URL(this.getParameter("baseURL"));

	    // initialize an item registry to store visualized data
       ItemRegistry registry = new ItemRegistry(g);

       // create a display to visualize the contents of the registry
       Display display = new Display(registry);

       // set the size and initial center of the display
       display.setSize(500,500);
       display.pan(250,250);

       // associate the renderers with the ItemRegistry

		//registry.setRendererFactory(new DefaultRendererFactory(
           	//    nodeRenderer, edgeRenderer, null));

	    registry.setRendererFactory(initRenderers("label"));

	    // create a filter to map input data into visual items


       ActionList filter = new ActionList(registry);

       // create a force simulator using anti-gravity (n-body force),
       //  a spring force on edges, and a drag (friction) force

		HashMap<String,Object> attributes = new HashMap<String,Object>();

		String propsFile = "settings.props";
	    Properties settings = loadProperties(propsFile);

	    attributes.put("settings", settings);

	    attributes.put("BoxColor", new Color(216,210,195));
		attributes.put("PanelColor", new Color(243,242,235));

		attributes.put("SpringCoefficient", "Contract/Expand");
		//attributes.put("render", ((DefaultRendererFactory)registry.getRendererFactory()).getNodeRenderer() );
		attributes.put("registry", registry );
		attributes.put("display", display);

		VisualColorFunction colorFunc = new VisualColorFunction();

		attributes.put("colorSet", colorFunc);
		attributes.put("baseURL", baseUrl);

       // create a list of actions that
       // (a) use the force simulator to continuously update the
       //     position and speed of items,
       // (b) set item colors, and
       // (c) repaint the display.
       //
       // The -1 indicates that the list should continuously re-run
       //  infinitely, while the 20 tells it to wait at least 20
       //  milliseconds between runs.
       forces = new ActionList(registry,-1,20);
       PanControl pc = new PanControl(false);
      	ZoomControl zc = new ZoomControl(false);

       String displayType = this.getParameter("displayType");
       if (displayType != null)
       {

       	if (displayType.equals("Vertical"))
       	{
       		createVerticalGraph(display,filter, zc, pc);

       	}
       	else if	(displayType.equals("Radial"))
       	{

       		createRadialGraph(display,filter, zc, pc);

       	}
       	else if	(displayType.equals("Jung"))
       	{

       		createJungGraph(display,filter);

       	}
       	else if (displayType.equals("Balloon"))
       	{

       		createBalloonGraph(display,filter,registry);

       	}
       	else if (displayType.equals("Distort"))
       	{

       		createDistortGraph(display, filter);

       	}
       	else if	(displayType.equals("Force"))
       	{
           	createForceGraph(display, filter, attributes,registry);
       	}
       }
       else
        {
       	createForceGraph(display, filter, attributes, registry);

       }
       forces.add(colorFunc);

       attributes.put("forces", forces);
       MraldDBPanel fpanel = new MraldDBPanel(attributes);
       Container c = getContentPane();
       c.setLayout(new BorderLayout());

       c.add(fpanel, BorderLayout.SOUTH);

		// add interactive controls to the display
       //  disable automatic repainting by controls, instead let
       //  the continuously running forces activity handle it

       display.addControlListener(new NeighborHighlightControl());
       display.addControlListener(new DragControl(false, true));
       display.addControlListener(pc);
       display.addControlListener(zc);

       //display.addControlListener(new FocusControl());
       // add the display to the applet
       getContentPane().add(display);

       // filter the input graph into visualized content
       filter.runNow();
	} catch ( Exception e ) {
		System.out.println( e.getMessage());
           e.printStackTrace();
       }
       // now we'll wait until the start method is called by the applet
       // container before starting the force simulation
   } //



    /* PM: Never used!
	private void printPreviewAll()
	{
		Component[] thesePanels = getComponents();
		new PrintPreview(thesePanels, " preview all");
	}*/
    /**
     * Starts the applet.
     */
    public void start() {
        // start force simulation, this will schedule the
        // force simulator to continuously run, as parameterized
        // in the init() method above
        forces.runNow();
    } //

    /**
     * Stops the applet.
     */
    public void stop() {
        // stop the force simulator for now.
        forces.cancel();
    } //

    /**
	 *  Description of the Method
	 */
	private DefaultRendererFactory initRenderers(String textField)
	{
		try
		{
			URL baseUrl =  new URL(this.getParameter("baseURL") + "/graphics");

			if (baseUrl == null)
				System.out.println("DBViewer Applet: codebaseUrl is null");
			else
				System.out.println("DBViewer Applet: codebaseUrl is NOT null " + baseUrl);


		TableNodeRenderer nodeRenderer =
			new TableNodeRenderer(baseUrl);

			//nodeRenderer.setRadius(20);

		//URL baseUrl = getCodeBase() ;
		//nodeRenderer.setCodeBaseUrl(DBViewerApplet.class.getResource("/"));
		nodeRenderer.setCodeBaseUrl(baseUrl);


		nodeRenderer.setRenderType(TableNodeRenderer.RENDER_TYPE_FILL);
		nodeRenderer.setRoundedCorner(8, 8);
		Font mfont = new Font("SansSerif", Font.BOLD, 10);
		nodeRenderer.setTextAttributeName(textField);
		nodeRenderer.addTextAttribute(textField, 3, mfont);
		int noOfEntries = 70;
		for (int i=1; i< noOfEntries + 1; i++)
		{
			nodeRenderer.addTextAttribute("column" + i , 3);
		}
		for (int i=1; i< noOfEntries + 1; i++)
		{
			nodeRenderer.addTextAttribute("type" + i, 3);
		}

		/*for (int i=1; i< noOfEntries + 1; i++)
		{
		nodeRenderer.addTextAttribute("pkey" + i, 3);
		nodeRenderer.addTextAttribute("fkey" + i, 3);
		}*/


//		DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();
		MraldEdgeRenderer edgeRenderer = new MraldEdgeRenderer();

		edgeRenderer.setWeightType(DefaultEdgeRenderer.WEIGHT_TYPE_NONE);
		//edgeRenderer.setEdgeType(DefaultEdgeRenderer.EDGE_TYPE_CURVE);
		edgeRenderer.setEdgeType(DefaultEdgeRenderer.EDGE_TYPE_LINE);

		return new DefaultRendererFactory(
			nodeRenderer, edgeRenderer, null);
		}
		catch(Exception e)
		{

			System.out.println("Exception : INit Renderers: " + e.getMessage());
		}
		return null;
	}

} // end of class ForceApplet

