/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link BigDecimal}.
 */
public class DbTypeBigDecimal extends DbTypeSimple<BigDecimal> {

  /**
   * The constructor.
   */
  public DbTypeBigDecimal() {

    this("NUMBER");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeBigDecimal(String declaration) {

    this(declaration, Types.NUMERIC);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeBigDecimal(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<BigDecimal> getSourceType() {

    return BigDecimal.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, BigDecimal value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setBigDecimal(index, value);
    }
  }

}
