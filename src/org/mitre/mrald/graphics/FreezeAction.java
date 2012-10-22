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

import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.JButton;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.AbstractAction;

public class FreezeAction extends AbstractAction{

		private ItemRegistry registry ;
		private boolean freezeItems=false;
		public FreezeAction()
		{
			super();
		}

		public void setRegistry(ItemRegistry registry)
		{
			this.registry = registry;
		}

		public ItemRegistry getRegistry()
		{
			return this.registry;
		}

		/**
		* */
		public void run(ItemRegistry registry, double frac) {

			Iterator visItems = registry.getItems(true);
			if (freezeItems)
			{
				while (visItems.hasNext())
				{
					VisualItem item = (VisualItem)visItems.next();
					item.setFixed(true);
				}
				freezeItems = false;
			}
		}

		public void actionPerformed(ActionEvent arg0) {
			JButton but= (JButton)arg0.getSource();

			Iterator visItems = getRegistry().getItems(true);
			if (but.getText().equals("Freeze"))
			{
				but.setText("Resume");
				while (visItems.hasNext())
				{
					VisualItem item = (VisualItem)visItems.next();
					item.setFixed(true);
				}
			}
			else
			{
				but.setText("Freeze");
				while (visItems.hasNext())
				{
					VisualItem item = (VisualItem)visItems.next();
					item.setFixed(false);
				}

			}

        	}



	}


