/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbType} for a {@link Boolean} mapped as {@link Character} like suggested by
 * <a href="https://asktom.oracle.com/pls/apex/asktom.search?tag=boolean-datatype">Tom</a> for older Oracle databases.
 */
public abstract class DbTypeBoolean2Char extends DbType<Boolean, Character> {

  private static final Character TRUE = Character.valueOf('Y');

  private static final Character FALSE = Character.valueOf('N');

  /**
   * The constructor.
   */
  public DbTypeBoolean2Char() {

    this("CHAR(1) CHECK (FLAG IN ( 'Y', 'N' ))");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeBoolean2Char(String declaration) {

    this(declaration, Types.CHAR);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeBoolean2Char(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Boolean> getSourceType() {

    return Boolean.class;
  }

  @Override
  public Class<Character> getTargetType() {

    return Character.class;
  }

  @Override
  public Character toTarget(Boolean source) {

    if (source == null) {
      return null;
    } else if (source.booleanValue()) {
      return TRUE;
    } else {
      return FALSE;
    }
  }

  @Override
  public Boolean toSource(Character target) {

    if (target == null) {
      return null;
    } else if (TRUE.equals(target)) {
      return Boolean.TRUE;
    } else if (FALSE.equals(target)) {
      return Boolean.FALSE;
    }
    throw new IllegalArgumentException(target.toString());
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
