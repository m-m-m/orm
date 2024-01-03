/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

/**
 * {@link DbType} with (potential) {@link #isJdbcSupport() native JDBC parameter support}.
 *
 * @param <J> type of {@link #getSourceType() Java source type}.
 * @param <D> type of the {@link #getTargetType() database target type}.
 * @since 1.0.0
 */
public abstract class DbTypeJdbcSupport<J, D> extends DbType<J, D> {

  private final boolean jdbcSupport;

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   * @param jdbcSupport the {@link #isJdbcSupport() JDBC support}.
   */
  public DbTypeJdbcSupport(String declaration, int sqlType, boolean jdbcSupport) {

    super(declaration, sqlType);
    this.jdbcSupport = jdbcSupport;
  }

  @Override
  protected boolean isJdbcSupport() {

    return this.jdbcSupport;
  }

}
