/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import java.util.Iterator;

import io.github.mmm.base.collection.SingleElementIterator;
import io.github.mmm.property.criteria.SimplePath;
import io.github.mmm.value.CriteriaObject;

/**
 * Implementation of {@link DbResultRow} as simple object.
 *
 * @see io.github.mmm.orm.statement.select.SelectFrom
 * @since 1.0.0
 */
public class DbResultCount implements DbResultRow, DbResultCell<Long>, DbResultSet {

  private static final String DEFAULT_DB_NAME = "COUNT";

  private static final CriteriaObject<?> DEFAULT_SELECTION = SimplePath.of(DEFAULT_DB_NAME);

  private final Long count;

  private final String dbName;

  private final CriteriaObject<?> selection;

  /**
   * The constructor.
   *
   * @param count the count (number of rows updated, matches, or the like).
   */
  public DbResultCount(long count) {

    this(Long.valueOf(count));
  }

  /**
   * The constructor.
   *
   * @param count the count (number of rows updated, matches, or the like).
   */
  public DbResultCount(Long count) {

    this(count, DEFAULT_DB_NAME, DEFAULT_SELECTION);
  }

  /**
   * The constructor.
   *
   * @param count the count (number of rows updated, matches, or the like).
   * @param dbName the {@link #getDbName() database name}.
   * @param selection the {@link #getSelection() selection}.
   */
  public DbResultCount(Long count, String dbName, CriteriaObject<?> selection) {

    super();
    this.count = count;
    this.dbName = dbName;
    this.selection = selection;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public <V> DbResultCell<V> getCell(int i) {

    if (i == 0) {
      return (DbResultCell) this;
    }
    throw new IndexOutOfBoundsException(i);
  }

  @Override
  public Iterable<DbResultCell<?>> getCells() {

    return () -> new SingleElementIterator<>(this);
  }

  @Override
  public int getSize() {

    return 1;
  }

  @Override
  public Iterator<DbResultRow> iterator() {

    return new SingleElementIterator<>(this);
  }

  @Override
  public Long getValue() {

    return this.count;
  }

  @Override
  public String getDbName() {

    return this.dbName;
  }

  @Override
  public CriteriaObject<?> getSelection() {

    return this.selection;
  }

  @Override
  public DbResultCell<Long> withValue(Long newValue) {

    return new DbResultCount(newValue, this.dbName, this.selection);
  }

  @Override
  public void close() {

    // no resources allocated so nothing to do
  }

  @Override
  public String toString() {

    return this.dbName + "=" + this.count;
  }

}
