/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.Objects;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.criteria.PropertyAssignment;
import io.github.mmm.value.PropertyPath;

/**
 * An {@code INTO}-{@link DbClause clause} of a {@link DbStatement} such as
 * {@link io.github.mmm.orm.statement.insert.InsertStatement INSERT} or
 * {@link io.github.mmm.orm.statement.upsert.UpsertStatement UPSERT}.
 *
 * @param <E> type of the {@link #getEntity() entity}.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class IntoClause<E extends EntityBean, SELF extends IntoClause<E, SELF>>
    extends AbstractEntityClause<E, E, SELF> {

  /**
   * The constructor.
   *
   * @param aliasMap the {@link AliasMap}.
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  protected IntoClause(AliasMap aliasMap, E entity, String entityName) {

    super(aliasMap, entity, entityName);
  }

  /**
   * @param assignment the {@link PropertyAssignment} to set.
   * @return the {@link ValuesClause} for fluent API.
   */
  public abstract ValuesClause<E, ?> values(PropertyAssignment<?> assignment);

  /**
   * @param assignments the {@link PropertyAssignment}s to set.
   * @return the {@link ValuesClause} for fluent API.
   */
  public abstract ValuesClause<E, ?> values(PropertyAssignment<?>... assignments);

  /**
   * Convenience method for
   * <code>{@link #values(PropertyAssignment) values}({@link PropertyAssignment}.{@link PropertyAssignment#of(PropertyPath, Object) of}(property, value)).</code>
   *
   * @param <V> type of the {@link PropertyPath#get() value}.
   * @param property the {@link PropertyPath property} to set.
   * @param value the {@link io.github.mmm.property.criteria.Literal} value to insert (assign the {@code property} to).
   * @return the {@link ValuesClause} for fluent API.
   */
  public abstract <V> ValuesClause<E, ?> values(PropertyPath<V> property, V value);

  /**
   * Sets all {@link EntityBean#getProperties() properties} of the {@link #getEntity() entity} that are not
   * {@code null}.
   *
   * @return the {@link ValuesClause} for fluent API.
   */
  public ValuesClause<E, ?> values() {

    Objects.requireNonNull(this.entity);
    ValuesClause<E, ?> values = null;
    for (WritableProperty<?> property : this.entity.getProperties()) {
      if (!property.isReadOnly()) {
        values = addValues(values, property);
      }
    }
    if (values == null) {
      throw new IllegalStateException("Entity must not be empty!");
    }
    return values;
  }

  private <V> ValuesClause<E, ?> addValues(ValuesClause<E, ?> values, WritableProperty<V> property) {

    V value = property.get();
    if (value != null) {
      if (values == null) {
        values = values(property, value);
      } else {
        values = values.and(property, value);
      }
    }
    return values;
  }

}
