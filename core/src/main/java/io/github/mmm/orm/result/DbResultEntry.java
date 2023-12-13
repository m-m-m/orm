/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import java.util.function.Supplier;

import io.github.mmm.value.CriteriaObject;
import io.github.mmm.value.converter.TypeMapper;

/**
 * Container for {@link #getSelection() selection} and {@link #getValue() value}.
 *
 * @param <V> type of the {@link #getValue() value}.
 */
public interface DbResultEntry<V> {

  /**
   * @return the {@link Supplier} with the selection from the {@link io.github.mmm.orm.statement.DbStatement}.
   */
  CriteriaObject<?> getSelection();

  /**
   * @return the database name of this entry. That is the name of the column, the explicit alias of the
   *         {@link #getSelection() selection}, or a {@link String} representation derived from the
   *         {@link #getSelection() selection} (excluding entity aliases, compliant with simple naming so without
   *         expression syntax).
   */
  String getDbName();

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
   * @return a new instance of {@link DbResultEntry} with the same {@link #getSelection() selection} and
   *         {@link #getDbName() dbName} but the given {@code value} as {@link #getValue() value}.
   */
  DbResultEntry<V> withValue(V newValue);

}