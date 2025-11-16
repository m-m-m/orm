/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.FromClause;
import io.github.mmm.property.criteria.CriteriaPredicate;

/**
 * A {@link FromClause} of a {@link SelectStatement}.
 *
 * @param <R> type of the result of the selection.
 * @param <E> type of the {@link #getEntity() entity}.
 * @since 1.0.0
 */
public class SelectFromClause<R, E extends EntityBean> extends FromClause<R, E, SelectFromClause<R, E>>
    implements ClauseWithGroupBy<R>, ClauseWithOrderBy<R> {

  private final SelectStatement<R> statement;

  /**
   * The constructor.
   *
   * @param select the {@link SelectClause}.
   * @param entity the {@link #getEntity() entity}.
   */
  public SelectFromClause(SelectClause<R> select, E entity) {

    this(select, entity, null);
  }

  /**
   * The constructor.
   *
   * @param select the {@link SelectClause}.
   * @param entity the {@link #getEntity() entity}.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  public SelectFromClause(SelectClause<R> select, E entity, String entityName) {

    super(new AliasMap(), entity, entityName);
    this.statement = new SelectStatement<>(select, this);
    select.statement = this.statement;
  }

  @Override
  public SelectWhere<R> where(CriteriaPredicate predicate) {

    SelectWhere<R> where = this.statement.getWhere();
    where.and(predicate);
    return where;
  }

  @Override
  public SelectWhere<R> where(CriteriaPredicate... predicates) {

    SelectWhere<R> where = this.statement.getWhere();
    where.and(predicates);
    return where;
  }

  @Override
  public SelectStatement<R> get() {

    return this.statement;
  }

  @Override
  // make visible
  protected AliasMap getAliasMap() {

    return super.getAliasMap();
  }

}
