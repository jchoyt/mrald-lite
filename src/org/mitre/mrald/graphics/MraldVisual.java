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

import org.mitre.mrald.util.MraldException;

/**
 * A Swing user interface component for configuring the parametes of the
 * Force functions in the given ForceSimulator. Useful for exploring
 * different parameterizations when crafting a visualization.
 *
 * @version 1.0
 * @author ghamilton
 */
public  interface MraldVisual{

	public Object getAttribute(String attributeName) throws MraldException;
	public void setAttribute(String attribute, Object value) throws MraldException;


} // end of class ForcePanel
