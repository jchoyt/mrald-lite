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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostgresSource extends Source {

	// This pattern pulls out the row count as group 1 and the width as group 2.
	private static final Pattern explain = Pattern.compile("rows=(\\d+).*?width=(\\d+)");

	public PostgresSource(Statement db, String sql, int length, int width) {
		super(db, sql, length, width);
	}

	public PostgresSource(Statement db, String sql) {
		super(db, sql);
	}

	@Override
	public void estimateSize() throws SQLException {
		if (length > 0 && width > 0) return;
		// EXPLAIN is the Postgres keyword to optimize a query.
		ResultSet rs = db.executeQuery("EXPLAIN " + sql);
		if (rs.next()) {
			String result = rs.getString(1);
			// The regex (above) extracts the number of rows and average number of bytes.
			Matcher m = explain.matcher(result);
			if (m.find()) {
				length = Integer.parseInt(m.group(1));
				width = Integer.parseInt(m.group(2));
			} else {
				throw new SQLException("Unable to find estimates for " + sql);
			}
		} else {
			throw new SQLException("Unable to explain " + sql);
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection db = DriverManager.getConnection(
				"jdbc:postgresql://brainsrv2.mitre.org:5432/digests",
				"postgres", "postgres");
		Statement stmt = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		stmt.execute("SET search_path TO ziph");
		PostgresSource pg = new PostgresSource(stmt, "SELECT * FROM clustervalues");
		pg.estimateSize();
		System.out.println(pg.length + " rows");
		System.out.println(pg.width + " avg width");
	}

}
