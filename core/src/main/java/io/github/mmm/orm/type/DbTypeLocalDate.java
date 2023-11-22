/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link LocalDate}.
 */
public abstract class DbTypeLocalDate extends DbTypeSimple<LocalDate> {

  /**
   * The constructor.
   */
  public DbTypeLocalDate() {

    this("DATE");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeLocalDate(String declaration) {

    this(declaration, Types.DATE);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeLocalDate(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<LocalDate> getSourceType() {

    return LocalDate.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, LocalDate value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setObject(index, value);
    }
  }

}
