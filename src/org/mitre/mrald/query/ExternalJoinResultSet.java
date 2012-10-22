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


import java.util.Calendar;
import java.util.Map;

public class ExternalJoinResultSet implements ResultSet, ResultSetMetaData {

	public enum JoinType {
		INNER,
//		OUTER,
//		LEFT,
//		RIGHT,
//		NATURAL
	}

	private final ResultSet leftRS;
	private final ResultSet rightRS;
	private final int[] leftCols;
	private final int[] rightCols;
//	private int leftPos = -1;
//	private int rightPos = -1;
//	private final JoinType type;

	protected ExternalJoinResultSet(ResultSet left, int[] lJoin, ResultSet right, int[] rJoin, JoinType how) throws SQLException {
		leftRS = left;
		leftCols = lJoin;
		rightRS = right;
		rightCols = rJoin;
//		type = how;
		if (leftCols.length != rightCols.length) {
			throw new SQLException("Column mismatch.");
		}
	}

	// TODO: Create a constructor that uses column names.
	// TODO: Create a constructor that implements a NATURAL JOIN.

	// BIG ASSUMPTION: There is a 1-N relationship from left to right.
	// BIG ASSUMPTION: Every record on the right has a parent on the left.

	public boolean next() throws SQLException {
		// Advance the right pointer, if the result is equal output it.
		if (leftRS.isBeforeFirst()) leftRS.next();
		if (rightRS.next()) {
			while (!testCursor()) {
				leftRS.next();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return True if the tuples at the current cursor position can be joined.
	 * @throws SQLException
	 */
	private boolean testCursor() throws SQLException {
		for (int i = 0; i < leftCols.length; i++) {
			if (!testCursorAtColumn(i)) return false;
		}
		return true;
	}

	/**
	 * @param index An index into the arrays of join columns.
	 * @return True if the tuples at the current cursor position partially match.
	 * @throws SQLException
	 */
	private boolean testCursorAtColumn(int index) throws SQLException {
		Object left = leftRS.getObject(leftCols[index]);
		Object right = rightRS.getObject(rightCols[index]);
		if (leftRS.wasNull() || rightRS.wasNull()) return false;
		return left.equals(right);
	}

	private ResultSet lookupRS(int col) throws SQLException {
		// TODO: Fix for NATURAL JOIN.
		return (col <= leftRS.getMetaData().getColumnCount()) ? leftRS : rightRS;
	}

	private int lookupCol(int col) throws SQLException {
		// TODO: Fix for NATURAL JOIN.
		int leftCols = leftRS.getMetaData().getColumnCount();
		return (col <= leftCols) ? col : col - leftCols;
	}

	public void close() throws SQLException {
		try {
			leftRS.close();
		} catch (SQLException e) {
			rightRS.close();
			throw e;
		}
	}

	public boolean wasNull() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public String getString(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getString(lookupCol(columnIndex));
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getBoolean(lookupCol(columnIndex));
	}

	public byte getByte(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getByte(lookupCol(columnIndex));
	}

	public short getShort(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getShort(lookupCol(columnIndex));
	}

	public int getInt(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getInt(lookupCol(columnIndex));
	}

	public long getLong(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getLong(lookupCol(columnIndex));
	}

	public float getFloat(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getFloat(lookupCol(columnIndex));
	}

	public double getDouble(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getDouble(lookupCol(columnIndex));
	}

	@Deprecated
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return lookupRS(columnIndex).getBigDecimal(lookupCol(columnIndex), scale);
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getBytes(lookupCol(columnIndex));
	}

	public Date getDate(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getDate(lookupCol(columnIndex));
	}

	public Time getTime(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getTime(lookupCol(columnIndex));
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getTimestamp(lookupCol(columnIndex));
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getAsciiStream(lookupCol(columnIndex));
	}

	@Deprecated
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getUnicodeStream(lookupCol(columnIndex));
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getBinaryStream(lookupCol(columnIndex));
	}

	public String getString(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getString(lookupCol(columnIndex));
	}

	public boolean getBoolean(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getBoolean(lookupCol(columnIndex));
	}

	public byte getByte(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getByte(lookupCol(columnIndex));
	}

	public short getShort(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getShort(lookupCol(columnIndex));
	}

	public int getInt(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getInt(lookupCol(columnIndex));
	}

	public long getLong(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getLong(lookupCol(columnIndex));
	}

	public float getFloat(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getFloat(lookupCol(columnIndex));
	}

	public double getDouble(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getDouble(lookupCol(columnIndex));
	}

	@Deprecated
	public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getBigDecimal(lookupCol(columnIndex), scale);
	}

	public byte[] getBytes(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getBytes(lookupCol(columnIndex));
	}

	public Date getDate(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getDate(lookupCol(columnIndex));
	}

	public Time getTime(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getTime(lookupCol(columnIndex));
	}

	public Timestamp getTimestamp(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getTimestamp(lookupCol(columnIndex));
	}

	public InputStream getAsciiStream(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getAsciiStream(lookupCol(columnIndex));
	}

	@Deprecated
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getUnicodeStream(lookupCol(columnIndex));
	}

	public InputStream getBinaryStream(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getBinaryStream(lookupCol(columnIndex));
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
		return lookupRS(columnIndex).getObject(lookupCol(columnIndex));
	}

	public Object getObject(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getObject(lookupCol(columnIndex));
	}

	public int findColumn(String columnName) throws SQLException {
		// TODO: Currently works only for NATURAL JOIN.
		int col = -1;
		try {
			col = leftRS.findColumn(columnName);
		} catch (SQLException sqle) {
			col = rightRS.findColumn(columnName);
		}
		if (col == -1) throw new SQLException("Column not found.");
		return col;
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getCharacterStream(lookupCol(columnIndex));
	}

	public Reader getCharacterStream(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getCharacterStream(lookupCol(columnIndex));
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getBigDecimal(lookupCol(columnIndex));
	}

	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getBigDecimal(lookupCol(columnIndex));
	}

	public boolean isBeforeFirst() throws SQLException {
		// TODO: Fix for general case.
		return leftRS.isBeforeFirst();
	}

	public boolean isAfterLast() throws SQLException {
		// TODO: Fix for general case.
		return leftRS.isAfterLast();
	}

	public boolean isFirst() throws SQLException {
		// TODO: Fix for general case.
		return rightRS.isFirst();
	}

	public boolean isLast() throws SQLException {
		// TODO: Fix for general case.
		return rightRS.isLast();
	}

	public void beforeFirst() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void afterLast() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public boolean first() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	// This method cannot be supported: if last were supported, it would
	// have to advance the cursors to see if there is another match.
	// However, consider this sequence: getInt(1); if(!last()) getInt(1);
	// The two calls to getInt are expected to return the same value.
	// Thus, last() would need to probe into the cursors, without changing
	// their current values.
	public boolean last() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public int getRow() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public boolean absolute(int row) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public boolean relative(int rows) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public boolean previous() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void setFetchDirection(int direction) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public int getFetchDirection() throws SQLException {
		return ResultSet.FETCH_FORWARD;
	}

	public void setFetchSize(int rows) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public int getFetchSize() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public int getType() throws SQLException {
		return ResultSet.TYPE_FORWARD_ONLY;
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
		throw new SQLException("This method not implemented.");
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateNull(String columnName) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateByte(String columnName, byte x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateShort(String columnName, short x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateInt(String columnName, int x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateLong(String columnName, long x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateFloat(String columnName, float x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateDouble(String columnName, double x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateString(String columnName, String x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateBytes(String columnName, byte[] x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateDate(String columnName, Date x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateTime(String columnName, Time x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateObject(String columnName, Object x, int scale) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateObject(String columnName, Object x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void insertRow() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateRow() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void deleteRow() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void refreshRow() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void cancelRowUpdates() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void moveToInsertRow() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void moveToCurrentRow() throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public Statement getStatement() throws SQLException {
		return null;
	}

	public Object getObject(int columnIndex, Map<String,Class<?>> map) throws SQLException {
		return lookupRS(columnIndex).getObject(lookupCol(columnIndex), map);
	}

	public Ref getRef(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getRef(lookupCol(columnIndex));
	}

	public Blob getBlob(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getBlob(lookupCol(columnIndex));
	}

	public Clob getClob(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getClob(lookupCol(columnIndex));
	}

	public Array getArray(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getArray(lookupCol(columnIndex));
	}

	public Object getObject(String columnName, Map<String,Class<?>> map) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getObject(lookupCol(columnIndex), map);
	}

    public <T> T getObject(String columnName, Class<T> type) throws SQLException {
        int columnIndex = findColumn(columnName);
        return lookupRS(columnIndex).getObject(lookupCol(columnIndex), type);
    }

    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return lookupRS(columnIndex).getObject(lookupCol(columnIndex), type);
    }

	public Ref getRef(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getRef(lookupCol(columnIndex));
	}

	public Blob getBlob(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getBlob(lookupCol(columnIndex));
	}

	public Clob getClob(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getClob(lookupCol(columnIndex));
	}

	public Array getArray(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getArray(lookupCol(columnIndex));
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return lookupRS(columnIndex).getDate(lookupCol(columnIndex), cal);
	}

	public Date getDate(String columnName, Calendar cal) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getDate(lookupCol(columnIndex), cal);
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return lookupRS(columnIndex).getTime(lookupCol(columnIndex), cal);
	}

	public Time getTime(String columnName, Calendar cal) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getTime(lookupCol(columnIndex), cal);
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return lookupRS(columnIndex).getTimestamp(lookupCol(columnIndex), cal);
	}

	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getTimestamp(lookupCol(columnIndex), cal);
	}

	public URL getURL(int columnIndex) throws SQLException {
		return lookupRS(columnIndex).getURL(lookupCol(columnIndex));
	}

	public URL getURL(String columnName) throws SQLException {
		int columnIndex = findColumn(columnName);
		return lookupRS(columnIndex).getURL(lookupCol(columnIndex));
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateRef(String columnName, Ref x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateBlob(String columnName, Blob x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateClob(String columnName, Clob x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public void updateArray(String columnName, Array x) throws SQLException {
		throw new SQLException("This method not implemented.");
	}

	public int getColumnCount() throws SQLException {
		// TODO: Fix for NATURAL JOIN
		return leftRS.getMetaData().getColumnCount() + rightRS.getMetaData().getColumnCount();
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		return lookupRS(column).getMetaData().isAutoIncrement(lookupCol(column));
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		return lookupRS(column).getMetaData().isCaseSensitive(lookupCol(column));
	}

	public boolean isSearchable(int column) throws SQLException {
		return lookupRS(column).getMetaData().isSearchable(lookupCol(column));
	}

	public boolean isCurrency(int column) throws SQLException {
		return lookupRS(column).getMetaData().isCurrency(lookupCol(column));
	}

	public int isNullable(int column) throws SQLException {
		return lookupRS(column).getMetaData().isNullable(lookupCol(column));
	}

	public boolean isSigned(int column) throws SQLException {
		return lookupRS(column).getMetaData().isSigned(lookupCol(column));
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		return lookupRS(column).getMetaData().getColumnDisplaySize(lookupCol(column));
	}

	public String getColumnLabel(int column) throws SQLException {
		return lookupRS(column).getMetaData().getColumnLabel(lookupCol(column));
	}

	public String getColumnName(int column) throws SQLException {
		return lookupRS(column).getMetaData().getColumnName(lookupCol(column));
	}

	public String getSchemaName(int column) throws SQLException {
		return lookupRS(column).getMetaData().getSchemaName(lookupCol(column));
	}

	public int getPrecision(int column) throws SQLException {
		return lookupRS(column).getMetaData().getPrecision(lookupCol(column));
	}

	public int getScale(int column) throws SQLException {
		return lookupRS(column).getMetaData().getScale(lookupCol(column));
	}

	public String getTableName(int column) throws SQLException {
		return lookupRS(column).getMetaData().getTableName(lookupCol(column));
	}

	public String getCatalogName(int column) throws SQLException {
		return lookupRS(column).getMetaData().getCatalogName(lookupCol(column));
	}

	public int getColumnType(int column) throws SQLException {
		return lookupRS(column).getMetaData().getColumnType(lookupCol(column));
	}

	public String getColumnTypeName(int column) throws SQLException {
		return lookupRS(column).getMetaData().getColumnTypeName(lookupCol(column));
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

	public String getColumnClassName(int column) throws SQLException {
		return lookupRS(column).getMetaData().getColumnClassName(lookupCol(column));
	}

	private static final void print(ResultSet rs) throws SQLException {
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
			System.out.print(rs.getMetaData().getColumnName(i));
			System.out.print(i == rs.getMetaData().getColumnCount() ? '\n' : '\t');
		}
		while (rs.next()) {
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				System.out.print(rs.getString(i));
				System.out.print(i == rs.getMetaData().getColumnCount() ? '\n' : '\t');
			}
		}
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


        public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection db = DriverManager.getConnection(
				"jdbc:postgresql://brainsrv2.mitre.org:5432/digests",
				"postgres", "postgres");
		Statement stmt1 = db.createStatement();
		stmt1.execute("SET search_path TO ziph");
		ResultSet rs1 = stmt1.executeQuery("SELECT * FROM clusterstrategies ORDER BY cstrategy_id");
//		print(rs1);
		Statement stmt2 = db.createStatement();
		stmt2.execute("SET search_path TO ziph");
		ResultSet rs2 = stmt2.executeQuery("SELECT * FROM clustervalues ORDER BY cstrategy_id");
//		print(rs2);
		int[] col1 = {1};
		int[] col2 = {3};
		ExternalJoinResultSet join = new ExternalJoinResultSet(rs1, col1, rs2, col2, JoinType.INNER);
		print(join);

		ResultSet rs = stmt1.executeQuery("EXPLAIN SELECT * FROM clusterstrategies NATURAL JOIN clustervalues");
		print(rs);
		System.out.println("Done!");
	}

}