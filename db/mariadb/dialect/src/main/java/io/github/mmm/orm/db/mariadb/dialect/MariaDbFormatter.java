/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.mariadb.dialect;

import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.property.criteria.CriteriaFormatterFactory;

/**
 * {@link DbDialectStatementFormatter} for MariaDB.
 *
 * @since 1.0.0
 */
public class MariaDbFormatter extends DbDialectStatementFormatter {

  /**
   * The constructor.
   *
   * @param dialect the {@link MariaDbDialect}.
   */
  public MariaDbFormatter(MariaDbDialect dialect) {

    super(dialect);
  }

  /**
   * The constructor.
   *
   * @param dialect the {@link MariaDbDialect}.
   * @param criteriaFormatterFactory the {@link CriteriaFormatterFactory}.
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public MariaDbFormatter(MariaDbDialect dialect, CriteriaFormatterFactory criteriaFormatterFactory,
      String indentation) {

    super(dialect, criteriaFormatterFactory, indentation);
  }

}
