/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.delete;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.FromClause;
import io.github.mmm.property.criteria.CriteriaPredicate;

/**
 * A {@link FromClause}-{@link DbClause} of an SQL {@link DeleteStatement}.
 *
 * @param <E> type of the {@link #getEntity() entity}.
 * @since 1.0.0
 */
public class DeleteFromClause<E extends EntityBean> extends FromClause<E, E, DeleteFromClause<E>> {

  private final DeleteStatement<E> statement;

  /**
   * The constructor.
   *
   * @param delete the opening {@link DeleteClause}.
   * @param entity the {@link #getEntity() entity} to operate on.
   */
  public DeleteFromClause(DeleteClause delete, E entity) {

    this(delete, entity, null);
  }

  /**
   * The constructor.
   *
   * @param delete the opening {@link DeleteClause}.
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  public DeleteFromClause(DeleteClause delete, E entity, String entityName) {

    super(new AliasMap(), entity, entityName);
    this.statement = new DeleteStatement<>(delete, this);
  }

  @Override
  public DeleteWhere<E> where(CriteriaPredicate predicate) {

    DeleteWhere<E> where = this.statement.getWhere();
    where.and(predicate);
    return where;
  }

  @Override
  public DeleteWhere<E> where(CriteriaPredicate... predicates) {

    DeleteWhere<E> where = this.statement.getWhere();
    where.and(predicates);
    return where;
  }

  @Override
  public DeleteStatement<E> get() {

    return this.statement;
  }

  @Override
  // make visible
  protected AliasMap getAliasMap() {

    return super.getAliasMap();
  }

}
