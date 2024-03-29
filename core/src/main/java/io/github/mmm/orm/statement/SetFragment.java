/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.alter.AlterTableClause;
import io.github.mmm.orm.statement.update.UpdateSet;
import io.github.mmm.property.criteria.PropertyAssignment;
import io.github.mmm.value.PropertyPath;

/**
 * Interface for a fragment or clause to {@link #set(PropertyAssignment) set} {@link PropertyAssignment assignments}.
 *
 * @param <E> type of the {@link AlterTableClause#getEntity() entity}.
 * @param <C> type of {@link DbClause} to return for fluent API.
 * @since 1.0.0
 */
public interface SetFragment<E extends EntityBean, C extends DbClause> {

  /**
   * @param assignment the {@link PropertyAssignment} to add.
   * @return the {@link DbClause} for fluent API calls.
   */
  C set(PropertyAssignment<?> assignment);

  /**
   * Convenience method for
   * <code>{@link #set(PropertyAssignment) set}({@link PropertyAssignment}.{@link PropertyAssignment#ofValue(PropertyPath) ofValue}(property)).</code>
   *
   * @param <V> type of the {@link PropertyPath#get() value}.
   * @param property the {@link PropertyPath property} to set.
   * @return the {@link DbClause} for fluent API calls.
   */
  default <V> C set(PropertyPath<V> property) {

    return set(PropertyAssignment.ofValue(property));
  }

  /**
   * Convenience method for
   * <code>{@link #set(PropertyAssignment) set}({@link PropertyAssignment}.{@link PropertyAssignment#of(PropertyPath, Object) of}(property, value)).</code>
   *
   * @param <V> type of the {@link PropertyPath#get() value}.
   * @param property the {@link PropertyPath property} to set.
   * @param value the {@link io.github.mmm.property.criteria.Literal} value to insert (assign the {@code property} to).
   * @return the {@link DbClause} for fluent API calls.
   */
  default <V> C set(PropertyPath<V> property, V value) {

    return set(PropertyAssignment.of(property, value));
  }

  /**
   * Convenience method for
   * <code>{@link #set(PropertyAssignment) set}({@link PropertyAssignment}.{@link PropertyAssignment#of(PropertyPath, PropertyPath) of}(property, valueProperty)).</code>
   *
   * @param <V> type of the {@link PropertyPath#get() value}.
   * @param property the {@link PropertyPath property} to set.
   * @param valueProperty the {@link PropertyPath property} from where to read the value to set.
   * @return the {@link UpdateSet} for fluent API.
   */
  default <V> C set(PropertyPath<V> property, PropertyPath<V> valueProperty) {

    return set(PropertyAssignment.of(property, valueProperty));
  }

  /**
   * @param assignments the {@link PropertyAssignment}s to add.
   * @return the {@link DbClause} for fluent API calls.
   */
  C setAll(PropertyAssignment<?>... assignments);

}
