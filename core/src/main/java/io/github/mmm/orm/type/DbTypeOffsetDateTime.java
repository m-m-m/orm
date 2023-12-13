/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;

/**
 * Implementation of {@link DbType} for a regular {@link OffsetDateTime}.
 */
public class DbTypeOffsetDateTime extends DbTypeSimple<OffsetDateTime> {

  /**
   * The constructor.
   */
  public DbTypeOffsetDateTime() {

    this("TIMESTAMP");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeOffsetDateTime(String declaration) {

    this(declaration, Types.TIMESTAMP);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeOffsetDateTime(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<OffsetDateTime> getSourceType() {

    return OffsetDateTime.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, OffsetDateTime value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setObject(index, value);
    }
  }

}
