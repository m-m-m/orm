/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

/**
 * Implementation of {@link DbType} for simple types that are directly supported by the target database such as
 * {@link Long} or {@link String}.
 *
 * @param <V> type of the {@link #getSourceType() Java datatype}.
 */
public abstract class DbTypeSimple<V> extends DbType<V, V> {

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeSimple(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public abstract Class<V> getSourceType();

  @Override
  public Class<V> getTargetType() {

    return getSourceType();
  }

  @Override
  public V toTarget(V source) {

    return source;
  }

}
