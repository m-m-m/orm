/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.orm.dialect.DbContext;
import io.github.mmm.orm.impl.DbContextNone;

/**
 * Interface of a formatter for {@link DbStatement}s.
 *
 * @see BasicDbStatementFormatter
 */
public interface DbStatementFormatter {

  /**
   * @param statement the {@link DbStatement} to format.
   * @param context the {@link DbContext}.
   * @return this {@link DbStatementFormatter} for fluent API calls.
   */
  default DbPlainStatement formatStatement(DbStatement<?> statement) {

    return formatStatement(statement, DbContextNone.INSTANCE);
  }

  /**
   * @param statement the {@link DbStatement} to format.
   * @param context the {@link DbContext}.
   * @return this {@link DbStatementFormatter} for fluent API calls.
   */
  DbPlainStatement formatStatement(DbStatement<?> statement, DbContext context);

  /**
   * @return the {@link #formatStatement(DbStatement, DbContext) formatted statement} as {@link String} (e.g. SQL).
   */
  @Override
  String toString();
}
