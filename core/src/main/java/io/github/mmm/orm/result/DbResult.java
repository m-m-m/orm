/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * A {@link DbResult} represents a generic result of a {@link SelectStatement} from a database.
 *
 * @since 1.0.0
 */
public interface DbResult extends Iterable<DbResultEntry<?>> {

  /**
   * @param <V> type of the value.
   * @param i the index of the requested {@link DbResultEntry}. Has to be in the range from {@code 0} to
   *        {@link #getSize() size-1}.
   * @return the requested {@link DbResultEntry}.
   */
  <V> DbResultEntry<V> getEntry(int i);

  /**
   * @param dbName the {@link DbResultEntry#getDbName() database name} of the requested {@link DbResultEntry}.
   * @return the specified {@link DbResultEntry}.
   * @throws ObjectNotFoundException if no such object has been found.
   */
  default DbResultEntry<?> getEntry(String dbName) {

    for (DbResultEntry<?> entry : this) {
      if (dbName.equals(entry.getDbName())) {
        return entry;
      }
    }
    throw new ObjectNotFoundException("ResultEntry", dbName);
  }

  /**
   * @param <V> type of the value.
   * @param i the index of the requested {@link DbResultEntry}. Has to be in the range from {@code 0} to
   *        {@link #getSize() size-1}.
   * @return the requested result value.
   */
  @SuppressWarnings("unchecked")
  default <V> V getValue(int i) {

    return (V) getEntry(i).getValue();
  }

  /**
   * @return the number of {@link #getEntry(int) entries}.
   */
  int getSize();

}
