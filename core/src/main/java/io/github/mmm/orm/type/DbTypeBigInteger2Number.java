/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link BigInteger}.
 */
public class DbTypeBigInteger2Number extends DbType<BigInteger, Number> {

  /**
   * The constructor.
   */
  public DbTypeBigInteger2Number() {

    this("NUMBER");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeBigInteger2Number(String declaration) {

    this(declaration, Types.NUMERIC);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeBigInteger2Number(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<BigInteger> getSourceType() {

    return BigInteger.class;
  }

  @Override
  public Class<? extends Number> getTargetType() {

    return Number.class;
  }

  @Override
  public BigInteger toSource(Number target) {

    if (target == null) {
      return null;
    } else if (target instanceof BigDecimal bd) {
      return bd.toBigInteger();
    } else if (target instanceof BigInteger bi) {
      return bi;
    }
    return BigInteger.valueOf(target.longValue());
  }

  @Override
  public Number toTarget(BigInteger source) {

    if (source == null) {
      return null;
    }
    return new BigDecimal(source);
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Number value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      if (value instanceof BigDecimal db) {
        statement.setBigDecimal(index, db);
      } else if (value instanceof BigInteger bi) {
        statement.setBigDecimal(index, new BigDecimal(bi));
      } else {
        statement.setLong(index, value.longValue());
      }
    }
  }

}
