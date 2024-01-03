/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import io.github.mmm.value.converter.TypeMapper;

/**
 * A single value {@link #getValue() value} with {@link #getName() name} of a {@link DbResult}.
 *
 * @param <V> type of the {@link #getValue() value}.
 */
public interface DbResultValue<V> {

  /**
   * @return the database name of this entry. That is the name of the column, the explicit alias, or a {@link String}
   *         representation derived from the {@link io.github.mmm.value.CriteriaObject selection criteria}.
   */
  String getName();

  /**
   * @return the optional {@link TypeMapper#getDeclaration() database declaration}.
   */
  default String getDeclaration() {

    return "";
  }

  /**
   * @return the result value from the database. May be {@code null}.
   */
  V getValue();

  /**
   * @param newValue the new {@link #getValue() value}.
   * @return a new instance of {@link DbResultValue} with the same {@link #getName() name} and {@link #getDeclaration()
   *         declaration} but the given {@code value} as {@link #getValue() value}.
   */
  DbResultValue<V> withValue(V newValue);

}