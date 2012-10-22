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
package org.mitre.mrald.formbuilder;

import org.mitre.mrald.util.DBMetaData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
/**
 *  Interface for an Element to support the extensible form building process.
 *
 *@author     jchoyt
 *@created    January 24, 2003
 */
public interface FormBuilderElement
{
    /**
     *  Produces the HTML for inclusion on the second step of form building. The
     *  HTML returned should be self-supporting - i.e., only the guts of a
     *  &lt;div&gt; or a &lt;td&gt; tag. It should not be part of a larger table
     *  structure. It is used in building the second page of the form buliding
     *  process.
     *
     *  MultiDb: Add a Thread parameter - for case where building multiple Elements of
     *  the same type for multiple databases
     *@param  metaData  Metadata about the tables the form is based on.
     *@param  num       Which iteration this is. This should be used to create
     *      unique tag names
     *@return           The HTML for inclusion in the second form building page.
     */
    public String getFBHtml( DBMetaData metaData, int num, int thread );


    /**
     *  This method should build a Node object (or object that inherits from
     *  Node) for inclusion in the xml representation of the MRALD form. Unless
     *  otherwise noted, it is assumed that this will be added to the root node.
     *  It is used in buliding the XML file.
     *
     *@param  document  The Document object the return Node will be added to
     *@return           A Node object for inclusion in the Document being built.
     */
    public Node getFBNode( Document document );

    /**
     *  This method is used to seperate out the Form components into seperate
     *  threads, to determine which FormBuilderElements should be grouped together
     *
     *
     *@param  none
     *@return  int - number of the Thread
     */

   // public int getThread();

}

