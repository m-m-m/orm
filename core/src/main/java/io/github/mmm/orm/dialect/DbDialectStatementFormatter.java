/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.dialect;

import io.github.mmm.orm.orm.Orm;
import io.github.mmm.orm.param.CriteriaParametersIndexed;
import io.github.mmm.orm.statement.DbStatementFormatter;
import io.github.mmm.property.criteria.CriteriaFormatter;

/**
 * Abstract base class of {@link DbStatementFormatter} for any real {@link DbDialect database dialect}.
 */
public abstract class DbDialectStatementFormatter extends DbStatementFormatter {

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  public DbDialectStatementFormatter(Orm orm) {

    super(orm, CriteriaFormatter.of(new CriteriaParametersIndexed()));
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   * @param criteriaFormatter the {@link CriteriaFormatter} used to format criteria fragments to database syntax (e.g.
   *        SQL).
   */
  public DbDialectStatementFormatter(Orm orm, CriteriaFormatter criteriaFormatter) {

    super(orm, criteriaFormatter);
  }

}
