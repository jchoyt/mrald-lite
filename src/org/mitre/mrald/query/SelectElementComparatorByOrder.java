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

import java.util.Comparator;

public class SelectElementComparatorByOrder implements Comparator<SelectElement>
{
    /**
     *  Compares its two arguments for order. Returns a negative integer, zero,
     *  or a positive integer as the first argument is less than, equal to, or
     *  greater than the second. The implementor must ensure that sgn(compare(x,
     *  y)) == -sgn(compare(y, x)) for all x and y. (This implies that
     *  compare(x, y) must throw an exception if and only if compare(y, x)
     *  throws an exception.) The implementor must also ensure that the relation
     *  is transitive: ((compare(x, y)>0) && (compare(y, z)>0)) implies
     *  compare(x, z)>0. Finally, the implementer must ensure that compare(x,
     *  y)==0 implies that sgn(compare(x, z))==sgn(compare(y, z)) for all z. It
     *  is generally the case, but not strictly required that (compare(x, y)==0)
     *  == (x.equals(y)). Generally speaking, any comparator that violates this
     *  condition should clearly indicate this fact. The recommended language is
     *  "Note: this comparator imposes orderings that are inconsistent with
     *  equals."
     *
     *@param  o1                      the first object to be compared.
     *@param  o2                      the second object to be compared.
     *@return                         a negative integer, zero, or a positive
     *      integer as the first argument is less than, equal to, or greater
     *      than the second.
     *@exception  ClassCastException  thrown if the arguments' types prevent
     *      them from being compared by this Comparator.
     */
     public int	compare(SelectElement o1, SelectElement o2)
     {
         Integer order1 = Integer.decode(o1.getOrder());
         Integer order2 = Integer.decode(o2.getOrder());
         return order1.compareTo( order2 );
     }


     /**
      *  Indicates whether some other object is "equal to" this Comparator. This
      *  method must obey the general contract of Object.equals(Object).
      *  Additionally, this method can return true only if the specified Object
      *  is also a comparator and it imposes the same ordering as this
      *  comparator. Thus, comp1.equals(comp2) implies that sgn(comp1.compare(o1,
      *  o2))==sgn(comp2.compare(o1, o2)) for every object reference o1 and o2.
      *  Note that it is always safe not to override Object.equals(Object).
      *  However, overriding this method may, in some cases, improve performance
      *  by allowing programs to determine that two distinct Comparators impose
      *  the same order. <br>
      *  Overrides: equals in class Object <br>
      *  true only if the specified object is also a comparator and it imposes
      *  the same ordering as this comparator.
      *
      *@param  obj  the reference object with which to compare.
      *@return      true only if the specified object is also a comparator and it
      *      imposes the same ordering as this comparator.
     */
    public boolean equals( Object obj )
    {
        if ( obj instanceof SelectElementComparatorByOrder )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
