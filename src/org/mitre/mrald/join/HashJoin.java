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
package org.mitre.mrald.join;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;

import javax.sql.rowset.CachedRowSet;

import org.mitre.mrald.util.HashMultiValueMap;
import org.mitre.mrald.util.KeySet;

import com.sun.rowset.CachedRowSetImpl;

public class HashJoin extends Join {

	private HashMultiValueMap<KeySet,Integer> innerHash;

	public HashJoin(Source outerSource, Source innerSource, int[] outerCols, int[] innerCols) throws SQLException {
		super(outerSource, innerSource, outerCols, innerCols);
	}

	@Override
	public ResultSet execute() throws SQLException {
		innerSource.estimateSize();
		innerHash = new HashMultiValueMap<KeySet,Integer>(innerSource.length);
		if (outerSource.db.getResultSetType() != ResultSet.TYPE_FORWARD_ONLY) {
			outerSource.db = outerSource.db.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		}
		outerSource.db.setFetchSize(100);
		outerRS = outerSource.getResultSet();
		innerRS = innerSource.getResultSet();
		if (innerSource.db.getResultSetType() != ResultSet.TYPE_SCROLL_INSENSITIVE) {
			// Due to a bug in CachedRowSetImpl, the first invocation of the constructor
			// might fail, but the second one will succeed.
			CachedRowSet cache;
			try {
				cache = new CachedRowSetImpl();
			} catch (NullPointerException npe) {
				cache = new CachedRowSetImpl();
			}
			cache.populate(innerRS);
			innerRS.close();
			innerRS = cache;
		}
		populateHash();
		return new HashResultSet();
	}

	private void populateHash() throws SQLException {
		while (innerRS.next()) {
			KeySet key = new KeySet(innerRS, innerCols);
			innerHash.put(key, new Integer(innerRS.getRow()));
		}
	}

	private class HashResultSet extends JoinResultSet {

		public HashResultSet() throws SQLException {
			super();
		}

		Iterator<Integer> values = null;

		protected boolean advanceCursor() throws SQLException {
			// If the values iterator still has values, advance it.
			if (values != null && values.hasNext()) {
				advanceIterator();
			} else {
				if (!outerRS.next()) return false;
				KeySet key = new KeySet(outerRS, outerCols);
				Set<Integer> lookup = innerHash.getAll(key);
				values = (lookup == null ? null : lookup.iterator());
				advanceIterator();
			}
			return true;
		}

		protected void advanceIterator() throws SQLException {
			innerRS.absolute(values == null ? -1 : values.next().intValue());
		}

	}

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection db = DriverManager.getConnection(
				"jdbc:postgresql://brainsrv2.mitre.org:5432/digests",
				"postgres", "postgres");

		Statement stmt1 = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		stmt1.execute("SET search_path TO ziph");
		String sql1 = "SELECT * FROM clusterstrategies";
		Source src1 = new PostgresSource(stmt1, sql1);
		int[] col1 = {1};

		Statement stmt2 = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		stmt2.execute("SET search_path TO ziph");
		String sql2 = "SELECT * FROM clustervalues";
		Source src2 = new PostgresSource(stmt2, sql2);
		int[] col2 = {3};

		HashJoin join = new HashJoin(src1, src2, col1, col2);
		ResultSet rs = join.execute();
		Source.print(rs);

		System.out.println("Done!");
	}

}
