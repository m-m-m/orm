/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;

/**
 * Implementation of {@link Iterator} for {@link DbResultValue}s of {@link DbResult}.
 *
 * @since 1.0.0
 */
public class DbResultValueIterator implements Iterator<DbResultValue<?>>, DbResultValue<Object> {

  private DbResult result;

  private final int max;

  private int index;

  /**
   * The constructor.
   *
   * @param result the {@link DbResult} to adapt.
   */
  public DbResultValueIterator(DbResult result) {

    super();
    this.result = result;
    this.max = result.getSize() - 1;
    this.index = -1;
  }

  @Override
  public boolean hasNext() {

    return (this.index < this.max);
  }

  @Override
  public DbResultValue<?> next() {

    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    this.index++;
    if (this.index > this.max) {
      this.result = null;
    }
    return this;
  }

  @Override
  public Object getValue() {

    return this.result.getValue(this.index);
  }

  @Override
  public String getName() {

    return this.result.getName(this.index);
  }

  @Override
  public String getDeclaration() {

    return this.result.getDeclaration(this.index);
  }

  @Override
  public DbResultValue<Object> withValue(Object newValue) {

    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder("DbResultValueIterator:");
    if (this.index < 0) {
      sb.append("«start»");
    } else if (this.result == null) {
      sb.append("«end»");
    } else {
      sb.append(getName());
      sb.append('[');
      sb.append(getDeclaration());
      sb.append("]=");
      sb.append(getValue());
    }
    return sb.toString();
  }

}
