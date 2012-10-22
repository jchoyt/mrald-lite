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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import Zql.ParseException;
import Zql.ZConstant;
import Zql.ZExp;
import Zql.ZExpression;
import Zql.ZQuery;
import Zql.ZqlJJParser;

public class IndexJoin extends Join {

	public IndexJoin(Source outerSource, Source innerSource, int[] outerCols, int[] innerCols) throws SQLException {
		super(outerSource, innerSource, outerCols, innerCols);
	}

	@Override
	public ResultSet execute() throws SQLException {
		// Manipulate the sql!
		if (outerSource.db.getResultSetType() != ResultSet.TYPE_FORWARD_ONLY) {
			outerSource.db = outerSource.db.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		}
		outerSource.db.setFetchSize(100);
		outerRS = outerSource.getResultSet();
		try {
			String sql = innerSource.sql;
			if (!sql.contains(";")) sql += ";";

			ZQuery query = new ZqlJJParser(new StringReader(sql)).QueryStatement();
			Vector selectCols = query.getSelect();
			ZExp whereClause = query.getWhere();
			if (whereClause == null) whereClause = new ZConstant("TRUE", ZConstant.NUMBER);
			ZConstant placeHolder = new ZConstant("?", ZConstant.NUMBER);
			ZExpression and = new ZExpression("AND", whereClause);

			for (int i = 0; i < innerCols.length; i++) {
				// SQL uses 1-based indices, but the parser uses 0-based indices.
				int selectIndex = innerCols[i] - 1;
				String columnName = selectCols.get(selectIndex).toString();
				ZConstant column = new ZConstant(columnName, ZConstant.COLUMNNAME);
				ZExpression clause = new ZExpression("=", column, placeHolder);
				and.addOperand(clause);
			}

			query.addWhere(and);
			sql = innerSource.sql = query.toString();
			PreparedStatement db = innerSource.db.getConnection().prepareStatement(sql);
			innerSource.db = db;
			return new IndexResultSet(null, db.getMetaData());
		} catch (ParseException pe) {
			throw new SQLException(pe.getMessage());
		}
	}

	private class IndexResultSet extends JoinResultSet {

		public IndexResultSet(ResultSetMetaData outer, ResultSetMetaData inner) throws SQLException {
			super(outer, inner);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected boolean testCursor() throws SQLException {
			return true;
		}

		@Override
		protected boolean advanceCursor() throws SQLException {
			if (innerRS != null && innerRS.next()) return true;
			while (outerRS.next()) {
				PreparedStatement db = (PreparedStatement)innerSource.db;
				for (int i = 0; i < outerCols.length; i++) {
					Object join = outerRS.getObject(outerCols[i]);
					db.setObject(i+1,join);
				}
				innerRS = db.executeQuery();
				if (innerRS.next()) return true;
			}

			// TODO Auto-generated method stub
			return false;
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
		String sql2 = "SELECT cluster_id, num_clusters, cstrategy_id FROM clustervalues";
		Source src2 = new PostgresSource(stmt2, sql2);
		int[] col2 = {3};

		IndexJoin join = new IndexJoin(src1, src2, col1, col2);
		ResultSet rs = join.execute();
		System.out.println(src2.sql);

//		PreparedStatement stmt = (PreparedStatement)src2.db;
//		stmt.setObject(1, new Integer(0));
//		rs = stmt.executeQuery();
		Source.print(rs);
	}

}
