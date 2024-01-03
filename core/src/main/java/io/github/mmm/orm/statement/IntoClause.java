/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.Objects;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.WritableProperty;

/**
 * An {@code INTO}-{@link DbClause clause} of a {@link DbStatement} such as
 * {@link io.github.mmm.orm.statement.insert.InsertStatement INSERT} or
 * {@link io.github.mmm.orm.statement.upsert.UpsertStatement UPSERT}.
 *
 * @param <E> type of the {@link #getEntity() entity}.
 * @param <V> type of the {@link ValuesClause}.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class IntoClause<E extends EntityBean, V extends ValuesClause<E, ?>, SELF extends IntoClause<E, V, SELF>>
    extends AbstractEntityClause<E, E, SELF> implements ValuesFragment<E, V> {

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
   * Sets all {@link EntityBean#getProperties() properties} of the {@link #getEntity() entity} that are not
   * {@code null}.
   *
   * @return the {@link ValuesClause} for fluent API.
   */
  public V valuesAll() {

    Objects.requireNonNull(this.entity);
    V values = null;
    for (WritableProperty<?> property : this.entity.getProperties()) {
      if (!property.isTransient()) {
        values = addValues(values, property);
      }
    }
    if (values == null) {
      throw new IllegalStateException("Entity must not be empty!");
    }
    return values;
  }

  private <T> V addValues(V values, WritableProperty<T> property) {

    T value = property.get();
    if (isIncludeNullValues() || (value != null)) {
      if (values == null) {
        values = value(property, value);
      } else {
        @SuppressWarnings("rawtypes")
        ValuesClause values2 = values.value(property, value);
        assert (values2 == values);
      }
    }
    return values;
  }

  /**
   * @return {@code true} to include {@code null} values, {@code false} otherwise (to ignore them).
   */
  protected boolean isIncludeNullValues() {

    return true;
  }

}
