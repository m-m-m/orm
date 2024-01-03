/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.Objects;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.criteria.PropertyAssignment;

/**
 * {@link SetClause}-{@link DbClause} containing {@link #getAssignments() assignments} to set or update.
 *
 * @param <E> type of the {@link EntityBean} to query.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class SetClause<E extends EntityBean, SELF extends SetClause<E, SELF>> extends AssignmentClause<E, SELF>
    implements TypedClauseWithWhere<E>, SetFragment<E, SELF> {

  /** Name of {@link SetClause} for marshaling. */
  public static final String NAME_SET = "set";

  /**
   * The constructor.
   */
  public SetClause() {

    super();
  }

  @Override
  public SELF set(PropertyAssignment<?> assignment) {

    Objects.requireNonNull(assignment, "assignment");
    this.assignments.add(assignment);
    return self();
  }

  @Override
  public SELF setAll(PropertyAssignment<?>... propertyAssignments) {

    for (PropertyAssignment<?> assignment : propertyAssignments) {
      set(assignment);
    }
    return self();
  }

}
