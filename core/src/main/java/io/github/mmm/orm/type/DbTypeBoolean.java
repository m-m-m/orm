/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link Boolean}.
 */
public class DbTypeBoolean extends DbTypeSimple<Boolean> {

  /**
   * The constructor.
   */
  public DbTypeBoolean() {

    this("BOOLEAN");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeBoolean(String declaration) {

    this(declaration, Types.BOOLEAN);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeBoolean(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Boolean> getSourceType() {

    return Boolean.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Boolean value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setBoolean(index, value.booleanValue());
    }
  }

}
