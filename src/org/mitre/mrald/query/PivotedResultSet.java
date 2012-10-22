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
// (c) The MITRE Corporation 2006
// ALL RIGHTS RESERVED
package org.mitre.mrald.query;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.RowId;
import java.sql.NClob;
import java.sql.SQLXML;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * <p>
 * A pivoted result set takes a normalized database relation and converts it
 * into a matrix format, like a spreadsheet. The first column of the relation
 * indicates the row labels. The next-to-last column of the relation indicates
 * the column labels. The last column of the relation indicates the data to
 * display in each cell. The remaining columns are copied verbatim.
 * </p>
 *
 * <p>
 * The rows are sorted in the order in which they first appear in the underlying
 * relation. The columns are sorted in lexicographic order (i.e., the columns
 * are converted to strings). If there is only a single value for each cell in
 * the matrix, the data values preserve their underlying type. If there are
 * multiple values for a given cell, the data values are converted to strings
 * and separated using &lt;br/&gt; tags.
 * </p>
 *
 * <p>
 * A pivoted result set is constructed by passing it any result set. Because
 * some result sets don't support multiple passes, the constructor iterates over
 * the result set building a local copy of the result set and auxiliary
 * structures for tracking row and column headers.
 * </p>
 *
 * <p>
 * Assume the schema of the underlying result-set is RS(R,X2,X3,...,Xk,C,V). The
 * schema of the pivoted result-set is PRS(R,X2,X3,...,Xk,C1,C2,...,Cn) where
 * C1..Cn are the unique values from C. We assume that the following functional
 * dependencies hold: R-&gt;Xi for all 2&lt;=i&lt;=k. In the pivoted result-set,
 * new functional dependencies are guaranteed: R-&gt;Ci for all 1&lt;=i&lt;=n.
 *
 * @author Peter Mork
 * @version 1.0
 */
public class PivotedResultSet implements ResultSet, ResultSetMetaData {

	public final int entity_column;

	public final int attribute_column;

	public final int value_column;

	public final int base_column_count;

	public final ResultSet base_data;

	public final ResultSetMetaData base_metadata;

	/**
	 * This array contains one row for each unique row-value (indexed from 0 to
	 * R-1. It contains one new column for each unique col-value (indexed from 2
	 * to C+1. The first column stores the row-values. Columns 2 through C-2
	 * contain data copied from the underlying result-set.
	 */
	private Object[][] pivot_data;

	/**
	 * Maps each column name into the range [2,C+1] where N is the number of
	 * unique columns; the row-header is mapped to the first column.
	 */
	private HashMap<String, Integer> pivot_columns;

	/**
	 * Maps column indices from the range [1,C+1] to column names; the first
	 * column name is the row-header and the remaining column names are drawn
	 * from the data.
	 */
	private String[] pivot_column_names;

	/**
	 * Falls in the interval [-1,R] where R is the number of rows. -1 indicates
	 * before-first R indicates after-last All other values indicate the current
	 * row
	 */
	private int row_pointer = -1;

	/**
	 * Creates a new result set by pivoting an existing result set. The first
	 * column of the base data represents row labels, the second column
	 * represents column labels and the third column represents the data used to
	 * populate the pivot table.
	 *
	 * @param base_data
	 *            The data used to generate the pivot table.
	 * @throws SQLException
	 *             If any error occurs.
	 */
	public PivotedResultSet(ResultSet base_data, String[] pieces) throws SQLException {
		this.base_data = base_data;
		this.base_metadata = base_data.getMetaData();
		this.base_column_count = base_metadata.getColumnCount();
		entity_column = find(pieces[PivotFilter.ENTITY_FIELD]);
		attribute_column = find(pieces[PivotFilter.ATTRIBUTE_FIELD]);
		value_column = find(pieces[PivotFilter.VALUE_FIELD]);
		buildPivotTable();
	}

	/**
	 * @param field
	 *            The name of a query field.
	 * @return The index of that field in the resultset or 0 if it's not found.
	 * @throws SQLException
	 *             If any error occurs.
	 */
	public int find(String field) throws SQLException {
		for (int i = 1; i <= base_column_count; i++) {
			if (base_metadata.getColumnName(i).equalsIgnoreCase(field)) {
				return i;
			}
		}
		throw new SQLException("Field not found: " + field);
	}

	private void buildPivotTable() throws SQLException {
		// Preserve the order in which tuples and rows are visited.
		ArrayList<Object[]> tuples = new ArrayList<Object[]>();
		HashMap<Object, Integer> rows = new HashMap<Object, Integer>();
		int row_index = 0;
		// Sort the columns in lexicographic order.
		TreeSet<String> cols = new TreeSet<String>();

		// Iterate over the result-set and extract the tuples.
		while (base_data.next()) {
			Object row = base_data.getObject(entity_column);
			String col = base_data.getString(attribute_column);
			Object[] tuple = new Object[base_column_count + 1];
			tuple[0] = null;
			for (int i = 1; i <= base_column_count; i++) {
				tuple[i] = base_data.getObject(i);
			}
			tuples.add(tuple);
			// Map each new row to the next integer.
			if (!rows.containsKey(row))
				rows.put(row, new Integer(row_index++));
			// Insert each new col into a sorted set.
			if (!cols.contains(col))
				cols.add(col);
		}

		// Convert the sorted set of columns into a mapping from name to index.
		buildPivotColumns(cols);

		// Build a tuple for each row-value.
		pivot_data = new Object[rows.size()][];
		for (Object row : rows.keySet()) {
			int index = rows.get(row).intValue();
			Object[] tuple = new Object[getColumnCount() + 1 /* 0=>null */];
			pivot_data[index] = tuple;
		}

		// Copy the values into the pivot table.
		populatePivotTable(tuples, rows);
	}

	private void buildPivotColumns(TreeSet<String> cols) throws SQLException {
		int col_index = 1;
		// Add the row-header to the column mapping.
		int num_columns = cols.size() + base_column_count - 2;
		pivot_columns = new HashMap<String, Integer>(num_columns);
		pivot_column_names = new String[num_columns + 1 /* 0=>null */];
		for (int i = 1; i <= base_column_count; i++) {
			if (i == attribute_column || i == value_column)
				continue;
			pivot_columns.put(base_metadata.getColumnName(i), new Integer(
					col_index));
			pivot_column_names[col_index] = base_metadata.getColumnName(i);
			++col_index;
		}
		// Add the column-headers to the column mapping.
		for (String col : cols) {
			pivot_columns.put(col, new Integer(col_index));
			pivot_column_names[col_index] = col;
			++col_index;
		}
	}

	private void populatePivotTable(ArrayList<Object[]> tuples,
			HashMap<Object, Integer> rows) throws SQLException {
		for (Object[] tuple : tuples) {

			// Find the correct row for the value.
			Object tuple_row = tuple[entity_column];
			Object[] pivot_row = pivot_data[rows.get(tuple_row).intValue()];

			// Copy most of the data from tuples to pivot_data.
			int shift = 0;
			for (int i = 1; i <= base_column_count; i++) {
				if (i == attribute_column || i == value_column) {
					++shift;
				} else {
					pivot_row[i - shift] = tuple[i];
				}
			}

			// Find the correct column for the value.
			String col = (String) tuple[attribute_column];
			int col_index = pivot_columns.get(col).intValue();

			// Determine if a value already exists.
			Object val = tuple[value_column];
			Object old = pivot_row[col_index];
			// If so, append the new value to the old.
			if (old != null) {
				val = old.toString() + "<br/>" + val.toString();
			}

			// Copy the value into the pivot table.
			pivot_row[col_index] = val;
		}
	}

	public boolean next() throws SQLException {
		return relative(1);
	}

	public void close() throws SQLException {
		base_data.close();
		pivot_column_names = null;
		pivot_columns = null;
		pivot_data = null;
	}

	public boolean wasNull() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public String getString(int columnIndex) throws SQLException {
		Object result = pivot_data[row_pointer][columnIndex];
		return (result != null) ? result.toString() : null;
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public byte getByte(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public short getShort(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public int getInt(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public long getLong(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public float getFloat(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public double getDouble(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Date getDate(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Time getTime(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		Object result = pivot_data[row_pointer][columnIndex];
		if (!(result instanceof Timestamp)) {
			if (result instanceof Date) {
				result = new Timestamp(((Date)result).getTime());
			} else {
				try {
					java.util.Date date = DateFormat.getDateInstance().parse(result.toString());
					result = new Timestamp(date.getTime());
				} catch (ParseException e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
		return (Timestamp)result;
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public String getString(String columnName) throws SQLException {
		return getString(findColumn(columnName));
	}

	public boolean getBoolean(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public byte getByte(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public short getShort(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public int getInt(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public long getLong(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public float getFloat(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public double getDouble(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public BigDecimal getBigDecimal(String columnName, int scale)
			throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public byte[] getBytes(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Date getDate(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Time getTime(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Timestamp getTimestamp(String columnName) throws SQLException {
		return getTimestamp(findColumn(columnName));
	}

	public InputStream getAsciiStream(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public InputStream getUnicodeStream(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public InputStream getBinaryStream(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public SQLWarning getWarnings() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void clearWarnings() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public String getCursorName() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return this;
	}

	public Object getObject(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Object getObject(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public int findColumn(String columnName) throws SQLException {
		Integer result = pivot_columns.get(columnName);
		if (result == null) throw new SQLException("No such column: " + columnName);
		return result.intValue();
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Reader getCharacterStream(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		Object result = pivot_data[row_pointer][columnIndex];
		if (!(result == null || result instanceof BigDecimal)) {
			result = new BigDecimal(result.toString());
		}
		return (BigDecimal) result;
	}

	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return getBigDecimal(findColumn(columnName));
	}

	public boolean isBeforeFirst() throws SQLException {
		return row_pointer < 0;
	}

	public boolean isAfterLast() throws SQLException {
		return row_pointer >= pivot_data.length;
	}

	public boolean isFirst() throws SQLException {
		return row_pointer == 0;
	}

	public boolean isLast() throws SQLException {
		return row_pointer == pivot_data.length - 1;
	}

	public void beforeFirst() throws SQLException {
		row_pointer = -1;
	}

	public void afterLast() throws SQLException {
		row_pointer = pivot_data.length;
	}

	public boolean first() throws SQLException {
		row_pointer = 0;
		return pivot_data.length > 0;
	}

	public boolean last() throws SQLException {
		row_pointer = pivot_data.length - 1;
		return pivot_data.length > 0;
	}

	public int getRow() throws SQLException {
		return row_pointer + 1;
	}

	public boolean absolute(int row) throws SQLException {
		row_pointer = row - 1;
		return adjustedRowPointer();
	}

	public boolean relative(int rows) throws SQLException {
		row_pointer += rows;
		return adjustedRowPointer();
	}

	private boolean adjustedRowPointer() {
		if (row_pointer < -1)
			row_pointer = -1;
		if (row_pointer > pivot_data.length)
			row_pointer = pivot_data.length;
		return (row_pointer >= 0 && row_pointer < pivot_data.length);
	}

	public boolean previous() throws SQLException {
		return relative(-1);
	}

	public void setFetchDirection(int direction) throws SQLException {
		/* It doesn't matter. Everything is cached in memory. */
	}

	public int getFetchDirection() throws SQLException {
		return ResultSet.FETCH_UNKNOWN; /* Order is irrelevant. */
	}

	public void setFetchSize(int rows) throws SQLException {
		/* It doesn't matter. Everything is cached in memory. */
	}

	public int getFetchSize() throws SQLException {
		return 1; /* Fetch size is irrelevant. */
	}

	public int getType() throws SQLException {
		return ResultSet.TYPE_SCROLL_SENSITIVE; /*
												 * Changes made by others should
												 * be seen.
												 */
	}

	public int getConcurrency() throws SQLException {
		return ResultSet.CONCUR_READ_ONLY;
	}

	public boolean rowUpdated() throws SQLException {
		return false;
	}

	public boolean rowInserted() throws SQLException {
		return false;
	}

	public boolean rowDeleted() throws SQLException {
		return false;
	}

	public void updateNull(int columnIndex) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateObject(int columnIndex, Object x, int scale)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateNull(String columnName) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateByte(String columnName, byte x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateShort(String columnName, short x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateInt(String columnName, int x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateLong(String columnName, long x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateFloat(String columnName, float x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateDouble(String columnName, double x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateBigDecimal(String columnName, BigDecimal x)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateString(String columnName, String x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateBytes(String columnName, byte[] x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateDate(String columnName, Date x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateTime(String columnName, Time x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateTimestamp(String columnName, Timestamp x)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateAsciiStream(String columnName, InputStream x, int length)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateBinaryStream(String columnName, InputStream x, int length)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateCharacterStream(String columnName, Reader reader,
			int length) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateObject(String columnName, Object x, int scale)
			throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateObject(String columnName, Object x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void insertRow() throws SQLException {
		throw new SQLException("A pivoted result-set cannot be inserted into.");
	}

	public void updateRow() throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void deleteRow() throws SQLException {
		throw new SQLException("A pivoted result-set cannot be deleted from.");
	}

	public void refreshRow() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void cancelRowUpdates() throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void moveToInsertRow() throws SQLException {
		throw new SQLException("A pivoted result-set cannot be inserted into.");
	}

	public void moveToCurrentRow() throws SQLException {
	}

	public Statement getStatement() throws SQLException {
		return base_data.getStatement();
	}

	public Object getObject(int arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Ref getRef(int i) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Blob getBlob(int i) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Clob getClob(int i) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Array getArray(int i) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Object getObject(String arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		throw new SQLException("This method not implemented.");
	}

    public <T> T getObject(String columnName, Class<T> type) throws SQLException {
		throw new SQLException("This method not implemented.");
    }

    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		throw new SQLException("This method not implemented.");
    }

	public Ref getRef(String colName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Blob getBlob(String colName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Clob getClob(String colName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Array getArray(String colName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Date getDate(String columnName, Calendar cal) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Time getTime(String columnName, Calendar cal) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Timestamp getTimestamp(String columnName, Calendar cal)
			throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public URL getURL(int columnIndex) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public URL getURL(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateRef(String columnName, Ref x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateBlob(String columnName, Blob x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateClob(String columnName, Clob x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public void updateArray(String columnName, Array x) throws SQLException {
		throw new SQLException("A pivoted result-set cannot be updated.");
	}

	public int getColumnCount() throws SQLException {
		return pivot_columns.size();
	}

	private int getAdjustedColumnIndex(int column) {
		int shift = ((column >= attribute_column) ? 1 : 0)
				+ ((column >= value_column) ? 1 : 0);
		return (column > base_column_count - 2) ? value_column : column + shift;
	}

	/**
	 * @return the name of the attribute used to generate column headers.
	 * @throws SQLException
	 */
	public String getValueColumnName() throws SQLException {
		return base_metadata.getColumnName(value_column);
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		return base_metadata.isAutoIncrement(getAdjustedColumnIndex(column));
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		return base_metadata.isCaseSensitive(getAdjustedColumnIndex(column));
	}

	public boolean isSearchable(int column) throws SQLException {
		return base_metadata.isSearchable(getAdjustedColumnIndex(column));
	}

	public boolean isCurrency(int column) throws SQLException {
		return base_metadata.isCurrency(getAdjustedColumnIndex(column));
	}

	public int isNullable(int column) throws SQLException {
		return base_metadata.isNullable(getAdjustedColumnIndex(column));
	}

	public boolean isSigned(int column) throws SQLException {
		return base_metadata.isSigned(getAdjustedColumnIndex(column));
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		return base_metadata
				.getColumnDisplaySize(getAdjustedColumnIndex(column));
	}

	public String getColumnLabel(int column) throws SQLException {
		return base_metadata.getColumnLabel(getAdjustedColumnIndex(column));
		// if (column < COL_INDEX) return base_metadata.getColumnLabel(column);
		// return base_metadata.getColumnName(getAdjustedColumnIndex(column));
	}

	public String getColumnName(int column) throws SQLException {
		return pivot_column_names[column];
	}

	public String getSchemaName(int column) throws SQLException {
		return base_metadata.getSchemaName(getAdjustedColumnIndex(column));
	}

	public int getPrecision(int column) throws SQLException {
		return base_metadata.getPrecision(getAdjustedColumnIndex(column));
	}

	public int getScale(int column) throws SQLException {
		return base_metadata.getScale(getAdjustedColumnIndex(column));
	}

	public String getTableName(int column) throws SQLException {
		return base_metadata.getTableName(getAdjustedColumnIndex(column));
	}

	public String getCatalogName(int column) throws SQLException {
		return base_metadata.getCatalogName(getAdjustedColumnIndex(column));
	}

	public int getColumnType(int column) throws SQLException {
		return base_metadata.getColumnType(getAdjustedColumnIndex(column));
	}

	public String getColumnTypeName(int column) throws SQLException {
		return base_metadata.getColumnTypeName(getAdjustedColumnIndex(column));
	}

	public boolean isReadOnly(int column) throws SQLException {
		return true;
	}

	public boolean isWritable(int column) throws SQLException {
		return false;
	}

	public boolean isDefinitelyWritable(int column) throws SQLException {
		return false;
	}

        public void updateClob(int columnIndex, Reader reader) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateClob(int columnIndex, Reader reader, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateClob(String columnLabel, Reader reader) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateClob(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateNClob(int columnIndex, NClob nClob) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateNClob(int columnIndex, Reader reader) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateNClob(String columnLabel, NClob nClob) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateNClob(String columnLabel, Reader reader) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable.");}
        public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException { 			throw new SQLException("Join ResultSets are not updateable."); 		}
        public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException { 			throw new SQLException("Join ResultSets are not updateable."); 		}
        public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException { 			throw new SQLException("Join ResultSets are not updateable."); 		}
        public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException { 			throw new SQLException("Join ResultSets are not updateable."); 		}
        public void updateCharacterStream(int columnIndex, Reader x) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateNString(int columnIndex, String nString) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateNString(String columnLabel, String nString) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public Reader getNCharacterStream(int columnIndex) throws SQLException { throw new SQLException("JoinResultSets do not support this access method."); }
        public Reader getNCharacterStream(String columnLabel) throws SQLException { throw new SQLException("JoinResultSets do not support this access method."); }
        public String getNString(int columnIndex) throws SQLException { throw new SQLException("JoinResultSets do not support this access method."); }
        public String getNString(String columnLabel) throws SQLException { throw new SQLException("JoinResultSets do not support this access method."); }
        public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException { throw new SQLException("JoinResultSets do not support this access method."); }
        public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException { throw new SQLException("JoinResultSets do not support this access method."); }
        public SQLXML getSQLXML(int columnIndex) throws SQLException { throw new SQLException("JoinResultSets do not support this access method."); }
        public SQLXML getSQLXML(String columnLabel) throws SQLException { throw new SQLException("JoinResultSets do not support this access method."); }
        public NClob getNClob(int columnIndex) throws SQLException { throw new SQLException("JoinResultSets do not support this access method."); }
        public NClob getNClob(String columnLabel) throws SQLException { throw new SQLException("JoinResultSets do not support this access method."); }
        public boolean isClosed()  { return false; }
        public int getHoldability() { return CLOSE_CURSORS_AT_COMMIT; }
        public void updateRowId(int columnIndex, RowId x) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public void updateRowId(String columnName, RowId x) throws SQLException { throw new SQLException("Join ResultSets are not updateable."); }
        public RowId getRowId(int columnIndex)  throws SQLException { throw new SQLException("JoinResultSets cannot access RowId"); }
        public RowId getRowId(String columnLabel)  throws SQLException { throw new SQLException("JoinResultSets cannot access RowId"); }
        public boolean isWrapperFor(Class<?> iface)  throws SQLException { return false; }
        public <T> T unwrap(Class<T> iface) throws SQLException { throw new SQLException("Unimplemented feature"); }


        public String getColumnClassName(int column) throws SQLException {
		return base_metadata.getColumnClassName(getAdjustedColumnIndex(column));
	}

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		Class.forName("org.postgresql.Driver");
		Connection db = DriverManager.getConnection(
				"jdbc:postgresql://mumps.mitre.org:5432/ChronicFatigue",
				"postgres", "postgres");
		Statement stmt = db.createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT bmi, yrs_ill, dob, abt_id, sf36_value, sex, sf36_code, age FROM sf36summaryscores NATURAL JOIN patientinfo;");
		// ResultSet rs = stmt.executeQuery("SELECT abt_id, sf36_code,
		// sf36_value FROM sf36summaryscores WHERE abt_id=1");
		// System.out.println(rs.last());
		String[] eav = {"patientinfo", "abt_id", "Numeric", "sf36summaryscores", "sf36_code", "String", "sf36summaryscores", "sf36_value", "Numeric"};
		PivotedResultSet pvt = new PivotedResultSet(rs, eav);
		for (int i = 1; i <= pvt.getColumnCount(); i++) {
			System.out.print(pvt.getColumnName(i));
			System.out.print("\t");
		}
		System.out.println();
		while (pvt.next()) {
			for (int j = 1; j <= pvt.getColumnCount(); j++) {
				System.out.print(pvt.getString(j));
				System.out.print("\t");
			}
			System.out.println();
		}
		System.out
				.println(pvt.getRow() - 1 /*
											 * Because we've moved off the end
											 * to the result set"
											 */);
		System.out.println("Done!");
	}

}