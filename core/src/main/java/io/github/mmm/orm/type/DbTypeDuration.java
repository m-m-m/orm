/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;

/**
 * Implementation of {@link DbType} for a regular {@link LocalDateTime}.
 */
public class DbTypeDuration extends DbTypeSimple<LocalDateTime> {

  /**
   * The constructor.
   */
  public DbTypeDuration() {

    this("TIMESTAMP");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeDuration(String declaration) {

    this(declaration, Types.TIMESTAMP);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeDuration(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<LocalDateTime> getSourceType() {

    return LocalDateTime.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, LocalDateTime value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setObject(index, value);
    }
  }

}
