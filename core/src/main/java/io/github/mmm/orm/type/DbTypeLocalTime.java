/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalTime;

/**
 * Implementation of {@link DbType} for a regular {@link LocalTime}.
 */
public class DbTypeLocalTime extends DbTypeSimple<LocalTime> {

  /**
   * The constructor.
   */
  public DbTypeLocalTime() {

    this("TIME");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeLocalTime(String declaration) {

    this(declaration, Types.TIME);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeLocalTime(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<LocalTime> getSourceType() {

    return LocalTime.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, LocalTime value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setObject(index, value);
    }
  }

}
