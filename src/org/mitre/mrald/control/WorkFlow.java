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
package org.mitre.mrald.control;

import java.util.TreeMap;

/******************************************************************************
 * Object to model the Workflow
 * This object is required as there are different workflow paths through the system design
 * @author Gail Hamilton
 *****************************************************************************/
public class WorkFlow extends Object
{
    //declare the member variables here. Remember to initialize.
    private String wfObjectName;
    private int wfOrder = 0;
    private boolean hasMethods = false;
    private TreeMap<Integer,String> wfMethods = new TreeMap<Integer,String>();

    public WorkFlow(){
    }

    public WorkFlow(String thisWfObjectName){

        wfObjectName = thisWfObjectName;
    }

   /****************************************************************************
    * The Constructor which assigns a parameter specified workflow order.
    ***************************************************************************/
    public WorkFlow(int thisWfOrder){

        wfOrder = thisWfOrder;
    }

    /****************************************************************************
    * This method as a parameter Workflow Method in the appropriate order.
    ***************************************************************************/

  public void addWfMethod(String thisWfMethodName,Integer order )
  {
    wfMethods.put(order,thisWfMethodName);
  }

    /****************************************************************************
    * This method checks to see if the given step in the workflow contains a key
    ***************************************************************************/

  public boolean containsKey(int order )
  {
    return containsKey(new Integer(order));
  }

    /****************************************************************************
    * Method to check if the Key Value is within the hashmap (key value is order)
    ***************************************************************************/

  public boolean containsKey(Integer order )
  {
    return wfMethods.containsKey(order);
  }

  /****************************************************************************
  * This method returns a value of true of false depending on if the workflow path
  * contains any methods.
  ***************************************************************************/

  public boolean hasMethods( )
  {
    return hasMethods;
  }

  /****************************************************************************
  * This method returns the desired workflow method for the requested order.
  ***************************************************************************/

  public String getWfMethod( int order )
  {
    return wfMethods.get(new Integer(order)).toString();
  }

  /****************************************************************************
  * This method sets the workflow object.
  ***************************************************************************/

  public void setWfObject(String thisWfObjectName)
  {
    wfObjectName = thisWfObjectName;
  }

  /****************************************************************************
  * This method sets the workflow order
  ***************************************************************************/

  public void setWfOrder(int thisWfOrder)
  {
    wfOrder = thisWfOrder;
  }

  /****************************************************************************
  * This method returns the method's size.
  ***************************************************************************/

  public int getMethodSize()
  {
        return wfMethods.size();
  }

  /****************************************************************************
  * This method returns the name of the desired workflow object.
  ***************************************************************************/

  public String getWfObject()
  {
    return wfObjectName;
  }

  /****************************************************************************
  * This method returns the workflow order.
  ***************************************************************************/

  public int getWfOrder()
  {
    return wfOrder ;
  }

  /****************************************************************************
  * This method is used to set the variable which indicates whether or not
  * the workflow has any methods.
  ***************************************************************************/

  public void setHasMethods(boolean thisHasMethods)
  {
    hasMethods = thisHasMethods;
  }

} //End of Class
