/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import io.github.mmm.orm.statement.MainDbClause;
import io.github.mmm.property.criteria.CriteriaPredicate;

/**
 * {@link MainDbClause} allowing to {@link #having(CriteriaPredicate) begin} a {@link HavingClause}-clause.
 *
 * @param <R> type of the result of the selection.
 * @since 1.0.0
 */
public interface ClauseWithHaving<R> extends MainDbClause<R> {

  @Override
  SelectStatement<R> get();

  /**
   * @param predicate the {@link CriteriaPredicate} to add as {@link HavingClause}-clause.
   * @return the {@link HavingClause}-clause for fluent API calls.
   */
  default HavingClause<R> having(CriteriaPredicate predicate) {

    HavingClause<R> having = get().getHaving();
    having.and(predicate);
    return having;
  }

  /**
   * @param predicates the {@link CriteriaPredicate}s to add as {@link HavingClause}-clause. They will be combined with
   *        {@link io.github.mmm.property.criteria.PredicateOperator#AND AND}.
   * @return the {@link HavingClause}-clause for fluent API calls.
   */
  default HavingClause<R> having(CriteriaPredicate... predicates) {

    HavingClause<R> having = get().getHaving();
    having.and(predicates);
    return having;
  }

}
