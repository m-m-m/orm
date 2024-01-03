/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import io.github.mmm.base.exception.ObjectNotFoundException;

/**
 * A {@link DbResultRow} represents a single result (row) from a database (typically of a
 * {@link io.github.mmm.orm.statement.select.SelectStatement}).
 *
 * @since 1.0.0
 * @see DbResultSet
 */
public interface DbResultRow {

  /**
   * @return an {@link Iterable} of the {@link DbResultCell}s.
   */
  Iterable<DbResultCell<?>> getCells();

  /**
   * @param <V> type of the value.
   * @param i the index of the requested {@link DbResultCell}. Has to be in the range from {@code 0} to
   *        {@link #getSize() size-1}.
   * @return the requested {@link DbResultCell}.
   */
  <V> DbResultCell<V> getCell(int i);

  /**
   * @param <V> type of the value.
   * @param dbName the {@link DbResultCell#getDbName() database name} of the requested {@link DbResultCell}.
   * @return the specified {@link DbResultCell}.
   * @throws ObjectNotFoundException if no such object has been found.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  default <V> DbResultCell<V> getEntry(String dbName) {

    for (DbResultCell cell : getCells()) {
      if (dbName.equals(cell.getDbName())) {
        return cell;
      }
    }
    throw new ObjectNotFoundException("DbResultCell", dbName);
  }

  /**
   * @param <V> type of the value.
   * @param i the index of the requested {@link DbResultCell}. Has to be in the range from {@code 0} to
   *        {@link #getSize() size-1}.
   * @return the requested result value.
   */
  @SuppressWarnings("unchecked")
  default <V> V getValue(int i) {

    return (V) getCell(i).getValue();
  }

  /**
   * @return the number of {@link #getCell(int) entries}.
   */
  int getSize();

}
