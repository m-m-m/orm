/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.ArrayList;
import java.util.List;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.criteria.PropertyAssignment;

/**
 * {@link DbClause} containing {@link #getAssignments() assignments} like a
 * {@link io.github.mmm.orm.statement.insert.InsertValuesClause}-clause.
 *
 * @param <E> type of the {@link EntityBean} to query.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class AssignmentClause<E extends EntityBean, SELF extends AssignmentClause<E, SELF>>
    extends AbstractTypedClause<E, SELF> {

  /** Name of the property {@link #getAssignments()} for marshaling. */
  public static final String NAME_ASSIGNMENTS = "set";

  /** The {@link List} of {@link PropertyAssignment}s. */
  protected final List<PropertyAssignment<?>> assignments;

  /**
   * The constructor.
   */
  public AssignmentClause() {

    super();
    this.assignments = new ArrayList<>();
  }

  /**
   * @return the {@link EntityBean}.
   */
  protected abstract E getEntity();

  /**
   * @return the {@link List} of {@link PropertyAssignment}s.
   */
  public List<PropertyAssignment<?>> getAssignments() {

    return this.assignments;
  }

}
