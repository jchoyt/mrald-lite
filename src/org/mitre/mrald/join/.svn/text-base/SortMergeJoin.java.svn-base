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

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

import org.mitre.mrald.util.KeySet;

import Zql.ParseException;
import Zql.ZQuery;
import Zql.ZqlJJParser;

import com.sun.rowset.CachedRowSetImpl;

public class SortMergeJoin extends Join {

	public SortMergeJoin(Source outerSource, Source innerSource, int[] outerCols, int[] innerCols) throws SQLException {
		super(outerSource, innerSource, outerCols, innerCols);
	}

	@Override
	public ResultSet execute() throws SQLException {
		if (outerSource.db.getResultSetType() != ResultSet.TYPE_FORWARD_ONLY) {
			outerSource.db = outerSource.db.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		}
		outerSource.db.setFetchSize(100);
		outerRS = createSortedResultSet(outerSource, outerCols);
		innerRS = createSortedResultSet(innerSource, innerCols);
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
		return new SortMergeResultSet();
	}

	private ResultSet createSortedResultSet(Source src, int[] cols) throws SQLException {
		try {
			String sql = src.sql;
			if (!sql.contains(";")) sql += ";";

			ZQuery query = new ZqlJJParser(new StringReader(sql)).QueryStatement();
			Vector selectCols = query.getSelect();
			Vector<Object> orderCols = new Vector<Object>();

			for (int i = 0; i < cols.length; i++) {
				// SQL uses 1-based indices, but the parser uses 0-based indices.
				int selectIndex = cols[i] - 1;
				orderCols.add(selectCols.get(selectIndex));
			}
			query.addOrderBy(orderCols);
			src.sql = query.toString();
			return src.getResultSet();
		} catch (ParseException pe) {
			throw new SQLException(pe.getMessage());
		}
	}

	private class SortMergeResultSet extends JoinResultSet {

		private KeySet outerPtr = null;
		private KeySet innerPtr = null;
		private KeySet markerPtr = null;
		private int markerIndex = -1;

		public SortMergeResultSet() throws SQLException {
			super();
		}

		@Override
		protected boolean advanceCursor() throws SQLException {
			if (!advanceInnerPtr()) return false;
			if (markerPtr == null) {
				return advanceOuterPtrAndMark();
			} else if (!innerPtr.equals(markerPtr)) {
				if (!advanceOuterPtr()) return false;
				if (outerPtr.equals(markerPtr)) {
					// Reset to marker
					innerRS.absolute(markerIndex);
					innerPtr = markerPtr;
				} else {
					return advanceOuterPtrAndMark();
				}
			}
			return true;
		}

		private boolean advanceInnerPtr() throws ClassCastException, SQLException {
			boolean result = innerRS.next();
			if (result) {
				innerPtr = new KeySet(innerRS, innerCols);
			}
			return result;
		}

		private boolean advanceOuterPtr() throws SQLException {
			boolean result = outerRS.next();
			if (result) {
				outerPtr = new KeySet(outerRS, outerCols);
			}
			return result;
		}

		private boolean advanceOuterPtrAndMark() throws SQLException {
			while (isOuterLessThanInner()) {
				boolean next = advanceOuterPtr();
				if (!next) return false;
			}
			return true;
		}

		private boolean isOuterLessThanInner() throws SQLException {
			if (outerPtr == null) return true;
			int compare = outerPtr.compareTo(innerPtr);
			markerPtr = (compare == 0) ? innerPtr : null;
			if (markerPtr != null) markerIndex = innerRS.getRow();
			return compare < 0;
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
		String sql1 = "SELECT cstrategy_id, description FROM clusterstrategies";
		Source src1 = new PostgresSource(stmt1, sql1);
		int[] col1 = {1};

		Statement stmt2 = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		stmt2.execute("SET search_path TO ziph");
//		String sql2 = "SELECT cluster_id, num_clusters, cstrategy_id FROM clustervalues WHERE num_clusters >= 8";
		String sql2 = "SELECT cluster_id, num_clusters, cstrategy_id FROM clustervalues WHERE num_clusters >= 8 AND cstrategy_id IN (1,3)";
		Source src2 = new PostgresSource(stmt2, sql2);
		int[] col2 = {3};

		SortMergeJoin join = new SortMergeJoin(src2, src1, col2, col1);
		ResultSet rs = join.execute();
		System.out.println(src1.sql);
		System.out.println(src2.sql);

//		PreparedStatement stmt = (PreparedStatement)src2.db;
//		stmt.setObject(1, new Integer(0));
//		rs = stmt.executeQuery();
		Source.print(rs);
	}

}
