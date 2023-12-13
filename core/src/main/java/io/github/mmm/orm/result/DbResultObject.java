/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import java.util.Iterator;

import io.github.mmm.base.collection.ArrayIterator;

/**
 * Implementation of {@link DbResult} as simple object.
 *
 * @see io.github.mmm.orm.statement.select.SelectFrom
 * @since 1.0.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DbResultObject implements DbResult {

  private final DbResultEntryObject[] entries;

  /**
   * The constructor.
   *
   * @param entries the {@link #getEntry(int) entries}.
   */
  public DbResultObject(DbResultEntryObject... entries) {

    super();
    this.entries = entries;
  }

  @Override
  public <V> DbResultEntry<V> getEntry(int i) {

    return this.entries[i];
  }

  @Override
  public int getSize() {

    return this.entries.length;
  }

  @Override
  public Iterator<DbResultEntry<?>> iterator() {

    return new ArrayIterator<>(this.entries);
  }

}
