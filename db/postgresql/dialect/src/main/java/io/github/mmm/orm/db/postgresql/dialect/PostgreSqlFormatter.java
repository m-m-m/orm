/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.postgresql.dialect;

import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.property.criteria.CriteriaFormatterFactory;

/**
 * {@link DbDialectStatementFormatter} for PostgreSQL Database.
 *
 * @since 1.0.0
 */
public class PostgreSqlFormatter extends DbDialectStatementFormatter {

  /**
   * The constructor.
   *
   * @param dialect the {@link PostgreSqlDialect}.
   */
  public PostgreSqlFormatter(PostgreSqlDialect dialect) {

    super(dialect);
  }

  /**
   * The constructor.
   *
   * @param dialect the {@link PostgreSqlDialect}.
   * @param criteriaFormatterFactory the {@link CriteriaFormatterFactory}.
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public PostgreSqlFormatter(PostgreSqlDialect dialect, CriteriaFormatterFactory criteriaFormatterFactory,
      String indentation) {

    super(dialect, criteriaFormatterFactory, indentation);
  }

}
