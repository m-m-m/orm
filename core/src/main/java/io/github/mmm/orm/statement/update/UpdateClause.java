/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.update;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractEntitiesClause;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.SetFragment;
import io.github.mmm.orm.statement.MainDbClause;
import io.github.mmm.orm.statement.StartClause;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.criteria.PropertyAssignment;

/**
 * {@link StartClause} of an UpdateStatement to update data in the database.
 *
 * @param <E> type of the {@link #getEntity() entity}.
 * @since 1.0.0
 */
public final class UpdateClause<E extends EntityBean> extends AbstractEntitiesClause<E, E, UpdateClause<E>>
    implements StartClause, MainDbClause<E>, SetFragment<E, UpdateSetClause<E>> {

  /** Name of {@link UpdateClause} for marshaling. */
  public static final String NAME_UPDATE = "UPDATE";

  private final UpdateStatement<E> statement;

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} to operate on.
   */
  public UpdateClause(E entity) {

    this(entity, null);
  }

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  public UpdateClause(E entity, String entityName) {

    super(new AliasMap(), entity, entityName);
    this.statement = new UpdateStatement<>(this);
  }

  /**
   * Sets all properties of the {@link #getEntity() entity} to their current values.
   *
   * @return the {@link UpdateSetClause} for fluent API.
   */
  public UpdateSetClause<E> set() {

    UpdateSetClause<E> set = this.statement.getSet();
    for (WritableProperty<?> property : this.entity.getProperties()) {
      set.set(PropertyAssignment.ofValue(property));
    }
    return set;
  }

  @Override
  public UpdateSetClause<E> set(PropertyAssignment<?> assignment) {

    return this.statement.getSet().set(assignment);
  }

  @Override
  public UpdateSetClause<E> setAll(PropertyAssignment<?>... assignments) {

    return this.statement.getSet().setAll(assignments);
  }

  @Override
  public UpdateStatement<E> get() {

    return this.statement;
  }

  @Override
  // make visible
  protected AliasMap getAliasMap() {

    return super.getAliasMap();
  }
}
