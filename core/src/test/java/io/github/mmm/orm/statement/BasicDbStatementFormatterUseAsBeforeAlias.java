package io.github.mmm.orm.statement;

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
   * @param criteriaFormatterFactory the {@link CriteriaFormatterFactory}.
   */
  public BasicDbStatementFormatterUseAsBeforeAlias(CriteriaFormatterFactory criteriaFormatterFactory) {

    super(criteriaFormatterFactory);
  }

  @Override
  public boolean isUseAsBeforeAlias() {

    return true;
  }

}
