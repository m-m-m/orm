/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import io.github.mmm.value.CriteriaObject;

/**
 * Extends {@link DbResultCellObject} with {@link #getDeclaration() declaration}.
 *
 * @param <V> type of the {@link #getValue() value}.
 * @since 1.0.0
 */
public class DbResultCellObjectWithDeclaration<V> extends DbResultCellObject<V> {

  private final String declaration;

  /**
   * The constructor.
   *
   * @param selection the {@link #getSelection() selection}.
   * @param value the result {@link #getValue() value} from the DB.
   * @param dbName the {@link #getDbName() database name}.
   * @param declaration the {@link #getDeclaration() declaration}.
   */
  public DbResultCellObjectWithDeclaration(CriteriaObject<?> selection, V value, String dbName, String declaration) {

    super(selection, value, dbName);
    this.declaration = declaration;
  }

  @Override
  public String getDeclaration() {

    return this.declaration;
  }

  @Override
  public DbResultCellObject<V> withValue(V newValue) {

    if (this.value == newValue) {
      return this;
    }
    return new DbResultCellObjectWithDeclaration<>(this.selection, newValue, this.dbName, this.declaration);
  }

}
