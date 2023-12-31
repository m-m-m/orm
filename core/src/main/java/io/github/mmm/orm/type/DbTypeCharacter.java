/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link Character}.
 */
public class DbTypeCharacter extends DbTypeSimple<Character> {

  /**
   * The constructor.
   */
  public DbTypeCharacter() {

    this("CHAR");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeCharacter(String declaration) {

    this(declaration, Types.CHAR);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeCharacter(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Character> getSourceType() {

    return Character.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Character value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setString(index, value.toString());
    }
  }

}
