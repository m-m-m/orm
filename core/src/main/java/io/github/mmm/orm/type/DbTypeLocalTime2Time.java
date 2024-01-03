/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalTime;

/**
 * Implementation of {@link DbType} for a regular {@link LocalTime}.
 */
@SuppressWarnings("exports")
public class DbTypeLocalTime2Time extends DbTypeJdbcSupport<LocalTime, Time> {

  /**
   * The constructor.
   */
  public DbTypeLocalTime2Time() {

    this("TIME");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeLocalTime2Time(String declaration) {

    this(declaration, true);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param jdbcSupport the {@link #isJdbcSupport() JDBC support}.
   */
  public DbTypeLocalTime2Time(String declaration, boolean jdbcSupport) {

    this(declaration, jdbcSupport, Types.TIME);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param jdbcSupport the {@link #isJdbcSupport() JDBC support}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeLocalTime2Time(String declaration, boolean jdbcSupport, int sqlType) {

    super(declaration, sqlType, jdbcSupport);
  }

  @Override
  public Class<LocalTime> getSourceType() {

    return LocalTime.class;
  }

  @Override
  public Class<Time> getTargetType() {

    return Time.class;
  }

  @Override
  public LocalTime toSource(Time target) {

    if (target == null) {
      return null;
    } else {
      return target.toLocalTime();
    }
  }

  @Override
  public Time toTarget(LocalTime source) {

    if (source == null) {
      return null;
    } else {
      return Time.valueOf(source);
    }
  }

  @Override
  public void setDbParameter(PreparedStatement statement, int index, Time value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setTime(index, value);
    }
  }

}
