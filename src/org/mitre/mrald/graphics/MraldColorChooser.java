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
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MraldColorChooser extends JPanel
                              implements ChangeListener {

    protected JColorChooser tcc;

    private static HashMap<String,Color> coloringItems = new HashMap<String,Color>();

    public MraldColorChooser(HashMap<String,Color> coloringItems) {
        super(new BorderLayout());

	setColoringItems(coloringItems);
        //Set up color chooser for setting text color
        tcc = new JColorChooser();
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder(
                                             "Choose Color"));

        add(tcc, BorderLayout.PAGE_END);
	add(setChooserPanel(), BorderLayout.CENTER);
    }

    public void setColoringItems(HashMap<String,Color> coloringItems)
    {
	    MraldColorChooser.coloringItems = coloringItems;
    }

    public MraldColorChooser(Color startColor, HashMap<String,Color> coloringItems) {
        super(new BorderLayout());

	setColoringItems(coloringItems);

        //Set up color chooser for setting text color
        tcc = new JColorChooser(startColor);
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder(
                                             "Choose Color"));


        add(tcc, BorderLayout.PAGE_END);
	add(setChooserPanel(), BorderLayout.CENTER);
    }

    public void stateChanged(ChangeEvent e) {
    	// This line of code is rather useless and odd. (PM)
//        Color newColor = tcc.getColor();
    }

    private JPanel setChooserPanel()
    {
	    JPanel bannerPanel = new JPanel(new BorderLayout());
           // Pallet panelBox = new Box(BoxLayout.X_AXIS);
	   int rows = 1;
	   MraldPalette panelBox = new MraldPalette( coloringItems.size(), rows, coloringItems);
	    /*for (int i=0; i< coloringItems.size(); i++)
	    {
		    Box itemBox = new Box(BoxLayout.Y_AXIS);

		    JLabel banner = new JLabel(coloringItems.get(i).toString());
		    Box.Filler showBox = new Box.Filler(new Dimension(20, 10), new Dimension(20, 10), new Dimension(20, 10));

		    showBox .setBackground(Color.green);
		    itemBox.add(showBox);
		    banner.setForeground(Color.black);
		    banner.setBackground(Color.blue);
		    banner.setOpaque(true);
		    banner.setFont(new Font("SansSerif", Font.BOLD, 10));
		    banner.setPreferredSize(new Dimension(50, 20));
		    itemBox.add(banner);
		    itemBox.setBorder(BorderFactory.createTitledBorder(coloringItems.get(i).toString()));
		    panelBox.add(itemBox, BoxLayout.X_AXIS);
	    }*/
	 bannerPanel.add(panelBox, BoxLayout.X_AXIS);

	bannerPanel.setBorder(BorderFactory.createTitledBorder("Choose item to Change Color"));

	return bannerPanel;
    }
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("MRALD Color Chooser");

        //Create and set up the content pane.
        JComponent newContentPane = new MraldColorChooser(coloringItems);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }



}

