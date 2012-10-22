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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.PrintPreview;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefusex.force.Force;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
/**
 * A Swing user interface component for configuring the parametes of the
 * Force functions in the given ForceSimulator. Useful for exploring
 * different parameterizations when crafting a visualization.
 *
 * @version 1.0
 * @author ghamilton
 */
public class MraldPanel extends JPanel implements MraldVisual{


    private HashMap<String,Object> attributeValues = new HashMap<String,Object>();

    private ForceSimulator fsim;

    private static float scaleFactor = 1.0f;

    private ArrayList showForces = new ArrayList();

    private ArrayList<JPanel> tabPanels = new ArrayList<JPanel>();

    protected GridBagLayout gridBag = new GridBagLayout();

    private Properties props = new Properties();

    private VisualColorFunction colorFunc;

    public MraldPanel(HashMap<String,Object> initialVals) throws MraldException
    {

    	this.attributeValues = new HashMap<String,Object>(initialVals);
    	Color panelColor = Color.WHITE;
    	if (hasAttribute("PanelColor"))
    		panelColor = (Color)getAttribute("PanelColor");

    	//Get the colors that are saved in the setings file
    	colorFunc = getColors();

    	//panelColor = colorFunc.getColor("Background");

    	this.setBackground(panelColor);
    	setProperties();

    	initUI();

        addTabPanes();
    } //

    public void setAttribute(String keyName, Object value) throws MraldException
    {
	    attributeValues.put(keyName, value);
    }

     public Object getAttribute(String keyName) throws MraldException
    {
	   return attributeValues.get(keyName);
    }

      public boolean hasAttribute(String keyName) throws MraldException
    {
	    return attributeValues.containsKey(keyName);
    }

    public ColorFunction getColorFunction()
    {
    	return colorFunc;
    }

    protected void initUI() throws MraldException{

    	//this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    	//Initialize the tab Panels
    	initTabPanels(2);

    	this.setLayout(gridBag);

    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.BOTH;
    	c.weightx = 1.0;

    	setActions();
    	if (hasAttribute("displayForces"))
    	{
    		showForces = (ArrayList)getAttribute("displayForces");
    	}

    	//Get ToolBox
		Color boxColor = Color.WHITE;
		Box toolBox = getToolsBox();
		gridBag.setConstraints(toolBox,c);
		addToTabPanel(0, toolBox);

		//Add ColorPanel
		JPanel colorPanel = getColorPanel();
		gridBag.setConstraints(colorPanel,c);
		//this.add(colorPanel);
		addToTabPanel(1, colorPanel);
		addToTabPanel(1, getSaveButton());
		addToTabPanel(1, getPrintButton());

		//Add Search Panel
		SearchPanel searchPanel = (SearchPanel)getSearchPanel();
        if (searchPanel != null)
        {
        	gridBag.setConstraints(searchPanel, c);
        	//this.add(searchPanel);
        	addToTabPanel(0, searchPanel);

        }

        //Add the Slider if it is needed
        this.add(Box.createHorizontalGlue());
        if (hasAttribute("fsim"))
    	{
    		fsim = (ForceSimulator)getAttribute("fsim");
    		Force[] forces = fsim.getForces();
            for ( int i=0; i<forces.length; i++ ) {
                Force f = forces[i];
    	    String name = f.getClass().getName();
    	    name = name.substring(name.lastIndexOf(".")+1);
                if (!showForces.contains(name))
    		continue;

                Box v = new Box(BoxLayout.Y_AXIS);
    	    v.setBackground(boxColor);
                for ( int j=0; j<f.getParameterCount(); j++ ) {
                        Box viz = createObject(f,j);
    		    if (viz != null)
    			    v.add(viz);
                }
                v.setBorder(BorderFactory.createTitledBorder(name));
                gridBag.setConstraints(v, c);

               //this.add(v);
                addToTabPanel(0, v);
            }

    	}

    } //

    private void setProperties() throws MraldException
    {
    	if (hasAttribute("settings"))
    	{
    		props = (Properties)getAttribute("settings");
    		if (props == null)
    			props = new Properties();
    	}
    	else //Maybe load from current values
    		props = new Properties();
    }

    protected void addToTabPanel(int panelNo, Component comp) throws MraldException
    {
    	getTabPanel(panelNo).add(comp);
    }

    protected void addTabPanes() throws MraldException
    {
    	//Clycle through the JPanel and add to Tab Panes
    	JTabbedPane tabPane = new JTabbedPane();
        String[] titles = new String[]{"Tools", "Settings"};
        String title = "";
    	for (int i=0; i < tabPanels.size(); i++)
    	{
    		if (titles.length < i)
    			title = "Tab" + i;
    		else
    			title = titles[i];
    		tabPane.addTab(title, tabPanels.get(i));
    	}
    	this.add(tabPane);
    }

    protected void initTabPanels(int noOfPanels) throws MraldException
    {
    	Color bckColor = this.getBackground();
    	if (hasAttribute("PanelColor"))
    		bckColor = (Color)getAttribute("PanelColor");

    	for (int i=0; i < noOfPanels; i++)
    	{
    		JPanel jPanel = new JPanel();
    		jPanel.setBackground(bckColor);
    		jPanel.setLayout(gridBag);
    		tabPanels.add(jPanel);
    	}
    }

    protected JPanel getTabPanel(int i) throws MraldException
    {
    	if (tabPanels.size() < i)
    		throw new MraldException("Cannot get this Tab");
    	return tabPanels.get(i);
    }

    protected Box getToolsBox() throws MraldException
    {
    	JButton freezeButton = getFreezeButton();
    	JButton overviewButton = getOverviewButton();
//    	Color boxColor = Color.WHITE;

//    	if (hasAttribute("BoxColor"))
//    		boxColor = (Color)getAttribute("BoxColor");
    	Box a = new Box(BoxLayout.X_AXIS);

		a.add(freezeButton);
		a.add(overviewButton);
    	a.setBorder(BorderFactory.createTitledBorder("Tools"));
        return a;
    }

    private VisualColorFunction getColors() throws MraldException
    {

	  if ( hasAttribute("settings"))
	   {

		   VisualColorFunction colorFunc = (VisualColorFunction)getAttribute("colorSet");

		   if (hasAttribute("settings"))
			   colorFunc.loadColors((Properties)getAttribute("settings"));

		   return colorFunc;

	   }
	   else if (hasAttribute("colorSet"))
		    {
		    	VisualColorFunction colorFunc = (VisualColorFunction)getAttribute("colorSet");

		    	return colorFunc;
		    }

	   else
	   {

		   return  new VisualColorFunction();

	   }
    }

    private JPanel getColorPanel() throws MraldException
    {

	    HashMap<String,Color> coloringItems = new HashMap<String,Color>();

	   int rows = 1;

	   coloringItems = colorFunc.getColors();

	   if (! hasAttribute("forces"))
	   {
		   System.out.println("No Forces List found");
		   return null;
	   }

	   JPanel palette =  new MraldPalette( this.getBackground(), coloringItems.size(), rows, coloringItems);
	   palette.addPropertyChangeListener(new ColorChooserChange());
	   palette.putClientProperty("colorSet", colorFunc);
 	   palette.putClientProperty("forces", getAttribute("forces"));

   	   return palette;


    }

   private JPanel getSearchPanel() throws MraldException
   {
		if (!hasAttribute("registry"))
	   		return null;
		if (!hasAttribute("PanelColor"))
		{
			ItemRegistry registry = (ItemRegistry)getAttribute("registry");
			SearchPanel searchPanel = new SearchPanel(registry);
			return searchPanel;
		}
		else
		{
			Color backColor = (Color)getAttribute("PanelColor");
			ItemRegistry registry = (ItemRegistry)getAttribute("registry");
			SearchPanel searchPanel = new SearchPanel(backColor, registry);
			return searchPanel;
		}

   }

   /* Never used!
   private JPanel getPrefixPanel() throws MraldException
   {
	   	if (!hasAttribute("registry"))
	   		return null;
	    ItemRegistry registry = (ItemRegistry)getAttribute("registry");
	    String[] attr = new String[]{"label"};
	    PrefixSearchPanel prefixPanel = new PrefixSearchPanel(attr, registry);
		return prefixPanel;

   }*/


      private JButton getOverviewButton() throws MraldException
      {
  	    JButton but = new JButton("Overview");
  	    but.putClientProperty("registry", getAttribute("registry"));
	    but.putClientProperty("display", getAttribute("display"));
  	    but.putClientProperty("hide", new Boolean(true));
  	    but.addActionListener((ShowOverviewAction)getAttribute("overview"));
  	    return but;
      }

      private JButton getSaveButton() throws MraldException
      {
  	    JButton but = new JButton("Save");
  	    but.putClientProperty("registry", getAttribute("registry"));
	    but.putClientProperty("colorSet", getAttribute("colorSet"));
  	    but.addActionListener((SaveAllAction)getAttribute("save"));
  	    return but;
      }

      private JButton getPrintButton() throws MraldException
      {
  	    JButton but = new JButton("Print");
  	    but.putClientProperty("registry", getAttribute("registry"));
  	    but.putClientProperty("display", getAttribute("display"));

  	    but.addActionListener((PrintAllAction)getAttribute("print"));
  	    return but;
      }
     private JButton getFreezeButton() throws MraldException
    {

	   JButton but = new JButton("Freeze");
	    but.putClientProperty("registry", getAttribute("registry"));
	    but.putClientProperty("frozen", new Boolean(true));
	    but.addActionListener((FreezeAction)getAttribute("freeze"));

	    return but;
    }

    private void setActions() throws MraldException
    {
	 setAttribute("action", new ForceConstantAction());
	setAttribute("change", new ForceChange());

	setAttribute("freeze", new FreezeAction());
	setAttribute("chooser", new ColorChooser());
	setAttribute("overview", new ShowOverviewAction());
	setAttribute("save", new SaveAllAction());
	setAttribute("print", new PrintAllAction());

    }
      private Box createObject(Force f, int param) throws MraldException{
        Box h = new Box(BoxLayout.X_AXIS);


        float curVal = f.getParameter(param);
	String labelVal = f.getParameterName(param);
	 if (!showForces.contains(labelVal))
		return null;

        if (hasAttribute(f.getParameterName(param)))
		labelVal = (String)getAttribute(f.getParameterName(param));

        JLabel     label = new JLabel(labelVal);
        label.setPreferredSize(new Dimension(200,20));
        label.setMaximumSize(new Dimension(200,20));

	String objectType= "Slider";
	JComponent newObject = null;

	scaleFactor = -1 * curVal;

	if (objectType.equals("Slider"))
	{
		newObject = new JSlider(-1000, 1000, new Float(curVal / scaleFactor).intValue() );
		newObject.setPreferredSize(new Dimension(400,20));
		newObject.setMaximumSize(new Dimension(400,20));
		((JSlider)newObject).addChangeListener((ForceChange)getAttribute("change"));
	}
	else if (objectType.equals("TextField"))
        {
		 newObject  = new JTextField(
                String.valueOf(curVal));

		newObject.setPreferredSize(new Dimension(200,20));
		newObject.setMaximumSize(new Dimension(200,20));
		((JTextField)newObject).addActionListener((ForceConstantAction)getAttribute("action"));

	}
        newObject.putClientProperty("force", f);
        newObject.putClientProperty("param", new Integer(param));

        h.add(label);
        h.add(Box.createHorizontalStrut(10));
        h.add(Box.createHorizontalGlue());
        h.add(newObject);
        h.setPreferredSize(new Dimension(500,30));
        h.setMaximumSize(new Dimension(500,30));
        return h;
    } //

   public void propertyChange(PropertyChangeEvent e)
	    {

		if (e.getPropertyName().equals("Highlight"))
		{

		}

	    }

    private class ForceConstantAction extends AbstractAction{
        public void actionPerformed(ActionEvent arg0) {
            JTextField text = (JTextField)arg0.getSource();
            float val = Float.parseFloat(text.getText());
            Force f = (Force)text.getClientProperty("force");
            Integer param = (Integer)text.getClientProperty("param");
            f.setParameter(param.intValue(), val);
        }
    } // end of inner class ForceAction

    public class ForceChange implements ChangeListener{
	    public void stateChanged(ChangeEvent e) {
		    JSlider source = (JSlider)e.getSource();
		    float val =0.0f;
		    if (!source.getValueIsAdjusting()) {
			    val = (source.getValue() /50) * scaleFactor ;

		    Force f = (Force)source.getClientProperty("force");
		    Integer param = (Integer)source.getClientProperty("param");
		    f.setParameter(param.intValue(), val);
        }

    }
    }

    @SuppressWarnings("unchecked")
     private class ColorChooser extends AbstractAction{
        public void actionPerformed(ActionEvent arg0) {
            JButton but = (JButton)arg0.getSource();
	    Color c = (Color)but.getClientProperty("color");
	    // Trust that the cast will work properly.
	    HashMap<String,Color> items = (HashMap<String,Color>)but.getClientProperty("setColors");
	    //MraldColorChooser chooser =
	    new MraldColorChooser(c, items);
	    MraldColorChooser.main(new String[]{});
        }
    }

    private class ColorChooserChange implements PropertyChangeListener{
        public void propertyChange(PropertyChangeEvent e)
	    {


        	JPanel palette = (JPanel)e.getSource();

        	VisualColorFunction colorSet = (VisualColorFunction)palette.getClientProperty("colorSet");
        	if (colorSet != null)
        	{
        		if (colorSet.hasType(e.getPropertyName()))
        		{
        			colorSet.setColor( e.getPropertyName(), (Color)e.getNewValue());

        		}
        	}
        	else
        	{
        		System.out.println("ColorChooserChange. In properyChange: ColorSet is null");

        	}
        	try
        	{
        		setAttribute("colorSet", colorSet);
        	}
        	catch(MraldException me)
        	{
        		System.out.println("Error in ColorCHooserCHange:" + me.getMessage() + " : " + me.getStackTrace());
        	}
        }
    }


      private class FreezeAction extends AbstractAction{
        public void actionPerformed(ActionEvent arg0) {
            JButton but = (JButton)arg0.getSource();
	    ItemRegistry registry = (ItemRegistry)but.getClientProperty("registry");
	    boolean frozen =  ((Boolean)but.getClientProperty("frozen")).booleanValue();
	    but.putClientProperty("frozen", new Boolean(!frozen));

	    if (registry != null)
	    {
		     Iterator items = registry.getItems();
		     if (frozen)
			     but.setText("Unfreeze");
		     else
			     but.setText("Freeze");
		      while (items.hasNext())
		     {
			     VisualItem vizItem = (VisualItem)items.next();
			     vizItem.setFixed(frozen);
	    	     }
	    }
	    else
		    System.out.println("Could not find property registry");
        }
    } // e


      private class PrintAllAction extends AbstractAction{
          public void actionPerformed(ActionEvent arg0) {
        	  try
        	  {
        		  JButton but = (JButton)arg0.getSource();

//        		  ItemRegistry registry = (ItemRegistry)but.getClientProperty("registry");
        		  System.out.println("Printing this ");
        		  Display printView = (Display)but.getClientProperty("display");

        		  printView.stopEditing();

        		  //printView.printComponents(printView.getGraphics());
        		  int compNo =0;

        		  compNo = printView.getComponents().length;
        		  System.out.println("Mrald Panel Print - no of components " + compNo );

        		  Component[] components = new Component[1];
        		  components[0] = printView;
        		  new PrintPreview(components, " preview all");
        	  }
        	  catch(Exception e)
        	  {
        		  System.out.println("Save error: " + e.getMessage());
        	  }
          } // e

      }

      /* Never used!
      private class SaveImageAction extends AbstractAction{
          public void actionPerformed(ActionEvent arg0) {
        	  try
        	  {
        		  JButton but = (JButton)arg0.getSource();

        		  ItemRegistry registry = (ItemRegistry)but.getClientProperty("registry");
        		  System.out.println("Printing this ");
        		  Display printView = (Display)but.getClientProperty("display");
        		  //Display printView = new Display(registry);

        		  ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
        		  printView.stopEditing();

        		  URL baseURL = null;
        		  printView.saveImage(fileOut, "JPG",1);

        		  if (hasAttribute("baseURL"))
        		       baseURL = (URL)getAttribute("baseURL");
        		  AppletComm.uploadFile(fileOut, baseURL, "FileUtil.jsp","graphics/output.jpg" );
        		  System.out.println("Save Image ");

        		  fileOut.close();


        		  //printView.printComponents(printView.getGraphics());
        		  //printView.print();
        		  //new PrintPreview(printView.getComponents(), " preview all");
        	  }
        	  catch(Exception e)
        	  {
        		  System.out.println("Save error: " + e.getMessage());
        	  }
          } // e

      }*/
      private class SaveAllAction extends AbstractAction{
          public void actionPerformed(ActionEvent arg0) {
        	  try
        	  {
        		  JButton but = (JButton)arg0.getSource();

//        		  ItemRegistry registry = (ItemRegistry)but.getClientProperty("registry");

        		  VisualColorFunction colorSet = (VisualColorFunction)but.getClientProperty("colorSet");
        		  colorSet.saveColors(props);
        		  ByteArrayOutputStream fileOut = new ByteArrayOutputStream();

        		  props.store(fileOut,"test");
        		  System.out.println("Save All. About to upload file");
        		  URL baseURL = null;

        		  if (hasAttribute("baseURL"))
        		       baseURL = (URL)getAttribute("baseURL");

        		  AppletComm.uploadFile(fileOut, baseURL, "FileUtil.jsp","graphics/settings.properties" );
        		  System.out.println("Save Properties ");
        	  }
        	  catch(Exception e)
        	  {
        		  System.out.println("Save error: " + e.getMessage());
        	  }
          } // e

      }
      private class ShowOverviewAction extends AbstractAction{
          public void actionPerformed(ActionEvent arg0)
          {
              JButton but = (JButton)arg0.getSource();
              ItemRegistry registry = (ItemRegistry)but.getClientProperty("registry");
              Display display = (Display)but.getClientProperty("display");

              boolean hide =  ((Boolean)but.getClientProperty("hide")).booleanValue();
              but.putClientProperty("hide", new Boolean(!hide));

              if (registry != null && display != null)
              {
            	 if (hide)
            	 {
            		  but.setText("Hide");
            		  Display overview = new Display(registry);
                	  overview.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1));
                	  overview.setSize(200,200);
                	  overview.zoom(new Point2D.Float(100,100), 0.2);
                	  int compNo = display.getComponentCount();
                	  but.putClientProperty("compNo", new Integer(compNo));

                	  display.add(overview, compNo);
            	 }
            	 else
            	 {
            		  but.setText("Overview");
            		  Display overview = new Display(registry);
                	  overview.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1));
                	  overview.setSize(200,200);
                	  overview.zoom(new Point2D.Float(100,100), 0.2);
                	  Integer compNoInt = (Integer)but.getClientProperty("compNo");
                      if (compNoInt != null)
                      {
                    	  int compNo = compNoInt.intValue();
                    	  display.remove(compNo);
                      }
            	 }

              }
              else
            	  System.out.println("Could not find property registry or dipslay");
          }
      }//e
} // end of class ForcePanel
