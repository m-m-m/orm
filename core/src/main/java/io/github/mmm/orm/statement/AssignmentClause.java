/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.LongId;
import io.github.mmm.orm.statement.update.UpdateSet;
import io.github.mmm.property.criteria.PropertyAssignment;
import io.github.mmm.value.PropertyPath;

/**
 * {@link DbClause} containing {@link #getAssignments() assignments} like a
 * {@link io.github.mmm.orm.statement.insert.InsertValues}-clause.
 *
 * @param <E> type of the {@link EntityBean} to query.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class AssignmentClause<E extends EntityBean, SELF extends AssignmentClause<E, SELF>>
    extends AbstractTypedClause<E, SELF> {

  /** Name of the property {@link #getAssignments()} for marshaling. */
  public static final String NAME_ASSIGNMENTS = "set";

  private final List<PropertyAssignment<?>> assignments;

  /**
   * The constructor.
   */
  public AssignmentClause() {

    super();
    this.assignments = new ArrayList<>();
  }

  /**
   * @param assignment the {@link PropertyAssignment} to add.
   * @return this {@link DbClause} itself for fluent API calls.
   */
  public SELF and(PropertyAssignment<?> assignment) {

    Objects.requireNonNull(assignment, "assignment");
    this.assignments.add(assignment);
    return self();
  }

  /**
   * Convenience method for
   * <code>{@link #and(PropertyAssignment) and}({@link PropertyAssignment}.{@link PropertyAssignment#ofValue(PropertyPath) ofValue}(property)).</code>
   *
   * @param <V> type of the {@link PropertyPath#get() value}.
   * @param property the {@link PropertyPath property} to set.
   * @return the {@link UpdateSet} for fluent API.
   */
  public <V> SELF and(PropertyPath<V> property) {

    return and(PropertyAssignment.ofValue(property));
  }

  /**
   * @param <V> type of the {@link PropertyPath#get() value}.
   * @param property the {@link PropertyPath property} to set.
   * @param value the {@link io.github.mmm.property.criteria.Literal} value to insert (assign the {@code property} to).
   * @return this {@link DbClause} itself for fluent API calls.
   */
  public <V> SELF and(PropertyPath<V> property, V value) {

    return and(PropertyAssignment.of(property, value));
  }

  /**
   * @param propertyAssignments the {@link PropertyAssignment}s to add.
   * @return this {@link DbClause} itself for fluent API calls.
   */
  public SELF and(PropertyAssignment<?>... propertyAssignments) {

    for (PropertyAssignment<?> assignment : propertyAssignments) {
      and(assignment);
    }
    return self();
  }

  /**
   * @return the {@link EntityBean}.
   */
  protected abstract E getEntity();

  /**
   * @param id the {@link Id} value to set as {@link EntityBean#Id() primary key}.
   * @return this {@link DbClause} itself for fluent API calls.
   */
  @SuppressWarnings({ "rawtypes" })
  public SELF andId(Id<? extends E> id) {

    // bug in Eclipse compiler
    // return and(getEntity().Id(), (Id) id);
    and(getEntity().Id(), (Id) id);
    return self();
  }

  /**
   * @param id the {@link EntityBean#Id() primary key} as {@link Long} value.
   * @return this {@link DbClause} itself for fluent API calls.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public SELF andId(long id) {

    Class<?> javaClass = getEntity().getType().getJavaClass();
    Id id2 = LongId.of(id, javaClass);
    // bug in Eclipse compiler
    // return andId(id2);
    andId(id2);
    return self();
  }

  /**
   * @return the {@link List} of {@link PropertyAssignment}s.
   */
  public List<PropertyAssignment<?>> getAssignments() {

    return this.assignments;
  }

}
