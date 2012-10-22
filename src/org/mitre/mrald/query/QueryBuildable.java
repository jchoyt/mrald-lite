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
package org.mitre.mrald.query;

import java.util.ArrayList;

import org.mitre.mrald.util.MraldException;
/**
 * This QueryBuildable interface must be implemented by each of the
 * QueryElement objects. This interface contains the needed functions necessary
 * to effectively update the query string being processed
 * @author Brian Blake
 * @version 1.0
 */
public interface QueryBuildable  {

    public ArrayList buildSelect(ArrayList<String> currentSelectList) throws MraldException;


    public ArrayList buildFrom (ArrayList<String> currentFromList) throws MraldException;


    public ArrayList buildWhereAnd (ArrayList<String> currentWhereAndList) throws MraldException;


    //public ArrayList buildWhereOr (ArrayList currentWhereOrList) throws MraldException;


    public ArrayList buildOrderBy (ArrayList<String> currentOrderBy) throws MraldException;


    public ArrayList buildGroupBy (ArrayList<String> currentOrderBy) throws MraldException;

}

