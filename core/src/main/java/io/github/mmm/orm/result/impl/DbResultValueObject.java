/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result.impl;

import io.github.mmm.orm.result.DbResultValue;

/**
 * Implementation of {@link DbResultValue} as immutable object.
 *
 * @param <V> type of the {@link #getValue() value}.
 * @since 1.0.0
 */
public class DbResultValueObject<V> implements DbResultValue<V> {

  private final String name;

  private final V value;

  private final String declaration;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param value the {@link #getValue() value}.
   * @param declaration the {@link #getDeclaration() declaration}.
   */
  public DbResultValueObject(String name, V value, String declaration) {

    super();
    this.name = name;
    this.value = value;
    this.declaration = declaration;
  }

  /**
   * The constructor.
   *
   * @param dbValue the {@link DbResultValue} to copy.
   */
  public DbResultValueObject(DbResultValue<V> dbValue) {

    this(dbValue.getName(), dbValue.getValue(), dbValue.getDeclaration());
  }

  @Override
  public V getValue() {

    return this.value;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public String getDeclaration() {

    return this.declaration;
  }

  @Override
  public DbResultValue<V> withValue(V newValue) {

    return new DbResultValueObject<>(this.name, newValue, this.declaration);
  }

  @Override
  public String toString() {

    return this.name + "[" + this.declaration + "]=" + this.value;
  }

}
