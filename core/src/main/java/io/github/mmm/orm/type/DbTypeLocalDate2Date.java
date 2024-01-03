/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

/**
 * Implementation of {@link DbType} for a regular {@link LocalDate}.
 */
@SuppressWarnings("exports")
public class DbTypeLocalDate2Date extends DbTypeJdbcSupport<LocalDate, Date> {

  /**
   * The constructor.
   */
  public DbTypeLocalDate2Date() {

    this("DATE", true);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeLocalDate2Date(String declaration) {

    this(declaration, true);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param jdbcSupport the {@link #isJdbcSupport() JDBC support}.
   */
  public DbTypeLocalDate2Date(String declaration, boolean jdbcSupport) {

    this(declaration, Types.DATE, jdbcSupport);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   * @param jdbcSupport the {@link #isJdbcSupport() JDBC support}.
   */
  public DbTypeLocalDate2Date(String declaration, int sqlType, boolean jdbcSupport) {

    super(declaration, sqlType, jdbcSupport);
  }

  @Override
  public Class<LocalDate> getSourceType() {

    return LocalDate.class;
  }

  @Override
  public Class<Date> getTargetType() {

    return Date.class;
  }

  @Override
  public Date toTarget(LocalDate source) {

    return Date.valueOf(source);
  }

  @Override
  public LocalDate toSource(Date target) {

    if (target == null) {
      return null;
    }
    return target.toLocalDate();
  }

  @Override
  public void setJavaParameter(PreparedStatement statement, int index, LocalDate value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setObject(index, value);
    }
  }

  @Override
  public void setDbParameter(PreparedStatement statement, int index, Date value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setDate(index, value);
    }
  }

}
