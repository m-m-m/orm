/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.ZonedDateTime;

/**
 * Implementation of {@link DbType} for a regular {@link ZonedDateTime}.
 */
public class DbTypeZonedDateTime extends DbTypeSimple<ZonedDateTime> {

  /**
   * The constructor.
   */
  public DbTypeZonedDateTime() {

    this("TIMESTAMP");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeZonedDateTime(String declaration) {

    this(declaration, Types.TIMESTAMP);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeZonedDateTime(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<ZonedDateTime> getSourceType() {

    return ZonedDateTime.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, ZonedDateTime value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setObject(index, value);
    }
  }

}
