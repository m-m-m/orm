/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link Long}.
 */
public abstract class DbTypeLong extends DbTypeSimple<Long> {

  /**
   * The constructor.
   */
  public DbTypeLong() {

    this("BIGINT");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeLong(String declaration) {

    this(declaration, Types.BIGINT);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeLong(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Long> getSourceType() {

    return Long.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Long value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setLong(index, value.longValue());
    }
  }

}
