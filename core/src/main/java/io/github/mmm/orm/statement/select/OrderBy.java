/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.mmm.orm.statement.AbstractTypedClause;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.MainDbClause;
import io.github.mmm.property.criteria.CriteriaOrdering;

/**
 * A {@link OrderBy}-{@link DbClause} of an SQL {@link SelectStatement}.
 *
 * @param <R> type of the result of the selection.
 * @since 1.0.0
 */
public class OrderBy<R> extends AbstractTypedClause<R, OrderBy<R>> implements MainDbClause<R> {

  /** Name of {@link OrderBy} for marshaling. */
  public static final String NAME_ORDER_BY = "ORDER BY";

  private final SelectStatement<R> statement;

  private final List<CriteriaOrdering> orderings;

  /**
   * The constructor.
   *
   * @param statement the owning {@link SelectStatement}.
   */
  public OrderBy(SelectStatement<R> statement) {

    super();
    this.statement = statement;
    this.orderings = new ArrayList<>();
  }

  @Override
  public boolean isOmit() {

    return this.orderings.isEmpty();
  }

  /**
   * @param ordering the {@link CriteriaOrdering} to add.
   * @return this {@link OrderBy}-class itself for fluent API calls.
   */
  public OrderBy<R> and(CriteriaOrdering ordering) {

    Objects.requireNonNull(ordering);
    this.orderings.add(ordering);
    return this;
  }

  /**
   * @param criteriaOrderings the {@link CriteriaOrdering}s to add.
   * @return this {@link OrderBy}-class itself for fluent API calls.
   */
  public OrderBy<R> and(CriteriaOrdering... criteriaOrderings) {

    for (CriteriaOrdering ordering : criteriaOrderings) {
      and(ordering);
    }
    return this;
  }

  /**
   * @return the {@link List} of {@link CriteriaOrdering}s to order by.
   */
  public List<CriteriaOrdering> getOrderings() {

    return this.orderings;
  }

  @Override
  public SelectStatement<R> get() {

    return this.statement;
  }

}
