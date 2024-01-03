/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result.impl;

import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;

/**
 * Implementation of {@link DbResult} as mutable Java bean (POJO).
 *
 * @since 1.0.0
 */
public class DbResultObject extends AbstractDbResult {

  private final DbResultValue<?>[] values;

  /**
   * The constructor.
   *
   * @param values the {@link DbResultValue}s.
   */
  public DbResultObject(DbResultValue<?>... values) {

    super();
    this.values = values;
  }

  @Override
  protected DbResultValue<?> get(int i) {

    return this.values[i];
  }

  @Override
  public int getSize() {

    return this.values.length;
  }

}
