/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.upsert;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.IntoClause;
import io.github.mmm.property.criteria.PropertyAssignment;

/**
 * A {@link IntoClause} of an {@link UpsertStatement}.
 *
 * @param <E> type of the {@link #getEntity() entity}.
 * @since 1.0.0
 */
public class UpsertInto<E extends EntityBean> extends IntoClause<E, UpsertValues<E>, UpsertInto<E>> {

  private final UpsertStatement<E> statement;

  /**
   * The constructor.
   *
   * @param upset the opening {@link UpsertClause}.
   * @param entity the {@link #getEntity() entity} to operate on.
   */
  public UpsertInto(UpsertClause upset, E entity) {

    this(upset, entity, null);
  }

  /**
   * The constructor.
   *
   * @param upsert the opening {@link UpsertClause}.
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  public UpsertInto(UpsertClause upsert, E entity, String entityName) {

    super(new AliasMap(), entity, entityName);
    this.statement = new UpsertStatement<>(upsert, this);
  }

  @Override
  public UpsertValues<E> value(PropertyAssignment<?> assignment) {

    return this.statement.getValues().value(assignment);
  }

  @Override
  public UpsertValues<E> values(PropertyAssignment<?>... assignments) {

    return this.statement.getValues().values(assignments);
  }

  @Override
  // make visible
  protected AliasMap getAliasMap() {

    return super.getAliasMap();
  }

}
