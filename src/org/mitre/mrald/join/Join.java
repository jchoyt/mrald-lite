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

import java.sql.RowId;
import java.sql.NClob;
import java.sql.SQLXML;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * <p>A Join combines the values from two sources into a single ResultSet.
 * @author Peter Mork
 * @version 1.0
 */
public abstract class Join {

	/**
	 * The outer source's columns will be indexed as 1..M.
	 */
	public final Source outerSource;
	/**
	 * The inner source's columns will be indexed as M+1..M+N.
	 */
	public final Source innerSource;
	/**
	 * The join columns from the outer source (in the range 1..M).
	 */
	public final int[] outerCols;
	/**
	 * The join columns from the inner source (in the range 1..N).
	 */
	public final int[] innerCols;
	/**
	 * A reference to the outer source's result set; in some implementations
	 * this is reset after every pass.
	 */
	protected ResultSet outerRS;
	/**
	 * A reference to the inner source's result set; in some implementations
	 * this is reset after every pass.
	 */
	protected ResultSet innerRS;
	/**
	 * References either outerRS or innerRS; it's needed to support the
	 * wasNull method.
	 */
	private ResultSet lastRS;

	// TODO: Create another (optional?) constructor that takes a result set.
	public Join(Source outerSource, Source innerSource, int[] outerCols, int[] innerCols) throws SQLException {
		this.outerSource = outerSource;
		this.innerSource = innerSource;
		this.outerCols = outerCols;
		this.innerCols = innerCols;
		if (outerCols.length != innerCols.length) {
			throw new SQLException("Column mismatch.");
		}
	}

	/**
	 * Note, this method should assume that outerRS and innerRS have not
	 * yet been instantiated.
	 * @return The result of joining the outer and inner sources, using the
	 * specified join columns.
	 * @throws SQLException
	 */
	public abstract ResultSet execute() throws SQLException ;

	public abstract class JoinResultSet implements ResultSet, ResultSetMetaData {
		/**
		 * Used to track the current row index; this is to be updated by
		 * the next() method.
		 */
		protected int rowNum = -1;
		protected int first = BEFORE_FIRST;
		public static final int BEFORE_FIRST = 0;
		public static final int FIRST = 1;
		public static final int AFTER_FIRST = 2;

		private ResultSetMetaData outerMetaData;
		private ResultSetMetaData innerMetaData;

		public JoinResultSet(ResultSetMetaData outer, ResultSetMetaData inner) throws SQLException {
			outerMetaData = (outer == null) ? outerRS.getMetaData() : outer;
			innerMetaData = (inner == null) ? innerRS.getMetaData() : inner;
		}

		public JoinResultSet() throws SQLException {
			this(null, null);
		}

		/**
		 * @return True if the tuples at the current cursor position can be joined.
		 * @throws SQLException
		 */
		protected boolean testCursor() throws SQLException {
			if (outerRS.getRow() == -1 || innerRS.getRow() == -1) return false;
			for (int i = 0; i < outerCols.length; i++) {
				if (!testCursorAtColumn(i)) return false;
			}
			return true;
		}

		/**
		 * Subclasses <em>must</em> invoke this method when they
		 * successfully advance
		 * @throws SQLException
		 */
		public final boolean next() throws SQLException {
			while (advanceCursor()) {
				if (testCursor()) {
					first = (first == BEFORE_FIRST ? FIRST : AFTER_FIRST);
					rowNum++;
					return true;
				}
			}
			rowNum = 0;
			return false;
		}

		protected abstract boolean advanceCursor() throws SQLException;

		/**
		 * @param index An index into the arrays of join columns.
		 * @return True if the tuples at the current cursor position partially match.
		 * @throws SQLException
		 */
		private boolean testCursorAtColumn(int index) throws SQLException {
			Object outer = outerRS.getObject(outerCols[index]);
			Object inner = innerRS.getObject(innerCols[index]);
			if (outerRS.wasNull() || innerRS.wasNull()) return false;
			return outer.equals(inner);
		}

		/**
		 * @param col A column index in the range 1..M+N.
		 * @param read True if the caller is reading a value from the ResultSet;
		 * this is needed to support wasNull.
		 * @return Either outerRS or innerRS, depending on which source cotnains
		 * the data for the indicated column.
		 * @throws SQLException
		 */
		private ResultSet lookupRS(int col, boolean read) throws SQLException {
			ResultSet result = (col <= outerMetaData.getColumnCount()) ? outerRS : innerRS;
			if (read) lastRS = result;
			return result;
		}

		private ResultSetMetaData lookupMD(int col) throws SQLException {
			return (col <= outerMetaData.getColumnCount() ? outerMetaData : innerMetaData);
		}

		/**
		 * @param col A column index in the range 1..M+N.
		 * @return A source-specific column index in the range 1..M or 1..N.
		 * @throws SQLException
		 */
		private int lookupCol(int col) throws SQLException {
			int outerCols = outerMetaData.getColumnCount();
			return (col <= outerCols) ? col : col - outerCols;
		}

		public void close() throws SQLException {
			try {
				outerSource.close();
			} catch (SQLException e) {
				innerSource.close();
				throw e;
			}
		}

		public boolean wasNull() throws SQLException {
			return lastRS.wasNull();
		}

		public boolean isBeforeFirst() throws SQLException {
			return (first == BEFORE_FIRST);
		}

		public boolean isAfterLast() throws SQLException {
			return outerRS.isAfterLast();
		}

		public boolean isFirst() throws SQLException {
			return (first == FIRST);
		}

		public String getString(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getString(lookupCol(columnIndex));
		}

		public boolean getBoolean(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getBoolean(lookupCol(columnIndex));
		}

		public byte getByte(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getByte(lookupCol(columnIndex));
		}

		public short getShort(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getShort(lookupCol(columnIndex));
		}

		public int getInt(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getInt(lookupCol(columnIndex));
		}

		public long getLong(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getLong(lookupCol(columnIndex));
		}

		public float getFloat(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getFloat(lookupCol(columnIndex));
		}

		public double getDouble(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getDouble(lookupCol(columnIndex));
		}

		@Deprecated
		public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
			return lookupRS(columnIndex, true).getBigDecimal(lookupCol(columnIndex), scale);
		}

		public byte[] getBytes(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getBytes(lookupCol(columnIndex));
		}

		public Date getDate(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getDate(lookupCol(columnIndex));
		}

		public Time getTime(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getTime(lookupCol(columnIndex));
		}

		public Timestamp getTimestamp(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getTimestamp(lookupCol(columnIndex));
		}

		public InputStream getAsciiStream(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getAsciiStream(lookupCol(columnIndex));
		}

		@Deprecated
		public InputStream getUnicodeStream(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getUnicodeStream(lookupCol(columnIndex));
		}

		public InputStream getBinaryStream(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getBinaryStream(lookupCol(columnIndex));
		}

		public String getString(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getString(lookupCol(columnIndex));
		}

		public boolean getBoolean(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getBoolean(lookupCol(columnIndex));
		}

		public byte getByte(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getByte(lookupCol(columnIndex));
		}

		public short getShort(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getShort(lookupCol(columnIndex));
		}

		public int getInt(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getInt(lookupCol(columnIndex));
		}

		public long getLong(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getLong(lookupCol(columnIndex));
		}

		public float getFloat(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getFloat(lookupCol(columnIndex));
		}

		public double getDouble(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getDouble(lookupCol(columnIndex));
		}

		@Deprecated
		public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getBigDecimal(lookupCol(columnIndex), scale);
		}

		public byte[] getBytes(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getBytes(lookupCol(columnIndex));
		}

		public Date getDate(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getDate(lookupCol(columnIndex));
		}

		public Time getTime(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getTime(lookupCol(columnIndex));
		}

		public Timestamp getTimestamp(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getTimestamp(lookupCol(columnIndex));
		}

		public InputStream getAsciiStream(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getAsciiStream(lookupCol(columnIndex));
		}

		@Deprecated
		public InputStream getUnicodeStream(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getUnicodeStream(lookupCol(columnIndex));
		}

		public InputStream getBinaryStream(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getBinaryStream(lookupCol(columnIndex));
		}

		public SQLWarning getWarnings() throws SQLException {
			SQLWarning warning = outerRS.getWarnings();
			return (warning != null) ? warning : innerRS.getWarnings();
		}

		public void clearWarnings() throws SQLException {
			outerRS.clearWarnings();
			innerRS.clearWarnings();
		}

		public String getCursorName() throws SQLException {
			throw new SQLException("This method not implemented.");
		}

		public ResultSetMetaData getMetaData() throws SQLException {
			return this;
		}

		public Object getObject(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getObject(lookupCol(columnIndex));
		}

		public Object getObject(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getObject(lookupCol(columnIndex));
		}

		public int findColumn(String columnName) throws SQLException {
			int col = -1;
			try {
				col = outerRS.findColumn(columnName);
			} catch (SQLException sqle) {
				col = innerRS.findColumn(columnName);
			}
			if (col == -1) throw new SQLException("Column not found.");
			return col;
		}

		public Reader getCharacterStream(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getCharacterStream(lookupCol(columnIndex));
		}

		public Reader getCharacterStream(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getCharacterStream(lookupCol(columnIndex));
		}

		public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getBigDecimal(lookupCol(columnIndex));
		}

		public BigDecimal getBigDecimal(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getBigDecimal(lookupCol(columnIndex));
		}

		public void beforeFirst() throws SQLException {
			throw new SQLException("Join ResultSets are forward-only.");
		}

		public void afterLast() throws SQLException {
			throw new SQLException("Join ResultSets are forward-only.");
		}

		public boolean first() throws SQLException {
			if (isBeforeFirst()) return this.next();
			if (isFirst()) return true;
			throw new SQLException("Join ResultSets are forward-only.");
		}

		public boolean last() throws SQLException {
			throw new SQLException("Join ResultSets are forward-only.");
		}

		public boolean isLast() throws SQLException {
			throw new SQLException("Join ResultSets are forward-only.");
		}

		public int getRow() throws SQLException {
			return rowNum;
		}

		public boolean absolute(int row) throws SQLException {
			if (row > 0 && row >= rowNum) {
				return relative(row - rowNum);
			} else {
				throw new SQLException("Join ResultSets are forward-only.");
			}
		}

		public boolean relative(int rows) throws SQLException {
			if (rows >= 0) {
				for (int i = 0; i < rows; i++) {
					if (!this.next()) return false;
				}
			} else {
				throw new SQLException("Join ResultSets are forward-only.");
			}
			return true;
		}

		public boolean previous() throws SQLException {
			throw new SQLException("Join ResultSets are forward-only.");
		}

		public void setFetchDirection(int direction) throws SQLException {
			if (direction != ResultSet.FETCH_FORWARD) {
				throw new SQLException("Join ResultSets are forward-only.");
			}
		}

		public int getFetchDirection() throws SQLException {
			return ResultSet.FETCH_FORWARD;
		}

		public void setFetchSize(int rows) throws SQLException {}

		public int getFetchSize() throws SQLException {
			return 1;
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
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateBoolean(int columnIndex, boolean x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateByte(int columnIndex, byte x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateShort(int columnIndex, short x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateInt(int columnIndex, int x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateLong(int columnIndex, long x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateFloat(int columnIndex, float x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateDouble(int columnIndex, double x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateString(int columnIndex, String x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateBytes(int columnIndex, byte[] x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateDate(int columnIndex, Date x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateTime(int columnIndex, Time x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateObject(int columnIndex, Object x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateNull(String columnName) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateBoolean(String columnName, boolean x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateByte(String columnName, byte x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateShort(String columnName, short x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateInt(String columnName, int x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateLong(String columnName, long x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateFloat(String columnName, float x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateDouble(String columnName, double x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateString(String columnName, String x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateBytes(String columnName, byte[] x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateDate(String columnName, Date x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateTime(String columnName, Time x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateObject(String columnName, Object x, int scale) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateObject(String columnName, Object x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void insertRow() throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateRow() throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void deleteRow() throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void refreshRow() throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void cancelRowUpdates() throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void moveToInsertRow() throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void moveToCurrentRow() throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public Statement getStatement() throws SQLException {
			return null;
		}

		public Object getObject(int columnIndex, Map<String,Class<?>> map) throws SQLException {
			return lookupRS(columnIndex, true).getObject(lookupCol(columnIndex), map);
		}

		public Ref getRef(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getRef(lookupCol(columnIndex));
		}

		public Blob getBlob(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getBlob(lookupCol(columnIndex));
		}

		public Clob getClob(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getClob(lookupCol(columnIndex));
		}

		public Array getArray(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getArray(lookupCol(columnIndex));
		}

		public Object getObject(String columnName, Map<String,Class<?>> map) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getObject(lookupCol(columnIndex), map);
		}

        public <T> T getObject(String columnName, Class<T> type) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getObject(lookupCol(columnIndex), type);
        }

        public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
			return lookupRS(columnIndex, true).getObject(lookupCol(columnIndex), type);
        }

		public Ref getRef(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getRef(lookupCol(columnIndex));
		}

		public Blob getBlob(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getBlob(lookupCol(columnIndex));
		}

		public Clob getClob(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getClob(lookupCol(columnIndex));
		}

		public Array getArray(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getArray(lookupCol(columnIndex));
		}

		public Date getDate(int columnIndex, Calendar cal) throws SQLException {
			return lookupRS(columnIndex, true).getDate(lookupCol(columnIndex), cal);
		}

		public Date getDate(String columnName, Calendar cal) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getDate(lookupCol(columnIndex), cal);
		}

		public Time getTime(int columnIndex, Calendar cal) throws SQLException {
			return lookupRS(columnIndex, true).getTime(lookupCol(columnIndex), cal);
		}

		public Time getTime(String columnName, Calendar cal) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getTime(lookupCol(columnIndex), cal);
		}

		public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
			return lookupRS(columnIndex, true).getTimestamp(lookupCol(columnIndex), cal);
		}

		public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getTimestamp(lookupCol(columnIndex), cal);
		}

		public URL getURL(int columnIndex) throws SQLException {
			return lookupRS(columnIndex, true).getURL(lookupCol(columnIndex));
		}

		public URL getURL(String columnName) throws SQLException {
			int columnIndex = findColumn(columnName);
			return lookupRS(columnIndex, true).getURL(lookupCol(columnIndex));
		}

		public void updateRef(int columnIndex, Ref x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateRef(String columnName, Ref x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateBlob(int columnIndex, Blob x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateBlob(String columnName, Blob x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateClob(int columnIndex, Clob x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateClob(String columnName, Clob x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateArray(int columnIndex, Array x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
		}

		public void updateArray(String columnName, Array x) throws SQLException {
			throw new SQLException("Join ResultSets are not updateable.");
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

		public int getColumnCount() throws SQLException {
			return outerMetaData.getColumnCount() + innerMetaData.getColumnCount();
		}

		public boolean isAutoIncrement(int column) throws SQLException {
			return lookupMD(column).isAutoIncrement(lookupCol(column));
		}

		public boolean isCaseSensitive(int column) throws SQLException {
			return lookupMD(column).isCaseSensitive(lookupCol(column));
		}

		public boolean isSearchable(int column) throws SQLException {
			return lookupMD(column).isSearchable(lookupCol(column));
		}

		public boolean isCurrency(int column) throws SQLException {
			return lookupMD(column).isCurrency(lookupCol(column));
		}

		public int isNullable(int column) throws SQLException {
			return lookupMD(column).isNullable(lookupCol(column));
		}

		public boolean isSigned(int column) throws SQLException {
			return lookupMD(column).isSigned(lookupCol(column));
		}

		public int getColumnDisplaySize(int column) throws SQLException {
			return lookupMD(column).getColumnDisplaySize(lookupCol(column));
		}

		public String getColumnLabel(int column) throws SQLException {
			return lookupMD(column).getColumnLabel(lookupCol(column));
		}

		public String getColumnName(int column) throws SQLException {
			return lookupMD(column).getColumnName(lookupCol(column));
		}

		public String getSchemaName(int column) throws SQLException {
			return lookupMD(column).getSchemaName(lookupCol(column));
		}

		public int getPrecision(int column) throws SQLException {
			return lookupMD(column).getPrecision(lookupCol(column));
		}

		public int getScale(int column) throws SQLException {
			return lookupMD(column).getScale(lookupCol(column));
		}

		public String getTableName(int column) throws SQLException {
			return lookupMD(column).getTableName(lookupCol(column));
		}

		public String getCatalogName(int column) throws SQLException {
			return lookupMD(column).getCatalogName(lookupCol(column));
		}

		public int getColumnType(int column) throws SQLException {
			return lookupMD(column).getColumnType(lookupCol(column));
		}

		public String getColumnTypeName(int column) throws SQLException {
			return lookupMD(column).getColumnTypeName(lookupCol(column));
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
			return lookupMD(column).getColumnClassName(lookupCol(column));
		}
	}
}
