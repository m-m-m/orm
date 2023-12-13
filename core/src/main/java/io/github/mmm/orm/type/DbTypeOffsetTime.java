/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetTime;

/**
 * Implementation of {@link DbType} for a regular {@link OffsetTime}.
 */
public class DbTypeOffsetTime extends DbTypeSimple<OffsetTime> {

  /**
   * The constructor.
   */
  public DbTypeOffsetTime() {

    this("TIME");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeOffsetTime(String declaration) {

    this(declaration, Types.TIME);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeOffsetTime(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<OffsetTime> getSourceType() {

    return OffsetTime.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, OffsetTime value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setObject(index, value);
    }
  }

}
