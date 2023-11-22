/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbType} for a {@link Boolean} mapped as {@link Integer} like typically done by hibernate for
 * older Oracle databases that do not support
 * <a href="https://asktom.oracle.com/pls/apex/asktom.search?tag=boolean-datatype">boolean datatype</a> .
 */
public abstract class DbTypeBoolean2Integer extends DbType<Boolean, Integer> {

  private static final Integer TRUE = Integer.valueOf(1);

  private static final Integer FALSE = Integer.valueOf(0);

  /**
   * The constructor.
   */
  public DbTypeBoolean2Integer() {

    this("NUMBER(1)");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeBoolean2Integer(String declaration) {

    this(declaration, Types.DATE);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeBoolean2Integer(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Boolean> getSourceType() {

    return Boolean.class;
  }

  @Override
  public Class<Integer> getTargetType() {

    return Integer.class;
  }

  @Override
  public Integer toTarget(Boolean source) {

    if (source == null) {
      return null;
    } else if (source.booleanValue()) {
      return TRUE;
    } else {
      return FALSE;
    }
  }

  @Override
  public Boolean toSource(Integer target) {

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
  public void setDbParameter(PreparedStatement statement, int index, Integer value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      int i = value.intValue();
      assert ((i >= 0) && (i <= 1));
      statement.setInt(index, i);
    }
  }

}
