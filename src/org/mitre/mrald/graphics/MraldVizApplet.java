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
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JApplet;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.animate.ColorAnimator;
import edu.berkeley.guir.prefuse.action.animate.LocationAnimator;
import edu.berkeley.guir.prefuse.action.filter.GraphFilter;
import edu.berkeley.guir.prefuse.action.filter.TreeFilter;
import edu.berkeley.guir.prefuse.action.filter.WindowedTreeFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.ActivityManager;
import edu.berkeley.guir.prefuse.activity.SlowInSlowOutPacer;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefusex.controls.AnchorUpdateControl;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.FocusControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.SubtreeDragControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.distortion.Distortion;
import edu.berkeley.guir.prefusex.distortion.FisheyeDistortion;
import edu.berkeley.guir.prefusex.force.DragForce;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
import edu.berkeley.guir.prefusex.force.NBodyForce;
import edu.berkeley.guir.prefusex.force.SpringForce;
import edu.berkeley.guir.prefusex.layout.BalloonTreeLayout;
import edu.berkeley.guir.prefusex.layout.ForceDirectedLayout;
import edu.berkeley.guir.prefusex.layout.FruchtermanReingoldLayout;
import edu.berkeley.guir.prefusex.layout.RadialTreeLayout;
import edu.berkeley.guir.prefusex.layout.VerticalTreeLayout;
/**
 * Application demo of a graph visualization using an interactive
 * force-based layout.
 *
 * @version 1.0
 * @author ghamilton*/
public class MraldVizApplet extends JApplet {

    protected ActionList forces;

    /**
     * Initializes the applet.
     */
    public void init() {
         // get the file containing the input data
        String inputFile = this.getParameter("fileName");

        // load the graph file
        //  the applet expects the input data to be at a top-level
        //  in the classpath
        Graph g = null;
        try {
		URL inputUrl = MraldVizApplet.class.getResource("/"+inputFile);
            	g = (new DBGraphReader()).loadGraph(inputUrl);

        URL url = getCodeBase();
        URL baseUrl =  new URL(this.getParameter("baseURL"));

	    // initialize an item registry to store visualized data
        ItemRegistry registry = new ItemRegistry(g);

        // create a display to visualize the contents of the registry
        Display display = new Display(registry);

        // set the size and initial center of the display
        display.setSize(500,500);
        display.pan(250,250);
        VisualColorFunction colorFunc = new VisualColorFunction();

       Color panelColor = colorFunc.getColor("Background");

       display.setBackground(panelColor);

        // associate the renderers with the ItemRegistry

		//registry.setRendererFactory(new DefaultRendererFactory(
            	//    nodeRenderer, edgeRenderer, null));

	    registry.setRendererFactory(initRenderers("label"));

	    // create a filter to map input data into visual items


        ActionList filter = new ActionList(registry);

        // create a force simulator using anti-gravity (n-body force),
        //  a spring force on edges, and a drag (friction) force


        String propsFile = "settings.props";
	    Properties settings = loadProperties(propsFile);


		HashMap<String,Object> attributes = new HashMap<String,Object>();

		attributes.put("BoxColor", new Color(216,210,195));
		attributes.put("PanelColor", new Color(243,242,235));
		attributes.put("URL", url);

		attributes.put("SpringCoefficient", "Contract/Expand");
		//attributes.put("render", ((DefaultRendererFactory)registry.getRendererFactory()).getNodeRenderer() );
		attributes.put("registry", registry );
		attributes.put("display", display);

		attributes.put("colorSet", colorFunc);
		attributes.put("settings", settings);
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
        MraldPanel fpanel = new MraldPanel(attributes);
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

    /**
     * Starts the applet.
     */
    public void start() {
        // start force simulation, this will schedule the
        // force simulator to continuously run, as parameterized
        // in the init() method above
        forces.runNow();
    } //

    protected void createVerticalGraph(Display display, ActionList filter, ZoomControl zc, PanControl pc)
    {

    	display.addMouseListener(pc);
        display.addMouseMotionListener(pc);
        display.addMouseListener(zc);
        display.addMouseMotionListener(zc);
		filter.add(new TreeFilter());

		forces.add(new VerticalTreeLayout());
		forces.add(new RepaintAction());
    }

    protected void createRadialGraph(Display display, ActionList filter, ZoomControl zc, PanControl pc)
    {
    	display.addMouseListener(pc);
        display.addMouseMotionListener(pc);
        display.addMouseListener(zc);
        display.addMouseMotionListener(zc);
		filter.add(new TreeFilter());
		forces.add(new RadialTreeLayout());
		forces.add(new RepaintAction());
    }

    protected void createDistortGraph(Display display, ActionList filter)
    {
    	filter.add(new TreeFilter());

    	Distortion feye = new FisheyeDistortion();
    	forces.add(new RadialTreeLayout());

    	forces.add(feye);
    	forces.add(new RepaintAction());

    	AnchorUpdateControl auc = new AnchorUpdateControl(feye, forces);
    	display.addMouseListener(auc);
        display.addMouseMotionListener(auc);

    }

    protected void createJungGraph(Display display, ActionList filter)
    {
    	filter.add(new TreeFilter());
		FruchtermanReingoldLayout test = new FruchtermanReingoldLayout();
		forces.add(test);
    }

    protected void createForceGraph(Display display, ActionList filter, HashMap<String,Object> attributes, ItemRegistry registry)
    {
    	ForceSimulator fsim = new ForceSimulator();

    	ArrayList<String> displayForces = new ArrayList<String>();
    	displayForces.add("SpringForce");
    	displayForces.add("SpringCoefficient");
    	attributes.put("displayForces", displayForces);
    	fsim.addForce(new NBodyForce(-0.4f, -1f, 0.9f));
    	fsim.addForce(new SpringForce(4E-8f, 75f));
    	fsim.addForce(new DragForce(-0.005f));

   		filter.add(new GraphFilter());
   		forces.add(new ForceDirectedLayout(fsim, false, false));
   		attributes.put("fsim", fsim);
   		forces.add(new RepaintAction());



    }

    protected void createBalloonGraph(Display display, ActionList filter, ItemRegistry registry)
    {

    	filter.add(new WindowedTreeFilter(-4,true));
        filter.add(new BalloonTreeLayout());
        ActionList update = new ActionList(registry);
        update.add(new RepaintAction());

        ActionList animate = new ActionList(registry, 1500, 20);
        animate.setPacingFunction(new SlowInSlowOutPacer());
        animate.add(new LocationAnimator());
        animate.add(new ColorAnimator());
        animate.add(new RepaintAction());
        animate.alwaysRunAfter(filter);
        display.addControlListener(new FocusControl(filter));
        display.addControlListener(new SubtreeDragControl());
        display.addControlListener(new NeighborHighlightControl(update));
    }
    /**
     * Stops the applet.
     */
    public void stop() {
        // stop the force simulator for now.
        forces.cancel();
        ActivityManager.kill();
    } //

    /*Load the properties file. If one doesn't exist create a new one*/
    public Properties loadProperties(String propsFile)
    {
 	   try
 	   {

 	   	   Properties settings = new Properties();

 	   	   //URL propsUrl = DBViewerApplet.class.getResource("/" + propsFile);
 	   		//URL propsUrl = DBViewerApplet.class.getResource("/" + propsFile);
 	   		URL propsUrl = new URL(getCodeBase() + "/" + propsFile);
			//System.out.println("Setting Props File:" + propsUrl);
 	   		if (propsUrl == null)
 		   {
 			   return settings;
 		   }
 		   else
 		   {

 			   URLConnection conn = propsUrl.openConnection();
 			   BufferedInputStream in = new BufferedInputStream(conn.getInputStream());

 			   if (in != null)
 			   {
 				   if (propsFile.indexOf("xml") > 0) //if this is an xml file
 				   {
 					   //System.out.println("Setting Props File: Input not null " + propsUrl);

 					   //settings.loadFromXML(in);

 					   //System.out.println("Setting Props File: Input not null : Settings :" + settings.toString());

 					   return settings;
 				   }
 				   else
 				   {

 					  // System.out.println("Setting Props File: Input not null " + propsUrl);

 					   settings.load(in);

 					   //System.out.println("Setting Props File: Input not null : Settings :" + settings.toString());

 					   return settings;
 				   }
 			   }
 			   else
			   {
				   System.out.println("Setting Props File: Input  null : Could not load Settings");

 				   return settings;
			   }
 		   }
 	   }
 	   catch(Exception e)
 	   {
 		   System.out.println( e.getMessage());
            e.printStackTrace();
            return null;
 	   }

    }

    public boolean saveProperties(String propsFile)
    {
 	   try
 	   {

 		   URL propsUrl = new URL(getCodeBase() +"/" + propsFile);
 		   if (propsUrl == null)
 		   {
 			   return false;
 		   }
 		   else
 		   {
 			   //URLConnection conn = propsUrl.openConnection();
 			   //BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());

 			  return true;
 		   }
 	   }
 	   catch(Exception e)
 	   {
 		   System.out.println( e.getMessage());
            e.printStackTrace();
            return false;
 	   }
    }


    /* Never used!
	private void printPreviewAll()
	{
		Component[] thesePanels = getComponents();
		new PrintPreview(thesePanels, " preview all");
	}*/
    /**
	 *  Description of the Method
	 */
	private DefaultRendererFactory initRenderers(String textField)
	{
		try
		{

			TextItemRenderer nodeRenderer =
			new TextItemRenderer();

			//nodeRenderer.setRadius(20);


		nodeRenderer.setRenderType(TextItemRenderer.RENDER_TYPE_FILL);
		nodeRenderer.setRoundedCorner(8, 8);
//		Font mfont = new Font("SansSerif", Font.BOLD, 10);
		nodeRenderer.setTextAttributeName(textField);

		//DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();
		MraldEdgeRenderer edgeRenderer = new MraldEdgeRenderer();
		//LoopEdgeRenderer edgeRenderer = new LoopEdgeRenderer();

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

	public class TreeMapSizeFunction extends AbstractAction {
        public void run(ItemRegistry registry, double frac) {
            int leafCount = 0;
            Iterator iter = registry.getNodeItems();
            while ( iter.hasNext() ) {
                NodeItem n = (NodeItem)iter.next();
                if ( n.getChildCount() == 0 ) {
                    n.setSize(1.0);
                    NodeItem p = (NodeItem)n.getParent();
                    for (; p!=null; p=(NodeItem)p.getParent())
                        p.setSize(1.0+p.getSize());
                    leafCount++;
                }
            }

            Dimension d = registry.getDisplay(0).getSize();
            double area = d.width*d.height;
            double divisor = leafCount/area;
            iter = registry.getNodeItems();
            while ( iter.hasNext() ) {
                NodeItem n = (NodeItem)iter.next();
                n.setSize(n.getSize()/divisor);
            }

            System.out.println("leafCount = " + leafCount);
        } //
    }

} // end of class ForceApplet

