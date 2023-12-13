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
public class DbTypeLocalDate2Date extends DbType<LocalDate, Date> {

  /**
   * The constructor.
   */
  public DbTypeLocalDate2Date() {

    this("DATE");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeLocalDate2Date(String declaration) {

    this(declaration, Types.DATE);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeLocalDate2Date(String declaration, int sqlType) {

    super(declaration, sqlType);
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
  public void setDbParameter(PreparedStatement statement, int index, Date value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setDate(index, value);
    }
  }

}
