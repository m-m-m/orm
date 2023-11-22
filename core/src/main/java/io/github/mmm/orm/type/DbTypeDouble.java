/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link Double}.
 */
public abstract class DbTypeDouble extends DbTypeSimple<Double> {

  /**
   * The constructor.
   */
  public DbTypeDouble() {

    this("DOUBLE PRECISION");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeDouble(String declaration) {

    this(declaration, Types.DOUBLE);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeDouble(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Double> getSourceType() {

    return Double.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Double value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setDouble(index, value.doubleValue());
    }
  }

}
