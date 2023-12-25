/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.sqlite.dialect;

import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.property.criteria.CriteriaFormatter;

/**
 * {@link DbDialectStatementFormatter} for SQLite Database.
 *
 * @since 1.0.0
 */
public class SqliteFormatter extends DbDialectStatementFormatter {

  /**
   * The constructor.
   *
   * @param dialect the {@link SqliteDialect}.
   */
  public SqliteFormatter(SqliteDialect dialect) {

    super(dialect);
  }

  /**
   * The constructor.
   *
   * @param dialect the {@link SqliteDialect}.
   * @param criteriaFormatter the {@link CriteriaFormatter} used to format criteria fragments to database syntax (SQL).
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public SqliteFormatter(SqliteDialect dialect, CriteriaFormatter criteriaFormatter, String indentation) {

    super(dialect, criteriaFormatter, indentation);
  }

}
