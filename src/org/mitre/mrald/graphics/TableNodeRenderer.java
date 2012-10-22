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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.net.URL;
import java.util.ArrayList;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.ImageFactory;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
/**
 *  Renders an item as a text string. The text string used by default is the
 *  value of the "label" attribute. To use a different attribute, use the <code>setTextAttributeName</code>
 *  method. To perform custom String selection, simply subclass this Renderer
 *  and override the <code>getText</code> method.
 *
 *@author     <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 *@created    October 6, 2004
 *@version    1.0
 */
public class TableNodeRenderer extends TextItemRenderer
{
public static final int DEFAULT_MAXLINES = 3;


    	protected AffineTransform m_transform = new AffineTransform();
    	protected ImageFactory m_images = new ImageFactory();
	protected String m_imageName = "image";

	private URL codeBaseUrl = null;

	protected RectangularShape m_imageBox  = new Rectangle2D.Float();

	protected int noOfLines=0;

	private boolean tableOnly =false;

	protected class TextEntry {
		public TextEntry(String n, int m, Font f) {
			name = n;
			maxlines = m;
			font = f;
		}
		String name;
		int maxlines;
		Font font;
	} //
	protected ArrayList<TextEntry> m_attrList = new ArrayList<TextEntry>();


	/**
	 *  Constructor for the TextItemRenderer object
	 */
	public TableNodeRenderer(URL baseUrl)
	{
		setCodeBaseUrl(baseUrl);
		init();
	}
	//

	/**
	 *  Constructor for the TextItemRenderer object
	 */
	public TableNodeRenderer()
	{
		init();
	}
	//

	private void init()
	{
		try
		{
			//URL imageURL = new URL(codeBaseUrl + "/graphics/pkey.png");

			URL imageURL = new URL("http://127.0.0.1:8080/graphics/pkey.png");
			System.out.println("TableNodeRenderer: baseURl :" + codeBaseUrl);

			if (codeBaseUrl != null)
				imageURL = new URL(codeBaseUrl + "/pkey.png");

			Image pkImage = java.awt.Toolkit.getDefaultToolkit().createImage(imageURL);

			imageURL = new URL("http://127.0.0.1:8080/graphics/fkey.png") ;

			if (codeBaseUrl != null)
				imageURL = new URL(codeBaseUrl + "/fkey.png");

			Image fkImage = java.awt.Toolkit.getDefaultToolkit().createImage(imageURL);

			m_images.addImage("pkey", pkImage);
			m_images.addImage("fkey", fkImage);
		}
		catch(java.net.MalformedURLException me)
		{
			try
			{
				URL imageURL = new URL("http://127.0.0.1:8080/graphics/pkey.png");
				Image pkImage = java.awt.Toolkit.getDefaultToolkit().createImage(imageURL);

				imageURL = new URL("http://127.0.0.1:8080/graphics/fkey.png") ;
				Image fkImage = java.awt.Toolkit.getDefaultToolkit().createImage(imageURL);
				m_images.addImage("pkey", pkImage);
				m_images.addImage("fkey", fkImage);
			}
			catch(java.net.MalformedURLException te)
			{
				System.out.println( "TableNodeRenderer : MalformedURL: " + te.getMessage());

			}
		}
	}

	public void logState(String mess)
	{
		//MraldOutFile.logToFile( Config.getProperty("LOGFILE") , "TableNodeRenderer: logging inherits from textitemrenderer. version 3.0 " + mess );
	}

	public void setTableOnly(boolean tableOnly)
	{
		this.tableOnly = tableOnly;
	}

	public boolean getTableOnly()
	{
		return this.tableOnly;
	}

	public void setCodeBaseUrl(URL codeBaseUrl)
	{
		this.codeBaseUrl = codeBaseUrl;
	}

	public void addTextAttribute(String attrName) {
		addTextAttribute(attrName, DEFAULT_MAXLINES, null);
	} //

	public void addTextAttribute(String attrName, int maxlines) {
		m_attrList.add(new TextEntry(attrName, maxlines, null));
	} //

	public void addTextAttribute(String attrName, int maxlines, Font font) {
		m_attrList.add(new TextEntry(attrName, maxlines, font));
	} //

	/**
	 * This method is not applicable in this class. Calling it
	 * causes an exception to be generated.
	 * @throws UnsupportedOperationException if called.
	 */
	protected String getText(VisualItem item) {
		throw new UnsupportedOperationException();
	} //

	protected String getText(VisualItem item, int entry) {
		String name = m_attrList.get(entry).name;
		return item.getAttribute(name);

	} //

	public int getNumEntries() {
		return m_attrList.size();
	} //

	public int getNumLines() {
		return noOfLines;
	} //

	//The number of lines may not match up wit hthe number of entries
	private void setNumLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}
	protected int getMaxLines(int entry) {
		return m_attrList.get(entry).maxlines;
	} //

	/**
	 *@param  item  Description of the Parameter
	 *@return       The rawShape value
	 *@see          edu.berkeley.guir.prefuse.render.ShapeRenderer#getRawShape(edu.berkeley.guir.prefuse.VisualItem)
	 */
	protected Shape getRawShape(VisualItem item)
	{
		//Expand Width if this is a detailed Table and not just the title alone
		int widthFactor = 1;
		int w = m_horizBorder * widthFactor;
		int h = 2*m_vertBorder;
		for ( int i = 0; i < getNumLines(); i++ ) {
			Font font = getFont(item, i);
			FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(font);
			String  text = getText(item, i);
//			String label_text = item.getAttribute(m_labelName);
			//w=  Math.max(fm.stringWidth(text) + 2*m_horizBorder, fm.stringWidth(label_text) + 2*m_horizBorder );
//			int maxlines = getMaxLines(i);
			if ( text != null ) {
				h += fm.getHeight();
				//Multiple by 2 to give enough space for the type
				w = Math.max(w, (fm.stringWidth(text) + 2*m_horizBorder) * widthFactor);
			}
			widthFactor = 2; //put to two as this allows for type
		}
		getAlignedPoint(m_tmpPoint, item, w, h, m_xAlign, m_yAlign);
		m_textBox.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY(),w,h);
		return m_textBox;
	}

	//
protected Font getFont(VisualItem item, int entry) {

		Font f = m_attrList.get(entry).font;
		if ( f == null ) { f = item.getFont();	}
		if ( f == null ) { f = m_font; }
		return f;
	} //

	public Rectangle getEntryBounds(VisualItem item, int entry) {

		int widthFactor = 2;
		int dy = m_vertBorder, ew = 0, eh = 0;
		int w = widthFactor *m_horizBorder;
		int h = 2*m_vertBorder;

		for ( int i = 0; i <= entry; i++ ) {
			Font font = getFont(item, i);
			FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(font);
			String  text = getText(item, i);
			//int maxlines = getMaxLines(i);
			if ( text != null ) {
				h += fm.getHeight();
				w = Math.max(w, fm.stringWidth(text) + widthFactor*m_horizBorder);
				if ( i < entry ) {
					dy += fm.getHeight();
				} else if ( i == entry ) {
					ew = fm.stringWidth(text) + widthFactor*m_horizBorder;
					eh = fm.getHeight();
				}
			}
		}
		getAlignedPoint(m_tmpPoint, item, w, h, m_xAlign, m_yAlign);
		m_textBox.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY()+dy,ew,eh);
		return m_textBox.getBounds();
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.render.Renderer#render(java.awt.Graphics2D, edu.berkeley.guir.prefuse.VisualItem)
	 */
	public void render(Graphics2D g, VisualItem item) {
		try
		{


			//These will be processed as part of the column attribute as want this all on one line
			int lineNo=0;
			 if (tableOnly)
			 {
				 lineNo=1;
			 }
			 else
			 {
				 for ( int i = 0; i < getNumEntries(); i++ ) {

					 String name = m_attrList.get(i).name;
					 if (name.startsWith("pkey") || name.startsWith("fkey") ||name.startsWith("type") ) continue;
					 lineNo ++;

				 }
			 }

			setNumLines(lineNo);

			Paint fillColor = item.getFillColor();
			Paint itemColor = item.getColor();
			Shape shape = getShape(item);
			if (shape != null) {
				switch (getRenderType(item)) {
					case RENDER_TYPE_DRAW :
						g.setPaint(itemColor);
						g.draw(shape);
						break;
					case RENDER_TYPE_FILL :
						g.setPaint(fillColor);
						g.fill(shape);
						break;
					case RENDER_TYPE_DRAW_AND_FILL :
						g.setPaint(fillColor);
						g.fill(shape);
						g.setPaint(itemColor);
						g.draw(shape);
						break;
				}
			}

			Rectangle r = shape.getBounds();

			int h = r.y + m_vertBorder;

			ArrayList<Object> pkeyCols=new ArrayList<Object>();
			int pKeyCount=1;
			String pkeyLabel="pkey1" ;
			while (item.getAttribute(pkeyLabel) != null)
			{
				pkeyLabel = "pkey" + pKeyCount;
				pkeyCols.add(item.getAttribute(pkeyLabel));
				pKeyCount++;
			}

			ArrayList<Object> fkeyCols=new ArrayList<Object>();
			int fKeyCount=1;
			String fkeyLabel="fkey1" ;
			while (item.getAttribute(fkeyLabel) != null)
			{
				fkeyLabel = "fkey" + fKeyCount;
				fkeyCols.add(item.getAttribute(fkeyLabel));
				fKeyCount++;
			}


			lineNo=0;
			for ( int i = 0; i < getNumEntries(); i++ )
			{

			  int xPos = r.x+m_horizBorder;
			  Font font = getFont(item, i);
			  FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(font);
			  String name = m_attrList.get(i).name;
				//If only outputting the table only
			  if (tableOnly)
			  {
				if (name.equals(m_labelName))
				{
					String text = getText(item, i);
					if (text == null)
					{
						i = getNumEntries();
						continue;
					}
					if ( text != null ) {
						Color overlay = (Color)item.getVizAttribute("overlay_"+lineNo);
						if ( overlay != null ) {
							g.setColor(overlay);
							Rectangle or = new Rectangle(r.x+m_horizBorder, h,
								fm.stringWidth(text) , fm.getHeight());
							g.fill(or);
						}

						g.setPaint(itemColor);
						g.setFont(font);
						g.drawString(text, xPos, h+fm.getAscent());


					}
				}
			  }
			  else
			  {
				//These will be processed as part of the column attribute as want this all on one line
				if (name.startsWith("pkey") || name.startsWith("fkey") ||name.startsWith("type") ) continue;

				int typeNo = i;
				String colType = item.getAttribute("type" + typeNo );

				lineNo ++;
				String text = getText(item, i);
				if (text == null)
				{
					i = getNumEntries();
					continue;
				}
				int push =0;

				//The first line is the tableName

				if (!name.equals(m_labelName))
				{
					int offset = 0;
					//if (text.equals(pkeyCol))
					if (pkeyCols.contains(text))
					{
						push = drawImage(g, r, item,h, "pkey", offset);
						xPos = xPos + push;
						offset = push;
						r = new Rectangle( r.x, r.y, new Double(r.getWidth()).intValue() + push, new Double(r.getHeight()).intValue());
					}
					if (fkeyCols.contains(text))
					{
						push = drawImage(g, r, item,h, "fkey", offset);
						xPos = xPos + push;
						r = new Rectangle( r.x, r.y, new Double(r.getWidth()).intValue() + push, new Double(r.getHeight()).intValue());

					}
					text = text + " : " + colType;
				}
//				int maxlines = getMaxLines(i);


				if ( text != null ) {
					Color overlay = (Color)item.getVizAttribute("overlay_"+lineNo);
					if ( overlay != null ) {
						g.setColor(overlay);
						Rectangle or = new Rectangle(r.x+m_horizBorder, h,
							fm.stringWidth(text) + push, fm.getHeight());
							g.fill(or);
					}

					g.setPaint(itemColor);
					g.setFont(font);
					g.drawString(text, xPos, h+fm.getAscent());

					h += fm.getHeight();
				}

			 }
		  }
		}
		catch(Exception e)
		{
			logState("Exception :" + e.getMessage());
		}
	}

	private int drawImage(Graphics2D g,  Rectangle r, VisualItem item, int h, String type, int offset)
	{
		Image img = getImage(item, type);

		Paint fillColor = item.getFillColor();
//		Paint itemColor = item.getColor();

		if ( img != null ) {
				Composite comp = g.getComposite();
				// enable alpha blending for image, if needed
				if ( fillColor instanceof Color) {
					int alpha = ((Color)fillColor).getAlpha();
					if ( alpha < 255 ) {
						AlphaComposite alphaComp =
							AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
								((float)alpha)/255);
								g.setComposite(alphaComp);
					}
				}
				Font font = getFont(item, 0);
				FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(font);

				g.drawImage(img, r.x+offset + m_horizBorder, h - fm.getAscent()/2,  null);

				double size = r.x;
				double is = 1;
//				double w = is*img.getWidth(null);
				double h2 = is*img.getHeight(null);

				double y = r.getMinY() + (r.getHeight()-h2)/2;
				double x2 = r.getMinX() + size*m_horizBorder;
				m_transform.setTransform(is,0,0,is,x2,y);


				g.setComposite(comp);

				return img.getWidth(null);
			}
			return 0;
	}

	/**
	 * Returns a URL for the image to draw. Subclasses can override
	 * this class to perform custom image selection.
	 * @param item the item for which to select an image to draw
	 * @return an <code>Image</code> to draw
	 */
	protected String getImageLocation(VisualItem item) {
		//return item.getAttribute(m_imageName);
		return "pkey";
		//return item.getAttribute(m_imageName);
	} //

	protected Image getImage(VisualItem item, String type) {
		Image rtnImage=null;


			rtnImage = m_images.getImage(type) ;

		return rtnImage;

	} //
}

