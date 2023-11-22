/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.mmm.orm.type.DbType;
import io.github.mmm.property.criteria.CriteriaParameter;

/**
 * Implementation of {@link CriteriaParameter}.
 *
 * @param <V> type of the {@link #getValue() value}.
 */
public class CriteriaParameterImpl<V> implements CriteriaParameter<V> {

  private static final String PLACEHOLDER_INDEX = "?";

  private final int index;

  private final V value;

  private final DbType<V, ?> dbType;

  private final String name;

  private CriteriaParameterImpl<?> next;

  /**
   * The constructor.
   *
   * @param index the {@link #getIndex() index}.
   * @param value the {@link #getValue() value}.
   * @param dbType the {@link #getDbType() database type}.
   */
  public CriteriaParameterImpl(int index, V value, DbType<V, ?> dbType) {

    this(index, value, dbType, null);
  }

  /**
   * The constructor.
   *
   * @param index the {@link #getIndex() index}.
   * @param value the {@link #getValue() value}.
   * @param dbType the {@link #getDbType() database type}.
   * @param name the {@link #getName() name}.
   */
  public CriteriaParameterImpl(int index, V value, DbType<V, ?> dbType, String name) {

    super();
    this.index = index;
    this.value = value;
    this.dbType = dbType;
    this.name = name;
  }

  @Override
  public int getIndex() {

    return this.index;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public V getValue() {

    return this.value;
  }

  /**
   * @return the {@link DbType} of this parameter.
   */
  public DbType<V, ?> getDbType() {

    return this.dbType;
  }

  /**
   * @return the placeholder for this parameter.
   */
  public String getPlaceholder() {

    if (this.name == null) {
      return PLACEHOLDER_INDEX;
    } else {
      return ":" + this.name;
    }
  }

  /**
   * @param statement the {@link PreparedStatement}.
   * @param connection the JDBC {@link Connection}.
   * @throws SQLException on error.
   */
  @SuppressWarnings("exports")
  public void apply(PreparedStatement statement, Connection connection) throws SQLException {

    this.dbType.setJavaParameter(statement, this.index + 1, this.value, connection);
  }

  /**
   * @return the next {@link CriteriaParameterImpl parameter} or {@code null} if this is the last one.
   */
  CriteriaParameterImpl<?> getNext() {

    return this.next;
  }

  void append(CriteriaParameterImpl<?> newNext) {

    if (this.next == null) {
      this.next = newNext;
    } else {
      throw new IllegalStateException("Can only append to the end!");
    }
  }

}
