/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.orm.dialect.DbContext;
import io.github.mmm.orm.impl.DbContextNone;
import io.github.mmm.property.criteria.CriteriaFormatter;

/**
 * Interface for
 *
 * @see AbstractDbStatementFormatter
 */
public interface DbStatementFormatter {

  /**
   * @param statement the {@link DbStatement} to format.
   * @param context the {@link DbContext}.
   * @return this {@link DbStatementFormatter} for fluent API calls.
   */
  default DbStatementFormatter formatStatement(DbStatement<?> statement) {

    return formatStatement(statement, DbContextNone.INSTANCE);
  }

  /**
   * @param statement the {@link DbStatement} to format.
   * @param context the {@link DbContext}.
   * @return this {@link DbStatementFormatter} for fluent API calls.
   */
  DbStatementFormatter formatStatement(DbStatement<?> statement, DbContext context);

  /**
   * @return the {@link CriteriaFormatter} used to format criteria fragments to database syntax (e.g. SQL).
   */
  CriteriaFormatter getCriteriaFormatter();

  /**
   * @return the {@link #formatStatement(DbStatement, DbContext) formatted statement} as {@link String} (e.g. SQL).
   */
  default String get() {

    return toString();
  }

  /**
   * @return the {@link #formatStatement(DbStatement, DbContext) formatted statement} as {@link String} (e.g. SQL).
   * @see #get()
   */
  @Override
  String toString();

}
