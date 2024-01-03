/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import java.util.ArrayList;
import java.util.List;

import io.github.mmm.orm.statement.select.SelectFrom;

/**
 * Implementation of {@link DbResultRow} as mutable Java bean (POJO).
 *
 * @see SelectFrom
 * @since 1.0.0
 */
public class DbResultRowPojo implements DbResultRow {

  private final List<DbResultCell<?>> cells;

  /**
   * The constructor.
   */
  public DbResultRowPojo() {

    super();
    this.cells = new ArrayList<>();
  }

  /**
   * @param cell the {@link DbResultCell} to add.
   */
  public void addCell(DbResultCell<?> cell) {

    this.cells.add(cell);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <V> DbResultCell<V> getCell(int i) {

    return (DbResultCell<V>) this.cells.get(i);
  }

  @Override
  public int getSize() {

    return this.cells.size();
  }

  @Override
  public Iterable<DbResultCell<?>> getCells() {

    return this.cells;
  }

  @Override
  public String toString() {

    return "" + this.cells;
  }

}
