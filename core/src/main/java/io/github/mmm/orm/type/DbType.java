/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.mmm.value.converter.AtomicTypeMapper;

/**
 * Abstract base class for a database type.
 *
 * @param <J> type of {@link #getSourceType() Java source type}.
 * @param <D> type of the {@link #getTargetType() database target type}.
 */
@SuppressWarnings("exports")
public abstract class DbType<J, D> extends AtomicTypeMapper<J, D> {

  /** @see #getSqlType() */
  protected final int sqlType;

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbType(String declaration, int sqlType) {

    super(declaration);
    this.sqlType = sqlType;
  }

  /**
   * @return the {@link Class} reflecting the Java standard type.
   */
  @Override
  public abstract Class<? extends J> getSourceType();

  /**
   * @return the {@link Class} reflecting the database standard type.
   */
  @Override
  public abstract Class<? extends D> getTargetType();

  /**
   * @return the SQL type int code from {@link java.sql.Types} or {@code -1} if no equivalent standard SQL type is
   *         available.
   */
  public final int getSqlType() {

    return this.sqlType;
  }

  /**
   * @return the number of decimal digits in case of decimal numeric type. Otherwise {@code 0}.
   */
  public int getDecimalDigits() {

    return 0;
  }

  /**
   * @return {@code true} if the Java source type is natively supported as
   *         {@link PreparedStatement#setObject(int, Object) JDBC parameter}, {@code false} otherwise.
   */
  protected boolean isJdbcSupport() {

    return false;
  }

  /**
   * This method sets the parameter at the given {@code index} to the given {@code value}. E.g. if this {@link DbType}
   * is for a simple regular {@link String} this method would more or less be equivalent to:
   *
   * <pre>
   * if (value == null) {
   *   statement.{@link PreparedStatement#setNull(int, int) setNull}(index, {@link #getSqlType() getSqlType()});
   * } else {
   *   statement.{@link PreparedStatement#setString(int, String) setString}(index, value);
   * }
   * </pre>
   *
   * @param statement the {@link PreparedStatement} where to set the parameter.
   * @param index the index position of the parameter to set. <b>ATTENTION</b>: JDBC index of parameters starts with
   *        {@code 1} (and not with {@code 0}) what is often causing confusion for developers.
   * @param value the value to bind to the parameter at the given {@code index}.
   * @param connection the JDBC {@link Connection}. May be used to create arrays but is typically ignored.
   * @throws SQLException if JDBC failed.
   * @see #setDbParameter(PreparedStatement, int, Object, Connection)
   */
  public void setJavaParameter(PreparedStatement statement, int index, J value, Connection connection)
      throws SQLException {

    if (isJdbcSupport()) {
      if (value == null) {
        statement.setNull(index, this.sqlType);
      } else {
        statement.setObject(index, value);
      }
    } else {
      D dbValue = toTarget(value);
      setDbParameter(statement, index, dbValue, connection);
    }
  }

  /**
   * This method sets the parameter at the given {@code index} to the given {@code value}. E.g. if this {@link DbType}
   * is for a simple regular {@link String} this method would more or less be equivalent to:
   *
   * <pre>
   * if (value == null) {
   *   statement.{@link PreparedStatement#setNull(int, int) setNull}(index, {@link #getSqlType() getSqlType()});
   * } else {
   *   statement.{@link PreparedStatement#setString(int, String) setString}(index, value);
   * }
   * </pre>
   *
   * @param statement the {@link PreparedStatement} where to set the parameter.
   * @param index the index position of the parameter to set. <b>ATTENTION</b>: JDBC index of parameters starts with
   *        {@code 1} (and not with {@code 0}) what is often causing confusion for developers.
   * @param value the value to bind to the parameter at the given {@code index}.
   * @param connection the JDBC {@link Connection}. May be used to create arrays but is typically ignored.
   * @throws SQLException if JDBC failed.
   * @see #setJavaParameter(PreparedStatement, int, Object, Connection)
   */
  public abstract void setDbParameter(PreparedStatement statement, int index, D value, Connection connection)
      throws SQLException;

}
