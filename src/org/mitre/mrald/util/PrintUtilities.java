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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;


/**
 *  A simple utility class that lets you very simply print an arbitrary
 *  component. Just pass the component to the PrintUtilities.printComponent. The
 *  component you want to print doesn't need a print method and doesn't have to
 *  implement any interface or do anything special at all. <P>
 *
 *  If you are going to be printing many times, it is marginally more efficient
 *  to first do the following: <PRE>
 *    PrintUtilities printHelper = new PrintUtilities(theComponent);
 *  </PRE> then later do printHelper.print(). But this is a very tiny
 *  difference, so in most cases just do the simpler
 *  PrintUtilities.printComponent(componentToBePrinted). 7/99 Marty Hall,
 *  http://www.apl.jhu.edu/~hall/java/ May be freely used or adapted.
 *
 *@author     jchoyt
 *@created    July 27, 2005
 */

public class PrintUtilities implements Printable
{
    private Component[] componentsToBePrinted;


    /**
     *  Description of the Method
     *
     *@param  c  Description of the Parameter
     */
    public static void printComponent( Component c )
    {
        new PrintUtilities( c ).print();
    }

    /**
     *  Description of the Method
     *
     *@param  c  Description of the Parameter
     */
    public static void printComponent( Component[] c )
    {
        new PrintUtilities( c ).print();
    }

    /**
     *  Constructor for the PrintUtilities object
     *
     *@param  componentToBePrinted  Description of the Parameter
     */
    public PrintUtilities( Component componentToBePrinted )
    {

    	this.componentsToBePrinted = new Component[]{componentToBePrinted };

    }

    /**
     *  Constructor for the PrintUtilities object
     *
     *@param  componentToBePrinted  Description of the Parameter
     */
    public PrintUtilities( Component[] componentsToBePrinted )
    {
        this.componentsToBePrinted = componentsToBePrinted;
    }

    /**
     *  Description of the Method
     */
    public void print()
    {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable( this );
        if ( printJob.printDialog() )
        {
            try
            {
                printJob.print();
            }
            catch ( PrinterException pe )
            {
                System.out.println( "Error printing: " + pe );
            }
        }
    }


    /**
     *  Constructor for the PrintUtilities object
     *
     *@param  componentToBePrinted  Description of the Parameter
     */
    public void addComponent( Component componentToBePrinted )
    {
        int size = componentsToBePrinted.length;
        Component[] newComponents = new Component[ size + 1];
        for (int i=0; i< size; i++)
        {
        	newComponents[i] = componentsToBePrinted[i];
        }
        newComponents[ size] = componentToBePrinted ;

        componentsToBePrinted = newComponents;
    }

    /**
     *  Description of the Method
     *
     *@param  g           Description of the Parameter
     *@param  pageFormat  Description of the Parameter
     *@param  pageIndex   Description of the Parameter
     *@return             Description of the Return Value
     */
    public int print( Graphics g, PageFormat pageFormat, int pageIndex )
    {
        /*if ( pageIndex > 0 )
        {
            return ( NO_SUCH_PAGE );
        }
        else
        {
        	*/
    		//Make sure that this the components exist
    		if (pageIndex > (componentsToBePrinted.length -1) )
    			return NO_SUCH_PAGE;

            Graphics2D g2d = ( Graphics2D ) g;
           double scaleWidth = pageFormat.getImageableWidth() /  componentsToBePrinted[pageIndex].getWidth();

            g2d.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
            g2d.scale(scaleWidth, scaleWidth);

            componentsToBePrinted[pageIndex].print( g2d );
            return ( PAGE_EXISTS );
       // }
    }

    /**
     *  Description of the Method
     *
     *@param  g           Description of the Parameter
     *@param  pageFormat  Description of the Parameter
     *@param  pageIndex   Description of the Parameter
     *@return             Description of the Return Value
     */
    public int print( Graphics g, PageFormat pageFormat, int pageIndex, boolean printAll )
    {
    	if ( pageIndex > 0 )
        {
            return ( NO_SUCH_PAGE );
        }
    	if (!printAll)
    		return print(g, pageFormat, pageIndex);

    	int size = componentsToBePrinted.length;

        for (int i=0; i < size; i++)
        {

            Graphics2D g2d = ( Graphics2D ) g;
            double scaleWidth = pageFormat.getImageableWidth() /  componentsToBePrinted[i].getWidth();

            g2d.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
            g2d.scale(scaleWidth, scaleWidth);

            componentsToBePrinted[i].print( g2d );

        }
        //return ( NO_SUCH_PAGE );
        return ( PAGE_EXISTS );
    }
}

