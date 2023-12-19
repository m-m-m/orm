/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.mysql.dialect;

import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.orm.orm.Orm;
import io.github.mmm.property.criteria.CriteriaFormatter;

/**
 * {@link DbDialectStatementFormatter} for H2 Database.
 *
 * @since 1.0.0
 */
public class MySqlFormatter extends DbDialectStatementFormatter {

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  public MySqlFormatter(Orm orm) {

    super(orm);
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   * @param criteriaFormatter the {@link CriteriaFormatter} used to format criteria fragments to database syntax (SQL).
   */
  public MySqlFormatter(Orm orm, CriteriaFormatter criteriaFormatter) {

    super(orm, criteriaFormatter);
  }

}
