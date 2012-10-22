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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

class MraldPalette extends JPanel implements PropertyChangeListener{


	    private Color backgroundCol = null;
	    private HashMap<String,Color> backupSetColors = new HashMap<String,Color>();

	    public MraldPalette(Color background, int columns, int rows, HashMap<String,Color> setColors) {
	    	this(columns, rows, setColors);
	    	setBackground( background);
		    backgroundCol = background;

	    }

	    public void setBackgroundCol(HashMap setColors) {

	    	Iterator cols = setColors.keySet().iterator();

	    	while(cols.hasNext())
		    {

			   String label = cols.next().toString();
			   if (label.equals("Background_color"))
			   backgroundCol = (Color)setColors.get(label);

			   if (backgroundCol != null)
				   setBackground(backgroundCol);

		    }

	    }
	    public MraldPalette( int columns, int rows, HashMap<String,Color> setColors) {


	    	 //setBackgroundCol(setColors);
		     backupSetColors = new HashMap<String,Color>(setColors);

		     Iterator cols = setColors.keySet().iterator();
		     setBorder( BorderFactory.createTitledBorder("Color Palette"));

		     Box v = new Box(BoxLayout.X_AXIS);
		     v.add(Box.createHorizontalGlue());
		    while(cols.hasNext())
		    {

			   String label = cols.next().toString();
			   ColorPatch colPatch = new ColorPatch(this, setColors.get(label), label)  ;

				   //gridBag.setConstraints(colPatch,c);
			   if (backgroundCol != null)
				   colPatch.setBackground(backgroundCol);
			   v.add(colPatch);

		    }

		    this.add(v);
	    }

	    public void setColor(String type, Color color)
	    {
		    Color oldColor = backupSetColors.get( type);
		    backupSetColors.put( type, color);
		    firePropertyChange(type, oldColor, color);
	    }

	     public Color getColor(String type)
	    {
		    return backupSetColors.get( type);

	    }
	     public void stateChanged(ChangeEvent e)
	     {
		     System.out.println("State Changed");
	     }

	     public void propertyChange(PropertyChangeEvent e)
	    {
		    System.out.println("test");
	    }
    }

     class ColorChange implements PropertyChangeListener{

	    MraldPalette palette=null;

	    public ColorChange(MraldPalette parent)
	    {
		    super();
		    this.palette = parent;
		    System.out.println("In ColorChange");
	    }

	    public void propertyChange(PropertyChangeEvent e)
	    {

	    		if (e.getPropertyName().equals("background"))
		{

			JButton jBut = (JButton)e.getSource();

			if (jBut != null)
			{
				String type = (String) jBut.getClientProperty("name");

				Color newColor = (Color)e.getNewValue();

				if (palette != null)
				{

					palette.setColor( type, newColor);
				}
			}
		}


	    }

    }


  class ColorPatch extends JPanel  {

  public Color selectedColor;
//  private MraldPalette parent = null;

  public ColorPatch(MraldPalette parent, Color color, String label) {
    // add border and set background color ...

      //Box box = new Box(BoxLayout.Y_AXIS);


      JButton but = new JButton(label);
      //this.add(new JLabel( label));
      but.putClientProperty("name", label);
      but.setMaximumSize( new Dimension(60,30));
      this.add(but);

      but.setBackground(color);
      repaint();
      setPreferredSize( new Dimension(70, 30));
      setMaximumSize( new Dimension(70, 30));
      setMinimumSize( new Dimension(70, 30));

      but.addPropertyChangeListener("background", new ColorChange(parent));

    but.addMouseListener(new MouseAdapter()
    {

	    public void mousePressed(MouseEvent e)
	    {
	    	JButton jbut = (JButton)e.getSource();

		    selectedColor = JColorChooser.showDialog(
		    		ColorPatch.this, // parent comp
				"Pick A Color",  // dialog title
				jbut.getBackground()); // initial color

		  if(selectedColor != null) {
			  jbut.setBackground(selectedColor);
			  repaint();

		  }
            }
    });


  }



}
