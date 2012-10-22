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
import java.awt.Paint;

import javax.swing.JApplet;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.filter.GraphFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.force.DragForce;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
import edu.berkeley.guir.prefusex.force.NBodyForce;
import edu.berkeley.guir.prefusex.force.SpringForce;
import edu.berkeley.guir.prefusex.layout.ForceDirectedLayout;

/**
 * Application demo of a graph visualization using an interactive
 * force-based layout.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ForceDBApplet extends JApplet {

    private ActionList forces;

    /**
     * Initializes the applet.
     */
    public void init() {
        // get which text field to display on nodes
        String textField = this.getParameter("label");
        // get the file containing the input data

        // load the graph file
        //  the applet expects the input data to be at a top-level
        //  in the classpath
        Graph g = null;
        try {
		String inputFile="";
             g = (new DBGraphReader()).loadGraph(inputFile);
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        // initialize an item registry to store visualized data
        ItemRegistry registry = new ItemRegistry(g);

        // create a display to visualize the contents of the registry
        Display display = new Display(registry);

        // set the size and initial center of the display
        display.setSize(500,500);
        display.pan(250,250);

        // initialize the renderers that draw onscreen items
        TextItemRenderer nodeRenderer = new TextItemRenderer();
        nodeRenderer.setRenderType(TextItemRenderer.RENDER_TYPE_FILL);
        nodeRenderer.setRoundedCorner(8,8);
        nodeRenderer.setTextAttributeName(textField);

        DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();

        // associate the renderers with the ItemRegistry
        registry.setRendererFactory(new DefaultRendererFactory(
                nodeRenderer, edgeRenderer, null));

        // create a filter to map input data into visual items
        ActionList filter = new ActionList(registry);
        filter.add(new GraphFilter());

        // create a force simulator using anti-gravity (n-body force),
        //  a spring force on edges, and a drag (friction) force
        ForceSimulator fsim = new ForceSimulator();
        fsim.addForce(new NBodyForce(-0.4f, -1f, 0.9f));
        fsim.addForce(new SpringForce(2E-5f, 75f));
        fsim.addForce(new DragForce(-0.01f));

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
        forces.add(new ForceDirectedLayout(fsim, false, false));
        forces.add(new DemoColorFunction());
        forces.add(new RepaintAction());

        // add interactive controls to the display
        //  disable automatic repainting by controls, instead let
        //  the continuously running forces activity handle it
        display.addControlListener(new NeighborHighlightControl());
        display.addControlListener(new DragControl(false, true));
        display.addControlListener(new PanControl(false));
        display.addControlListener(new ZoomControl(false));

        // add the display to the applet
        getContentPane().add(display);

        // filter the input graph into visualized content
        filter.runNow();

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

    /**
     * Stops the applet.
     */
    public void stop() {
        // stop the force simulator for now.
        forces.cancel();
    } //

    /**
     * A custom color function for setting the color of on-screen items.
     */
    public class DemoColorFunction extends ColorFunction {
        private Color pastelRed = new Color(255,125,125);
        private Color pastelOrange = new Color(255,200,125);
        private Color lightGray = new Color(220,220,255);

        public Paint getColor(VisualItem item) {
            if ( item instanceof EdgeItem ) {
                if ( item.isHighlighted() )
                    return pastelOrange;
                else
                    return Color.LIGHT_GRAY;
            } else {
                return Color.BLACK;
            }
        } //

        public Paint getFillColor(VisualItem item) {
            if ( item.isHighlighted() )
                return pastelOrange;
            else if ( item instanceof NodeItem ) {
                if ( item.isFixed() )
                    return pastelRed;
                else
                    return lightGray;
            } else {
                return Color.BLACK;
            }
        } //

    } // end of inner class DemoColorFunction

} // end of class ForceApplet

