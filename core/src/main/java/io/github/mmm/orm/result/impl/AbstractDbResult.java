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
public abstract class AbstractDbResult implements DbResult {

  /**
   * @param i the index of the requested {@link DbResultValue}. Has to be in the range from {@code 0} to
   *        {@link #getSize() size-1}.
   * @return the requested {@link DbResultValue}.
   */
  protected abstract DbResultValue<?> get(int i);

  @SuppressWarnings("unchecked")
  @Override
  public <V> V getValue(int i) {

    return (V) get(i).getValue();
  }

  @Override
  public String getName(int i) {

    return get(i).getName();
  }

  @Override
  public String getDeclaration(int i) {

    return get(i).getDeclaration();
  }

  @Override
  public String toString() {

    int size = getSize();
    StringBuilder sb = new StringBuilder(size * 16 + 2);
    sb.append('{');
    String s = "";
    for (int i = 0; i < size; i++) {
      sb.append(s);
      DbResultValue<?> dbValue = get(i);
      sb.append(dbValue);
      s = ", ";
    }
    sb.append('}');
    return sb.toString();
  }

}
