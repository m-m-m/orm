/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.mmm.orm.statement.select.SelectFrom;

/**
 * Implementation of {@link DbResult} as mutable Java bean (POJO).
 *
 * @see SelectFrom
 * @since 1.0.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DbResultPojo implements DbResult {

  private final List<DbResultEntry> entries;

  /**
   * The constructor.
   */
  public DbResultPojo() {

    super();
    this.entries = new ArrayList<>();
  }

  /**
   * @param entry the {@link DbResultEntry} to add.
   */
  public void addEntry(DbResultEntry<?> entry) {

    this.entries.add(entry);
  }

  @Override
  public <V> DbResultEntry<V> getEntry(int i) {

    return this.entries.get(i);
  }

  @Override
  public int getSize() {

    return this.entries.size();
  }

  @Override
  public Iterator<DbResultEntry<?>> iterator() {

    return (Iterator) this.entries.iterator();
  }

  @Override
  public String toString() {

    return "" + this.entries;
  }

}
