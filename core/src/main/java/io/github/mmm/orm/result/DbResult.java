/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

/**
 * A {@link DbResult} represents a single result (row) from a database (typically of a
 * {@link io.github.mmm.orm.statement.select.SelectStatement}). It is an abstraction of a JDBC
 * {@link java.sql.ResultSet}.<br>
 *
 * @since 1.0.0
 * @see DbResultSet
 */
public interface DbResult {

  /**
   * @param <V> type of the value.
   * @param i the index of the requested value. Has to be in the range from {@code 0} to {@link #getSize() size-1}.
   * @return the requested result value.
   */
  <V> V getValue(int i);

  /**
   * @param i the index of the requested name. Has to be in the range from {@code 0} to {@link #getSize() size-1}.
   * @return the requested database name (e.g. column name, alias, selection).
   */
  String getName(int i);

  /**
   * @return the number of {@link #getValue(int) values}.
   */
  int getSize();

}
