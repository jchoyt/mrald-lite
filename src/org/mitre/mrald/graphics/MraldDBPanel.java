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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.mitre.mrald.util.MraldException;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
/**
 * A Swing user interface component for configuring the parametes of the
 * Force functions in the given ForceSimulator. Useful for exploring
 * different parameterizations when crafting a visualization.
 *
 * @version 1.0
 * @author ghamilton
 */
public class MraldDBPanel extends MraldPanel implements MraldVisual{


    public MraldDBPanel(HashMap<String,Object> initialVals) throws MraldException {

	super(initialVals);

    } //

    private Box setLegend() throws MraldException
    {
    	try
    	{

		    String[] legend = new String[]{"Primary Key ", "Foreign Key"};
		    String[] pictures = new String[]{"pkey.png", "fkey.png"};
		    Box imageBox = new Box(BoxLayout.Y_AXIS);

		    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		    Color boxColor = Color.WHITE;

		    if (hasAttribute("BoxColor"))
			    boxColor = (Color)getAttribute("BoxColor");

		    imageBox.setBackground(boxColor);
		    URL imageURL = null;

		    System.out.println("Set Legend : Base URL: " + getAttribute("baseURL"));
		    for (int i=0; i < legend.length; i++)
		    {
		    	   //URL imageURL = MraldPanel.class.getResource("/graphics/" + pictures[i]);
		    	if (hasAttribute("baseURL"))
		    		imageURL = new URL(getAttribute("baseURL") + "/graphics/" +  pictures[i]);

		    	ImageIcon keyImage = new ImageIcon(imageURL);

			    JLabel     label = new JLabel(legend[i], keyImage, JLabel.LEFT);
			    label.setPreferredSize(new Dimension(200,20));
			    label.setMaximumSize(new Dimension(200,20));

			    imageBox.add(label);
		    }
		    imageBox.add(Box.createHorizontalStrut(10));
		    imageBox.add(Box.createHorizontalGlue());
		    imageBox.setBorder(BorderFactory.createTitledBorder("Legend"));
		    return imageBox;
    	}catch(Exception e)
    	{
    		throw new MraldException(e.getMessage());
    	}
    }

    protected void initUI() throws MraldException{


    	setAttribute("showDetails", new ShowDetails());
    	super.initUI();

    	//Add the Legend
    	Box imageBox = setLegend();
    	//this.add(imageBox);

    	addToTabPanel(1, imageBox);

    } //


    private JButton getDetailButton() throws MraldException
    {
	   JButton but = new JButton("Show Table");
	    but.putClientProperty("registry", getAttribute("registry"));
	    but.addActionListener((ShowDetails)getAttribute("showDetails"));
	    return but;
    }

    protected Box getToolsBox() throws MraldException
    {
    	Box a = super.getToolsBox();
    	JButton detailButton= getDetailButton();
    	a.add(detailButton);
        return a;
    }

    private class ShowDetails extends AbstractAction{
        public void actionPerformed(ActionEvent arg0) {
            JButton but = (JButton)arg0.getSource();
	    ItemRegistry registry = (ItemRegistry)but.getClientProperty("registry");
            if (registry != null)
	    {
		     TableNodeRenderer rend = (TableNodeRenderer)((DefaultRendererFactory)registry.getRendererFactory()).getNodeRenderer();
		     rend.setTableOnly(!rend.getTableOnly());
		    if (!rend.getTableOnly())
			    but.setText("Show Table");
		    else
			    but.setText("Show Details");
	    }
	    else
		    System.out.println("Could not find property registry");
        }
    }
} // end of class ForcePanel
