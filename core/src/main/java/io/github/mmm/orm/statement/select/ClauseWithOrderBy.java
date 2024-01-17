/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import io.github.mmm.orm.statement.MainDbClause;
import io.github.mmm.property.criteria.CriteriaOrdering;

/**
 * {@link MainDbClause} allowing to {@link #orderBy(CriteriaOrdering) begin} a {@link HavingClause}-clause.
 *
 * @param <R> type of the result of the selection.
 * @since 1.0.0
 */
public interface ClauseWithOrderBy<R> extends MainDbClause<R> {

  @Override
  SelectStatement<R> get();

  /**
   * @param ordering the {@link CriteriaOrdering} to add as {@link OrderByClause}-clause.
   * @return the {@link OrderByClause}-clause for fluent API calls.
   */
  default OrderByClause<R> orderBy(CriteriaOrdering ordering) {

    OrderByClause<R> orderBy = get().getOrderBy();
    orderBy.and(ordering);
    return orderBy;
  }

  /**
   * @param orderings the {@link CriteriaOrdering}s to add as {@link OrderByClause}-clause.
   * @return the {@link OrderByClause}-clause for fluent API calls.
   */
  default OrderByClause<R> orderBy(CriteriaOrdering... orderings) {

    OrderByClause<R> orderBy = get().getOrderBy();
    orderBy.and(orderings);
    return orderBy;
  }

}
