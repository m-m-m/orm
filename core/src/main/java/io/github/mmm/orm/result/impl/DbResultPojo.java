/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result.impl;

import java.util.ArrayList;
import java.util.List;

import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;

/**
 * Implementation of {@link DbResult} as mutable Java bean (POJO).
 *
 * @since 1.0.0
 */
public class DbResultPojo extends AbstractDbResult {

  private final List<DbResultValue<?>> values;

  /**
   * The constructor.
   */
  public DbResultPojo() {

    this(16);
  }

  /**
   * The constructor.
   *
   * @param capacity the initial capacity.
   */
  public DbResultPojo(int capacity) {

    super();
    this.values = new ArrayList<>(capacity);
  }

  /**
   * @param cell the {@link DbResultValue} to add.
   */
  public void add(DbResultValue<?> cell) {

    this.values.add(cell);
  }

  @Override
  protected DbResultValue<?> get(int i) {

    return this.values.get(i);
  }

  @Override
  public int getSize() {

    return this.values.size();
  }

}
