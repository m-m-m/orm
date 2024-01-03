/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.merge;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.IntoClause;
import io.github.mmm.property.criteria.PropertyAssignment;

/**
 * A {@link IntoClause} of an {@link MergeStatement}.
 *
 * @param <E> type of the {@link #getEntity() entity}.
 * @since 1.0.0
 */
public class MergeInto<E extends EntityBean> extends IntoClause<E, MergeValues<E>, MergeInto<E>> {

  private final MergeStatement<E> statement;

  /**
   * The constructor.
   *
   * @param merge the opening {@link Merge}.
   * @param entity the {@link #getEntity() entity} to operate on.
   */
  public MergeInto(Merge merge, E entity) {

    this(merge, entity, null);
  }

  /**
   * The constructor.
   *
   * @param merge the opening {@link Merge}.
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  public MergeInto(Merge merge, E entity, String entityName) {

    super(new AliasMap(), entity, entityName);
    this.statement = new MergeStatement<>(merge, this);
  }

  @Override
  public MergeValues<E> value(PropertyAssignment<?> assignment) {

    return this.statement.getValues().value(assignment);
  }

  @Override
  public MergeValues<E> values(PropertyAssignment<?>... assignments) {

    return this.statement.getValues().values(assignments);
  }

  @Override
  // make visible
  protected AliasMap getAliasMap() {

    return super.getAliasMap();
  }

}
