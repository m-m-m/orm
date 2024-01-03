/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.Objects;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.criteria.PropertyAssignment;

/**
 * {@link ValuesClause}-{@link DbClause} containing {@link #getAssignments() assignments} for values to insert.
 *
 * @param <E> type of the {@link EntityBean} to query.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class ValuesClause<E extends EntityBean, SELF extends ValuesClause<E, SELF>>
    extends AssignmentClause<E, SELF> implements ValuesFragment<E, SELF>, MainDbClause<E> {

  /**
   * The constructor.
   */
  public ValuesClause() {

    super();
  }

  @Override
  public SELF value(PropertyAssignment<?> assignment) {

    Objects.requireNonNull(assignment, "assignment");
    this.assignments.add(assignment);
    return self();
  }

  @Override
  public SELF values(PropertyAssignment<?>... propertyAssignments) {

    for (PropertyAssignment<?> assignment : propertyAssignments) {
      value(assignment);
    }
    return self();
  }

  @Override
  public abstract IntoValuesStatement<E> get();

}
