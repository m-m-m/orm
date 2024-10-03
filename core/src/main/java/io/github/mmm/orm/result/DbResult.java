/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import java.util.Iterator;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.orm.result.impl.DbResultValueIterator;

/**
 * A {@link DbResult} represents a single result (row) from a database (typically of a
 * {@link io.github.mmm.orm.statement.select.SelectStatement}). It is an abstraction of a JDBC
 * {@link java.sql.ResultSet}.<br>
 *
 * @since 1.0.0
 * @see DbResultValue
 */
public interface DbResult extends Iterable<DbResultValue<?>> {

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
   * @param i the index of the requested declaration. Has to be in the range from {@code 0} to {@link #getSize()
   *        size-1}.
   * @return the requested declaration. Will be {@link String#isEmpty() empty} if not available.
   */
  default String getDeclaration(int i) {

    if ((i < 0) || (i >= getSize())) {
      return null;
    }
    return "";
  }

  /**
   * @param name the potential {@link #getName(int) name} (column).
   * @return the index {@code i} such that {@link #getName(int) getName(i)} will return the given {@code name} or
   *         {@code -1} if no such index exists (name not found).
   */
  default int indexOf(String name) {

    int size = getSize();
    for (int i = 0; i < size; i++) {
      String dbName = getName(i);
      if (name.equalsIgnoreCase(dbName)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * @param name the potential {@link #getName(int) name} (column).
   * @return the index {@code i} such that {@link #getName(int) getName(i)} will return the given {@code name}.
   * @throws IllegalStateException if no such index exists as the given {@link #getName(int) name} was not found.
   */
  default int indexOfRequired(String name) {

    int index = indexOf(name);
    if (index >= 0) {
      return index;
    }
    StringBuilder sb = new StringBuilder();
    int size = getSize();
    for (int i = 0; i < size; i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(getName(i));
    }
    throw new ObjectNotFoundException("DB-column", name, sb.toString(), null);
  }

  /**
   * @return the number of {@link #getValue(int) values}.
   */
  int getSize();

  @Override
  default Iterator<DbResultValue<?>> iterator() {

    return new DbResultValueIterator(this);
  }

}
