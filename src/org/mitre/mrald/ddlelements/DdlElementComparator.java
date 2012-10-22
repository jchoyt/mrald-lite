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
package org.mitre.mrald.ddlelements;

import java.util.Comparator;

/**
 *  Copmarator class for DdlElements. Sorts the DdlElements by Table name, then
 *  by Order.
 *
 *@author     jchoyt
 *@created    December 1, 2002
 */
public class DdlElementComparator implements Comparator<DdlElement>
{

    /**
     *  Sorts the DdlElements by Table name, then by Order.
     *
     *@param  _de1  First DdlElement to be compared
     *@param  _de2  Second DdlElement to be compared
     *@return       a negative integer, zero, or a positive integer as the first
     *      argument is less than, equal to, or greater than the second.
     */
    public int compare( DdlElement de1, DdlElement de2 )
    {
        /*
         *  if ( de1.getTable().equals( de2.getTable() ) )
         *  {
         *  return Integer.parseInt( de1.getOrder() ) - Integer.parseInt( de2.getOrder() );
         *  }
         *  else
         *  {
         *  return de1.getTable().compareTo( de2.getTable() );
         *  }
         */
        int orderDiff = Integer.parseInt( de1.getOrder() ) - Integer.parseInt( de2.getOrder() );
        int tableDiff = de1.getTable().compareTo( de2.getTable() );
        return 1000 * tableDiff + orderDiff;
    }
}

