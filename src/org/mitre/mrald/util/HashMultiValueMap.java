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

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Peter Mork
 *
 * A HashMultiValueMap maps keys to sets of values.  (By comparison,
 * a HashMap maps each key to a single value.)
 */
public class HashMultiValueMap<K,V> implements Map<K,V>{

	/**
	 * The multi-value map uses a regular hashmap to relate each key to
	 * a set of values.
	 */
	private final HashMap<K,Set<V>> map;

	public int size() { return map.size(); }
	public boolean isEmpty() { return map.isEmpty(); }
	public boolean containsKey(Object key) { return map.containsKey(key); }
	public void clear() { map.clear(); }
	public Set<K> keySet() { return map.keySet(); }

	public HashMultiValueMap() {
		map = new HashMap<K,Set<V>>();
	}

	public HashMultiValueMap(int size) {
		map = new HashMap<K,Set<V>>(size);
	}

	/**
	 * Because the value could be associated with any key, this method
	 * tests every set of values.
	 */
	public boolean containsValue(Object value) {
		for (Set<V> values : map.values()) {
			if (values.contains(value)) return true;
		}
		return false;
	}

	/**
	 * @param key The key to find in the hashmap.
	 * @return A list of <em>all</em> the values that match the key.
	 */
	public Set<V> getAll(K key) {
		return map.get(key);
	}

	/**
	 * @param key The key to find in the hashmap.
	 * @return A <em>single</em> value that matches the key.
	 */
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		// We assume that the key really is of type K.
		Set<V> values = getAll((K)key);
		return (values == null ? null : values.iterator().next());
	}

	/**
	 * If the key is already associated with a set of values, the value
	 * is added to that set.  Otherwise a new set of values is created.
	 */
	public V put(K key, V value) {
		Set<V> values = getAll(key);
		if (values == null) {
			values = new LinkedHashSet<V>();
			map.put(key, values);
		}
		values.add(value);
		return value;
	}

	/**
	 * Removes <em>all</em> of the values associated with the key.
	 */
	public V remove(Object key) {
		Set<V> values = map.remove(key);
		return (values == null)? null : values.iterator().next();
	}

	/**
	 * @param key A particular key to remove.
	 * @param value A particular value to remove.
	 * @return The value removed from the hash-map, or null if the value
	 * was not associated with the key.
	 */
	@SuppressWarnings("unchecked")
	public V remove(Object key, V value) {
		// We assume that the key really is of type K.
		Set<V> values = getAll((K)key);
		boolean found = (values == null ? false: values.remove(value));
		if (values.isEmpty()) map.remove(key);
		return (found ? value : null);
	}

	/**
	 * Populates a HashMultiValueMap with a normal (single-value) map.
	 */
	public void putAll(Map<? extends K,? extends V> t) {
		for (Entry<? extends K,? extends V> entry : t.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public Set<Entry<K,V>> entrySet() { return new EntrySet(); }

	private class EntrySet extends AbstractSet<Entry<K,V>> {

		private final ArrayList<EntryPair> entries = new ArrayList<EntryPair>();

		/**
		 * An entry set flattens the key->{value} pairs into a set of
		 * of key->value pairs.  A given key may appear multiple times
		 * in this set.  Note that changes to the set are not reflected
		 * in the map unles they are made using the iterator.
		 */
		public EntrySet() {
			for (K key : keySet()) {
				Set<V> values = getAll(key);
				for (V value : values) {
					entries.add(new EntryPair(key, value, values));
				}
			}
		}

		@Override
		public Iterator<Entry<K,V>> iterator() {
			return new EntryIterator<Entry<K,V>>() {
				@Override
				public EntryPair next() {
					moveNext();
					return current;
				}
			};
		}

		@Override
		public int size() { return entries.size(); }

		private abstract class EntryIterator<E> implements Iterator<E> {

			protected Iterator<EntryPair> itr = entries.iterator();
			protected EntryPair current = null;
			public boolean hasNext() { return itr.hasNext(); }
			public abstract E next();

			/**
			 * Subclasses use this method to advance the iterator.
			 */
			protected void moveNext() {
				current = itr.next();
			}

			/**
			 * This method removes an entry and propagates the change to
			 * the underlying hashmap.
			 */
			public void remove() {
				itr.remove();
				HashMultiValueMap.this.remove(current.key, current.value);
			}

		}

	}

	public Collection<V> values() { return new ValueList(); }

	private class ValueList extends AbstractCollection<V> {

		private final EntrySet values = new EntrySet();

		@Override
		public Iterator<V> iterator() {
			return values.new EntryIterator<V>() {

				@Override
				public V next() {
					moveNext();
					return current.value;
				}

			};
		}

		@Override
		public int size() { return values.size(); }

	}

	private class EntryPair implements Entry {
		public final K key;
		public V value;
		private final Set<V> values;
		public EntryPair(K key, V value, Set<V> values) {
			this.key = key;
			this.value = value;
			this.values = values;
		}
		public Object getKey() {
			return key;
		}
		public Object getValue() {
			return value;
		}
		// Warning suppressed because we have to assume that the value
		// provided is of type V.
		@SuppressWarnings("unchecked")
		public Object setValue(Object value) throws ClassCastException {
			values.remove(value);
			values.add((V)value);
			return value;
		}
	}

}