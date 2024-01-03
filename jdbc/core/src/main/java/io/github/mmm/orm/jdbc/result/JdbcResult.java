/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.result;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import io.github.mmm.orm.result.DbResult;

/**
 * Implementation of {@link DbResult} for JDBC {@link ResultSet}.
 *
 * @since 1.0.0
 */
public class JdbcResult implements DbResult {

  private ResultSet resultSet;

  private int size;

  private String[] names;

  /**
   * The constructor.
   *
   * @param resultSet the JDBC {@link ResultSet} to wrap.
   */
  public JdbcResult(ResultSet resultSet) {

    super();
    this.resultSet = resultSet;
    this.size = -1;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <V> V getValue(int i) {

    try {
      return (V) this.resultSet.getObject(i + 1);
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public String getName(int i) {

    if (this.names == null) {
      try {
        this.names = new String[getSize()];
        ResultSetMetaData metaData = this.resultSet.getMetaData();
        for (int colIndex = 0; colIndex < this.size; colIndex++) {
          this.names[colIndex] = metaData.getColumnLabel(colIndex + 1);
        }
      } catch (SQLException e) {
        throw new IllegalStateException(e);
      }
    }
    return this.names[i];
  }

  @Override
  public int getSize() {

    if (this.size < 0) {
      try {
        ResultSetMetaData metaData = this.resultSet.getMetaData();
        this.size = metaData.getColumnCount();
      } catch (SQLException e) {
        throw new IllegalStateException(e);
      }
    }
    return this.size;
  }

  /**
   * @param resultSet new {@link ResultSet}.
   */
  public void setResultSet(ResultSet resultSet) {

    this.resultSet = resultSet;
  }

}
