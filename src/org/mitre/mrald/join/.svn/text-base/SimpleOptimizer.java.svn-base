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
import java.sql.SQLException;
import java.util.Vector;

import Zql.ParseException;
import Zql.ZQuery;
import Zql.ZqlJJParser;

public class SimpleOptimizer {

	public static Join chooseJoinAlgorithm(Source one, Source two, int[] col1, int[] col2, boolean useIndex) throws ParseException, SQLException {
		long memSize = Runtime.getRuntime().freeMemory();
		one.estimateSize();
		long size1 = one.length * one.width;
		two.estimateSize();
		long size2 = two.length * two.width;
		Source small = (size1 < size2) ? one : two;
		int[] smallC = (size1 < size2) ? col1 : col2;
		Source large = (size1 < size2) ? two: one;
		int[] largeC = (size1 < size2) ? col2 : col1;
		if (Math.min(size1, size2) < memSize) {
			if (noOrder(one.sql) && noOrder(two.sql)) {
				return new SortMergeJoin(large, small, largeC, smallC);
			} else {
				return new HashJoin(large, small, largeC, smallC);
			}
		}
		if (useIndex) {
			return new IndexJoin(small, large, smallC, largeC);
		}
		return new NestedLoopJoin(large, small, largeC, smallC);
	}

	private static boolean noOrder(String sql) throws ParseException {
		if (!sql.contains(";")) sql += ";";
		ZQuery query = new ZqlJJParser(new StringReader(sql)).QueryStatement();
		Vector orderCols = query.getOrderBy();
		return (orderCols == null || orderCols.size() == 0);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	// TODO Auto-generated method stub

	}

}
