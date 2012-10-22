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

import Zql.ParseException;

public class JoinTest {

	/**
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {
		Class.forName("org.postgresql.Driver");
		Join join;

		// Inner db.
		Connection db1 = DriverManager.getConnection(
				"jdbc:postgresql://brainsrv2.mitre.org:5432/oooiCommon",
				"postgres", "postgres");
		Statement stmt1 = db1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//		String sql1 = "select oooidataid , carrier, flightno from oooicommon where deptairport = 'IAD' or arrairport = 'IAD'";
		String sql1 = "select oooidataid , carrier, flightno from oooicommon";
		Source src1 = new PostgresSource(stmt1, sql1);
		int[] col1 = {1};

		// Outer db.
		Connection db2 = DriverManager.getConnection(
				"jdbc:postgresql://brainsrv2.mitre.org:5432/oooiDerived",
				"postgres", "postgres");
		Statement stmt2 = db2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//		String sql2 = "SELECT offtime, intime, oooidataid FROM oooiderived where oooidataid like '200606%'";
		String sql2 = "SELECT offtime, intime, oooidataid FROM oooiderived";
		Source src2 = new PostgresSource(stmt2, sql2);
		int[] col2 = {3};

//		join = new HashJoin(src2, src1, col2, col1);
//		test(join);

//		join  = new SortMergeJoin(src2, src1, col2, col1);
//		test(join);

//		join = new IndexJoin(src1, src2, col1, col2);
//		test(join);

//		join = new NestedLoopJoin(src2, src1, col2, col1);
//		test(join);

		join = SimpleOptimizer.chooseJoinAlgorithm(src1, src2, col1, col2, true);
		test(join);

		System.out.println("Done!");
	}

	private static void test(Join algorithm) {
		long stop, start = System.currentTimeMillis();
		try {
			ResultSet rs = algorithm.execute();
			while (rs.next()) {
				if (rs.getRow() % 1000 == 0) System.out.print(".");
			}
			stop = System.currentTimeMillis();
			System.out.println((stop - start) + "ms");
//			Source.print(rs);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
