package io.github.mmm.orm.statement;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.param.CriteriaParametersFactory;
import io.github.mmm.property.criteria.CriteriaFormatterFactory;

/**
 * {@link BasicDbStatementFormatter} {@link #isUseAsBeforeAlias() using AS before alias}.
 */
public class BasicDbStatementFormatterUseAsBeforeAlias extends BasicDbStatementFormatter {

  /**
   * The constructor.
   */
  public BasicDbStatementFormatterUseAsBeforeAlias() {

    super();
  }

  /**
   * The constructor.
   *
   * @param dialect the {@link AbstractDbDialect}.
   * @param parametersFactory the {@link CriteriaFormatterFactory}.
   */
  public BasicDbStatementFormatterUseAsBeforeAlias(AbstractDbDialect<?> dialect,
      CriteriaParametersFactory parametersFactory) {

    super(dialect, parametersFactory, INDENTATION);
  }

  @Override
  public boolean isUseAsBeforeAlias() {

    return true;
  }

}
