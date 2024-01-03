/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.param;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.type.DbType;
import io.github.mmm.property.criteria.CriteriaExpression;
import io.github.mmm.property.criteria.CriteriaParameters;
import io.github.mmm.property.criteria.Literal;

/**
 * {@link CriteriaParameters} using indexed parameters. It puts "{@code ?}" for each {@link Literal} into the output and
 * collects the parameters in order that can get accessed via {@link #iterator()} after the database statement has been
 * formatted and can be bound as parameters to the statement.
 *
 * @since 1.0.0
 */
public class CriteriaParametersIndexed extends AbstractCriteriaParameters {

  /**
   * The constructor.
   *
   * @param dialect the {@link AbstractDbDialect}.
   */
  public CriteriaParametersIndexed(AbstractDbDialect<?> dialect) {

    super(dialect);
  }

  @Override
  protected <V> CriteriaParameterImpl<V> createParameter(int index, V value, DbType<V, ?> dbType,
      CriteriaExpression<?> parent) {

    return new CriteriaParameterImpl<>(index, value, dbType);
  }

}
