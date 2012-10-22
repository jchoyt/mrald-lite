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


import java.util.*;

/**
 * The <code>LimtedStack</code> class represents a last-in-first-out
 * (LIFO) stack of objects that maintains a maximum maxSize. It extends class
 * <tt>Stack</tt> but overrides the push method to remove items from the bottom
 * if the stack gets too high.  The default maxSize is 10.
 * <p>
 * When a stack is first created, it contains no items.
 * <p>
 * Because Stack is inherited from Vector, all the methods of Vector are
 * available.
 *
 */
public class LimitedStack<E> extends Stack<E>
{
    protected int maxSize = 10;


    public LimitedStack( int maxSize )
    {
        this.maxSize = maxSize;
    }

    /**
     * Returns the maximum size
     */
    public int getMaxSize()
    {
        return maxSize;
    }


    /**
     * Pushes an item onto the top of this stack. If the corresponding maxSize
     * is greater than the maximum size specified (defaults to 10), the item on
     * bottom of the stack is removed to maintain that maximum size.
     *
     * @param   item   the item to be pushed onto this stack.
     * @return  the <code>item</code> argument.
     */
    public E push( E item )
    {
        addElement( item );
        while( size() > maxSize ) {
            removeElementAt( 0 );
        }
        return item;
    }
}

