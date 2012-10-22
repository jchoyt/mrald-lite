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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p>A Source is a four-tuple that information regarding how one could retrieve
 * a ResultSet from a database.  The first component is a database statement
 * object from an existing connection.  The second component is the SQL code
 * used to create the ResultSet; this code might (in some case) be modified
 * by a Join implementation.  The remaining components indicate the expected
 * size of the result set in terms of the number of rows (length) and the average
 * number of bytes per row (width).  If these values aren't known, the default
 * values are 0.</p>
 * @author Peter Mork
 * @version 1.0
 */
public abstract class Source {

	public Statement db;
	public String sql;
	public int length;
	public int width;

	/**
	 * @return The result of running the query; each time this method
	 * is called the result set is rerun.
	 * @throws SQLException
	 */
	public ResultSet getResultSet() throws SQLException {
		return db.executeQuery(sql);
	}

	/**
	 * In many cases, the length and width can be estimated by the query
	 * optimizer.  This method is to be implemented using database-specific
	 * code.
	 * @throws SQLException
	 */
	public abstract void estimateSize() throws SQLException;

	/**
	 * Writes a result set to the console, including row count.
	 * @param rs The result set to dump.
	 * @throws SQLException
	 */
	public static final void print(ResultSet rs) throws SQLException {
		System.out.print("Row\t");
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
			System.out.print(rs.getMetaData().getColumnName(i));
			System.out.print(i == rs.getMetaData().getColumnCount() ? '\n' : '\t');
		}
		int n = 0;
		while (rs.next()) {
			n++;
			System.out.print(rs.getRow() + "\t");
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				System.out.print(rs.getString(i));
				System.out.print(i == rs.getMetaData().getColumnCount() ? '\n' : '\t');
			}
		}
		System.out.println("(" + n + " rows)");
	}

	public Source(Statement db, String sql, int length, int width) {
		this.db = db;
		this.sql = sql;
		this.length = length;
		this.width = width;
	}

	public Source(Statement db, String sql) {
		this(db, sql, 0, 0);
	}

	public void close() throws SQLException {
		db.close();
	}

}
