/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link Integer}.
 */
public abstract class DbTypeInteger extends DbTypeSimple<Integer> {

  /**
   * The constructor.
   */
  public DbTypeInteger() {

    this("INTEGER");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeInteger(String declaration) {

    this(declaration, Types.INTEGER);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeInteger(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Integer> getSourceType() {

    return Integer.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Integer value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setInt(index, value.intValue());
    }
  }

}
