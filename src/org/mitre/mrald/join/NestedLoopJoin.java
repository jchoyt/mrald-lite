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

public class NestedLoopJoin extends Join {

	public NestedLoopJoin(Source outerSource, Source innerSource, int[] outerCols, int[] innerCols) throws SQLException {
		super(outerSource, innerSource, outerCols, innerCols);
	}

	@Override
	public ResultSet execute() throws SQLException {
		if (outerSource.db.getResultSetType() != ResultSet.TYPE_FORWARD_ONLY) {
			outerSource.db = outerSource.db.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		}
		outerSource.db.setFetchDirection(100);
		innerSource.db.setFetchDirection(100);
		outerRS = outerSource.getResultSet();
		innerRS = innerSource.getResultSet();
		return new NestedLoopResultSet();
	}

	private class NestedLoopResultSet extends JoinResultSet {

		public NestedLoopResultSet() throws SQLException {
			super();
		}

		protected boolean advanceCursor() throws SQLException {
			// On the first call to this method the outer-cursor is
			// still before the first record.
			if (outerRS.isBeforeFirst()) outerRS.next();
			return advanceInnerResultSet();
		}

		private boolean advanceOuterResultSet() throws SQLException {
			// When the outer-cursor advance, the inner-cursor is reset.
			if (outerRS.next()) {
				return resetInnerResultSet();
			}
			return false;
		}

		private boolean advanceInnerResultSet() throws SQLException {
			// If the inner-cursor falls off the end, we need to
			// advance the outer-cursor.
			if (innerRS.next()) {
				return true;
			} else {
				return advanceOuterResultSet();
			}
		}

		private boolean resetInnerResultSet() throws SQLException {
			// When possible, simply reset the inner-cursor to the first record.
			if (innerRS != null && innerSource.db.getResultSetType() == ResultSet.TYPE_SCROLL_INSENSITIVE) {
				return innerRS.first();
			// Otherwise, recompute the results and move forward.
			} else {
				innerRS = innerSource.getResultSet();
				return innerRS.next();
			}
		}

	}

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

		NestedLoopJoin join = new NestedLoopJoin(src1, src2, col1, col2);
		ResultSet rs = join.execute();
		Source.print(rs);

		System.out.println("Done!");
	}

}
