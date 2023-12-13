/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbType} for a regular {@link Short}.
 */
public class DbTypeShort extends DbType<Short, Number> {

  /**
   * The constructor.
   */
  public DbTypeShort() {

    this("INTEGER");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeShort(String declaration) {

    this(declaration, Types.INTEGER);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeShort(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Short> getSourceType() {

    return Short.class;
  }

  @Override
  public Class<Number> getTargetType() {

    return Number.class;
  }

  @Override
  public Short toSource(Number target) {

    if (target == null) {
      return null;
    } else if (target instanceof Short s) {
      return s;
    } else {
      return Short.valueOf(target.shortValue());
    }
  }

  @Override
  public Number toTarget(Short source) {

    return source;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Number value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setShort(index, value.shortValue());
    }
  }

}
