/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link String}.
 */
public abstract class DbTypeString extends DbTypeSimple<String> {

  /**
   * The constructor.
   */
  public DbTypeString() {

    this("VARCHAR");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeString(String declaration) {

    this(declaration, Types.VARCHAR);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeString(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<String> getSourceType() {

    return String.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, String value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setString(index, value);
    }
  }

}
