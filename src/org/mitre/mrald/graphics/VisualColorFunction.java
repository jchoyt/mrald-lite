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
import java.util.HashMap;
import java.util.Properties;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
/**
     * A custom color function for setting the color of on-screen items.
     */
    public class VisualColorFunction extends ColorFunction {

//	private Color pastelRed = new Color(255,125,125);
//        private Color pastelOrange = new Color(255,200,125);
//        private Color lightGray= new Color(230, 246,226);
//	private Color darkGreen=new Color(120, 166, 124);
        private Color lightGreen = new Color(237, 244, 235);
//        private Color lightYellow = new Color(234, 230, 165);
        private Color paleYellow = new Color(253, 234, 207);
        private Color darkishGreen = new Color(147,189, 134);
        private Color paleCream =  new Color(243,242,235);
        private Color transparent=  new Color(243,242,235, 0);


	private String HIGHLIGHT = "HighLight";
	private String STANDARD= "Standard";
	private String FOCUS = "Focus";
	private String BACKGROUND= "Background";

	private Color highlightColor = paleYellow;
	private Color standardColor = lightGreen;
	private Color focusColor = darkishGreen;
	private Color backgroundColor = paleCream;

        public Paint getColor(VisualItem item) {
            if ( item instanceof EdgeItem ) {
                if ( item.isHighlighted() )
                    return highlightColor;
                else
                    return Color.LIGHT_GRAY;
            } else {
                return Color.BLACK;
            }
        } //

        public Color getColor(String type)
        {
        	if (type.equals(HIGHLIGHT))
        		return highlightColor;
        	else if(type.equals(STANDARD))
        		return standardColor;
        	else if(type.equals(FOCUS))
    			return focusColor;
        	else //(type.equals(BACKGROUND))
        		return backgroundColor;
        }

        public Paint getFillColor(VisualItem item) {

            if ( item instanceof NodeItem ) {
            	if ( item.isHighlighted() )
                    return highlightColor;
                if ( item.isFixed() )
                    return focusColor;
                else
                    return standardColor;
            }
            else if ( item instanceof EdgeItem ) {
            	return transparent;
            }
            else {
                return  focusColor;
            }
        } //

	public boolean hasType(String type)
	{
		if (  (type.equals(HIGHLIGHT) ) || type.equals(STANDARD)  ||type.equals(FOCUS) ||type.equals(BACKGROUND) )
			return true;
		else
			return false;
	}

	public void setColor(String type, Color setColor)
	{
		if (type.equals(HIGHLIGHT))
			highlightColor = setColor;
		else if ( type.equals(FOCUS))
			focusColor = setColor;
		else if ( type.equals(BACKGROUND))
			backgroundColor = setColor;
		else
			standardColor = setColor;
	}


	public HashMap<String,Color> getColors()
	{
		HashMap<String,Color> colorSet = new HashMap<String,Color>();
		colorSet.put(HIGHLIGHT, highlightColor);
		colorSet.put(STANDARD, standardColor);
		colorSet.put(FOCUS, focusColor);
		colorSet.put(BACKGROUND, backgroundColor);
		return colorSet;
	}

	public Properties saveColors(Properties props)
	{

		props.setProperty(HIGHLIGHT + "_color", new Integer(highlightColor.getRGB()).toString());
		props.setProperty(STANDARD + "_color", new Integer(standardColor.getRGB()).toString());
		props.setProperty(FOCUS + "_color", new Integer (focusColor.getRGB()).toString());
		props.setProperty(BACKGROUND + "_color", new Integer (backgroundColor.getRGB()).toString());
		return props;
	}

	public void loadColors(Properties props)
	{

		int colorInt  = Integer.parseInt(props.get(HIGHLIGHT + "_color").toString());
		highlightColor = new Color(colorInt);
		colorInt  = Integer.parseInt(props.get(STANDARD + "_color").toString());
		standardColor = new Color(colorInt);
		colorInt  = Integer.parseInt(props.get(FOCUS + "_color").toString());
		focusColor = new Color(colorInt);

		if (props.get(BACKGROUND + "_color") == null)
		{
			colorInt  = backgroundColor.getRGB();
			backgroundColor = new Color(colorInt);
		}
		else
		{
			colorInt  = Integer.parseInt(props.get(BACKGROUND + "_color").toString());
			backgroundColor = new Color(colorInt);
		}

	}
 } // end of inner class DemoColorFunction

