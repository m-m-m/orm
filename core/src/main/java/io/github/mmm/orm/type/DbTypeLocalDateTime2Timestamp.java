/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

/**
 * Implementation of {@link DbType} for a regular {@link LocalDateTime}.
 */
@SuppressWarnings("exports")
public abstract class DbTypeLocalDateTime2Timestamp extends DbType<LocalDateTime, Timestamp> {

  /**
   * The constructor.
   */
  public DbTypeLocalDateTime2Timestamp() {

    this("TIMESTAMP");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeLocalDateTime2Timestamp(String declaration) {

    this(declaration, Types.TIMESTAMP);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeLocalDateTime2Timestamp(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<LocalDateTime> getSourceType() {

    return LocalDateTime.class;
  }

  @Override
  public Class<Timestamp> getTargetType() {

    return Timestamp.class;
  }

  @Override
  public Timestamp toTarget(LocalDateTime source) {

    return Timestamp.valueOf(source);
  }

  @Override
  public LocalDateTime toSource(Timestamp target) {

    if (target == null) {
      return null;
    }
    return target.toLocalDateTime();
  }

  @Override
  public void setDbParameter(PreparedStatement statement, int index, Timestamp value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setTimestamp(index, value);
    }
  }

}
