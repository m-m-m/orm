/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbType} for a regular {@link Byte}.
 */
public class DbTypeByte extends DbType<Byte, Number> {

  /**
   * The constructor.
   */
  public DbTypeByte() {

    this("INTEGER");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeByte(String declaration) {

    this(declaration, Types.INTEGER);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeByte(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<Byte> getSourceType() {

    return Byte.class;
  }

  @Override
  public Class<Byte> getTargetType() {

    return Byte.class;
  }

  @Override
  public Byte toSource(Number target) {

    if (target == null) {
      return null;
    } else if (target instanceof Byte b) {
      return b;
    } else {
      return Byte.valueOf(target.byteValue());
    }
  }

  @Override
  public Number toTarget(Byte source) {

    return source;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Number value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      statement.setByte(index, value.byteValue());
    }
  }

}
