// © The MITRE Corporation 2006
// ALL RIGHTS RESERVED
package org.mitre.mrald.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KeySet implements Comparable<KeySet> {
	public final Comparable[] keys;
	public KeySet(Comparable[] k) { this.keys = k; }
	public KeySet(ResultSet rs, int[] cols) throws SQLException,ClassCastException {
		keys = new Comparable[cols.length];
		for (int i = 0; i < cols.length; i++) {
			keys[i] = (Comparable)rs.getObject(cols[i]);
		}
	}
	@Override
	public int hashCode() {
		int hashValue = 0;
		for (Comparable key : keys) {
			hashValue ^= key.hashCode();
		}
		return hashValue;
	}
	@Override
	public boolean equals(Object obj) {
		KeySet that = (KeySet)obj;
		for (int i = 0; i < keys.length; i++) {
			if (!this.keys[i].equals(that.keys[i])) return false;
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	public int compareTo(KeySet that) {
		for (int i = 0; i < keys.length; i++) {
			int compare = this.keys[i].compareTo(that.keys[i]);
			if (compare != 0) return compare;
		}
		return 0;
	}
}