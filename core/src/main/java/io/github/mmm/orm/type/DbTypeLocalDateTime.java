/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;

/**
 * Implementation of {@link DbType} for a regular {@link Duration}.
 */
public class DbTypeLocalDateTime extends DbTypeSimple<Duration> {

  /**
   * The constructor.
   */
  public DbTypeLocalDateTime() {

    this("INTERVAL");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeLocalDateTime(String declaration) {

    this(declaration, Types.NULL);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeLocalDateTime(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Duration> getSourceType() {

    return Duration.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Duration value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setObject(index, value);
    }
  }

}
