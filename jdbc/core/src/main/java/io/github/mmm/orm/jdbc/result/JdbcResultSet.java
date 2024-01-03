/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.result;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import io.github.mmm.base.collection.AbstractIterator;
import io.github.mmm.orm.result.DbResultCellObject;
import io.github.mmm.orm.result.DbResultRow;
import io.github.mmm.orm.result.DbResultRowObject;
import io.github.mmm.orm.result.DbResultSet;
import io.github.mmm.value.CriteriaObject;

/**
 * Implementation of {@link DbResultSet} for JDBC {@link ResultSet}.
 *
 * @since 1.0.0
 */
public class JdbcResultSet extends AbstractIterator<DbResultRow> implements DbResultSet {

  private Statement statement;

  private boolean used;

  private ResultSet resultSet;

  private int columnCount;

  private String[] dbNames;

  /**
   * The constructor.
   *
   * @param statement the {@link Statement}.
   */
  public JdbcResultSet(Statement statement) {

    super();
    this.statement = statement;
    try {
      this.resultSet = statement.getResultSet();
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
    findFirst();
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected DbResultRow findNext() {

    try {
      while (this.resultSet != null) {
        if (this.resultSet.next()) {
          if (this.dbNames == null) {
            ResultSetMetaData metaData = this.resultSet.getMetaData();
            this.columnCount = metaData.getColumnCount();
            this.dbNames = new String[this.columnCount];
            for (int i = 1; i <= this.columnCount; i++) {
              this.dbNames[i] = metaData.getColumnLabel(i);
            }
          }
          DbResultCellObject[] cells = new DbResultCellObject[this.columnCount];
          for (int i = 0; i < this.columnCount; i++) {
            Object value = this.resultSet.getObject(i);
            CriteriaObject<?> selection = null; // TODO
            cells[i] = new DbResultCellObject<>(selection, value, this.dbNames[i]);
          }
          return new DbResultRowObject(cells);
        }
        if (this.statement.getMoreResults()) {
          this.resultSet = this.statement.getResultSet();
        } else {
          this.resultSet = null;
          this.statement = null;
        }
      }
      return null;
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Iterator<DbResultRow> iterator() {

    if (this.used) {
      throw new IllegalStateException("Can only iterate once!");
    }
    this.used = true;
    return this;
  }

  @Override
  public void close() {

    if (this.resultSet != null) {
      try {
        this.resultSet.close();
      } catch (SQLException e) {
        throw new IllegalStateException(e);
      }
      this.resultSet = null;
      this.statement = null;
    }
  }

}
