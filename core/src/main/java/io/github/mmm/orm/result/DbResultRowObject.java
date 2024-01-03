/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import java.util.Iterator;

import io.github.mmm.base.collection.ArrayIterator;

/**
 * Implementation of {@link DbResultRow} as simple object.
 *
 * @see io.github.mmm.orm.statement.select.SelectFrom
 * @since 1.0.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DbResultRowObject implements DbResultRow, Iterable<DbResultCell<?>> {

  private final DbResultCellObject[] cells;

  /**
   * The constructor.
   *
   * @param cells the {@link #getCell(int) entries}.
   */
  public DbResultRowObject(DbResultCellObject... cells) {

    super();
    this.cells = cells;
  }

  @Override
  public <V> DbResultCell<V> getCell(int i) {

    return this.cells[i];
  }

  @Override
  public int getSize() {

    return this.cells.length;
  }

  @Override
  public Iterable<DbResultCell<?>> getCells() {

    return this;
  }

  @Override
  public Iterator<DbResultCell<?>> iterator() {

    return new ArrayIterator<>(this.cells);
  }

}
