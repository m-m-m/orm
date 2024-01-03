/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping;

import java.util.function.Supplier;

import io.github.mmm.value.CriteriaObject;
import io.github.mmm.value.converter.TypeMapper;

/**
 * Container with information about the a single selected column.
 *
 * @param <V> type of the Java value to select and to map.
 * @since 1.0.0
 */
public final class DbSelection<V> {

  private final CriteriaObject<V> selection;

  private final TypeMapper<V, ?> typeMapper;

  /**
   * The constructor.
   *
   * @param selection the {@link #getSelection() selection}.
   * @param typeMapper the {@link #getTypeMapper() type mapper}.
   */
  public DbSelection(CriteriaObject<V> selection, TypeMapper<V, ?> typeMapper) {

    super();
    this.selection = selection;
    this.typeMapper = typeMapper;
  }

  /**
   * @return the {@link Supplier} with the selection from the {@link io.github.mmm.orm.statement.DbStatement}.
   */
  public CriteriaObject<V> getSelection() {

    return this.selection;
  }

  /**
   * @return the {@link TypeMapper} for the {@link #getSelection() selection}.
   */
  public TypeMapper<V, ?> getTypeMapper() {

    return this.typeMapper;
  }

  @Override
  public String toString() {

    return this.selection + "[" + this.typeMapper + "]";
  }

}
