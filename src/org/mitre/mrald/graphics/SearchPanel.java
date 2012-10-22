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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.mitre.mrald.util.MraldException;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.FocusManager;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusEventMulticaster;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.DefaultFocusSet;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.Entity;

class SearchPanel extends JPanel implements FocusListener, MraldVisual{


	    private Color backgroundCol = null;
	    private ArrayList<VisualItem> findSet = new ArrayList<VisualItem>();
	    private ItemRegistry registry = null;
	    private FocusListener listener = null;
	    private HashMap<String,Object> attributeValues = new HashMap<String,Object>();
		private JTextField queryText;

	    public SearchPanel(Color background, ItemRegistry registry) throws MraldException{
		    this(registry);
		    this.setBackground(background);
		    backgroundCol = background;
	    }

	    public SearchPanel(ItemRegistry thisRegistry) throws MraldException
	    {
	    	registry = thisRegistry;
	    	init();
	    	initUI();
	    }

	    private void init()
	    {
	    	 registry.getDefaultFocusSet().addFocusListener(this);
	    }

	    private void initUI() throws MraldException
	    {
	    	Box b = new Box(BoxLayout.X_AXIS);
	    	b.setBackground(backgroundCol);

	    	//JLabel searchLabel = new JLabel("search >> ");
	        //JButton searchButton = new JButton("Search");

	        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	        b.add(Box.createHorizontalStrut(5));
	        queryText   = new JTextField(15);
	        queryText.setPreferredSize(new Dimension(200,20));
	        queryText.setMaximumSize(new Dimension(200,20));
	        queryText.addActionListener(new SearchKeyword() );

	        b.putClientProperty("searchText", queryText);
	        //searchButton.addActionListener(new SearchKeyword() );

	        //b.add(searchLabel);
	        b.add(queryText);
	        //b.add(searchButton);
	        b.setBorder(BorderFactory.createTitledBorder("Search"));
	        this.add(b);
	    } //

		 public void search(String query)
		 {

			 FocusManager focusManager = registry.getFocusManager();

			 Iterator iter = registry.getItems();
			 //Cycle through the VisualItems and find the String
			 while ( iter.hasNext() )
			 {
				 	VisualItem itemCheck = (VisualItem)iter.next();
				 	if (itemCheck.getAttribute("label") == null) continue;

				 	if (itemCheck.getAttribute("label").equals(query))
				 	{
				 		findSet.add(itemCheck);

				 	}
			 }
			 int cnt = findSet.size();
			 Entity[] add = new Entity[cnt];
			 for (int i=0; i < findSet.size(); i++)
			 {
				 add[i] = findSet.get(i);
			 }
			 Entity[] rem = findSet.toArray(FocusEvent.EMPTY);

		     FocusEvent fe = new FocusEvent(focusManager.getDefaultFocusSet(), FocusEvent.FOCUS_SET, add, rem);
		     changeFocus(fe);


		 }

		public void changeFocus(FocusEvent e)
		{


			 if ( e.getEventType() == FocusEvent.FOCUS_SET )
			 {
				 FocusEvent fe = e;
				 Entity[] focusItems = fe.getAddedFoci();

				 FocusManager focusManager = registry.getFocusManager();
				 FocusSet searchItems = new DefaultFocusSet();
				 searchItems.add(findSet);
				 focusManager.putFocusSet(FocusManager.DEFAULT_KEY, searchItems);

				 synchronized ( registry ) {

					 FocusManager fm = registry.getFocusManager();
		        	 FocusSet fs = fm.getFocusSet(FocusManager.DEFAULT_KEY);

		        	 for (int i=0; i < findSet.size(); i++)
		        	 {
		        		 VisualItem checkItem = findSet.get(i);

					 	 fs.set(checkItem.getEntity());
			        	registry.touch(checkItem.getItemClass());

			        	 NodeItem n = ((NodeItem)focusItems[i]);
			        	 n.setHighlighted(true);
			        	     Iterator iter = n.getEdges();
			                while ( iter.hasNext() ) {
			                    EdgeItem eitem = (EdgeItem)iter.next();
			                    NodeItem nitem = (NodeItem)eitem.getAdjacentNode(n);
			                    if (eitem.isVisible() ) {
			                        eitem.setHighlighted(true);
			                        registry.touch(eitem.getItemClass());
			                        nitem.setHighlighted(true);
			                        registry.touch(nitem.getItemClass());
			                    }
			                }
			            }


		        	}

				}

		}

		public void addFocusListener(FocusListener fl) {
		      listener = FocusEventMulticaster.add(listener, fl);
		}

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

	      private class SearchKeyword extends AbstractAction{
	          public void actionPerformed(ActionEvent arg0) {

	        	  if (arg0.getSource() instanceof JButton)
	        	  {
//	        		  JButton but = (JButton)arg0.getSource();
	        		  String searchTxt = queryText.getText();
	        		  search(searchTxt);
	        	  }
	        	  else if (arg0.getSource() instanceof JTextField)
	        	  {

	        			  JTextField searchText = (JTextField)arg0.getSource();
	        			  String searchTxt = searchText.getText();
	        			  search(searchTxt);

	        	  }
	          }
	      }

		public void focusChanged(FocusEvent arg0) {
			// TODO Auto-generated method stub

		}

}

