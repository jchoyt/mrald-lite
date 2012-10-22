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
package org.mitre.mrald.util;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JPanel;



public class ImageDisplayPanel extends JPanel
{
	private Image currentImage;

	public ImageDisplayPanel( Image currentImage)
	{
		this.currentImage = currentImage;
	}

	/**
	 * @return Returns the currentImage.
	 */
	public Image getCurrentImage() {
		return currentImage;
	}

	/**
	 * @param currentImage The currentImage to set.
	 */
	public void setCurrentImage(Image currentImage) {
		this.currentImage = currentImage;
	}

	/**
	 *  Description of the Method
	 *
	 *@param  image  Description of the Parameter
	 *@param  x      Description of the Parameter
	 *@param  y      Description of the Parameter
	 */
	public void drawImage( Image image )
	{

		Rectangle rect = new Rectangle(200,200);

		Image offScrImage = this.createImage( rect.width, rect.height );

		Graphics offScrGc = offScrImage.getGraphics();
		offScrGc.setColor( Color.black );
		offScrGc.fillRect( 0, 0, rect.width, rect.height );

		offScrGc.drawImage(offScrImage,0,0,this);
		this.paint(offScrGc);

		if (offScrGc == null)
			System.out.println("Scrollable Image - this is null");

	}

}

