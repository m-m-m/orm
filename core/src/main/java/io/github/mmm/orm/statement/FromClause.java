/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.entity.bean.EntityBean;

/**
 * A {@code FROM}-{@link DbClause clause} of a {@link DbStatement} such as
 * {@link io.github.mmm.orm.statement.select.SelectClause} or {@link io.github.mmm.orm.statement.delete.DeleteClause}.
 *
 * @param <R> type of the result. Only different from {@literal <E>} for complex selects.
 * @param <E> type of the {@link #getEntity() entity}.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class FromClause<R, E extends EntityBean, SELF extends FromClause<R, E, SELF>>
    extends AbstractEntitiesClause<R, E, SELF> implements MainDbClause<R>, TypedClauseWithWhere<R> {

  /**
   * The constructor.
   *
   * @param aliasMap the {@link AliasMap}.
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  protected FromClause(AliasMap aliasMap, E entity, String entityName) {

    super(aliasMap, entity, entityName);
  }

}
