/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link Float}.
 */
public abstract class DbTypeFloat extends DbTypeSimple<Float> {

  /**
   * The constructor.
   */
  public DbTypeFloat() {

    this("REAL");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeFloat(String declaration) {

    this(declaration, Types.REAL);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeFloat(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Float> getSourceType() {

    return Float.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Float value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setFloat(index, value.floatValue());
    }
  }

}
