package io.github.mmm.orm.param;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.property.criteria.CriteriaParameters;

/**
 * Interface for a factory to {@link #create(AbstractDbDialect) create} instances of {@link CriteriaParameters}.
 */
public interface CriteriaParametersFactory {

  /**
   * @param dialect the {@link DbDialect}.
   * @return a new instance of {@link CriteriaParameters}.
   */
  CriteriaParameters<?> create(AbstractDbDialect<?> dialect);

}
