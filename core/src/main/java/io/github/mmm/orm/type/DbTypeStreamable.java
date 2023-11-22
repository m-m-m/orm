/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import io.github.mmm.binary.Streamable;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link String}.
 */
public abstract class DbTypeStreamable extends DbTypeSimple<Streamable> {

  /**
   * The constructor.
   */
  public DbTypeStreamable() {

    this("BLOB");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeStreamable(String declaration) {

    this(declaration, Types.BLOB);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeStreamable(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Streamable> getSourceType() {

    return Streamable.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Streamable value, Connection connection)
      throws SQLException {

    if (value == null) {
      int type = this.sqlType;
      if (type < 0) {
        type = Types.NULL;
      }
      statement.setNull(index, type);
    } else {
      statement.setBinaryStream(index, value.asStream());
    }
  }

}
