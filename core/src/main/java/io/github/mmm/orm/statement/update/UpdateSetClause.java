/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.update;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.MainDbClause;
import io.github.mmm.orm.statement.SetClause;
import io.github.mmm.orm.statement.ValuesClause;
import io.github.mmm.orm.statement.insert.InsertIntoClause;
import io.github.mmm.orm.statement.insert.InsertStatement;
import io.github.mmm.property.criteria.CriteriaPredicate;

/**
 * {@link ValuesClause}-{@link DbClause} of an {@link InsertStatement}.
 *
 * @param <E> type of the {@link InsertIntoClause#getEntity() entity}.
 * @since 1.0.0
 */
public class UpdateSetClause<E extends EntityBean> extends SetClause<E, UpdateSetClause<E>> implements MainDbClause<E> {

  private final UpdateStatement<E> statement;

  /**
   * The constructor.
   *
   * @param statement the owning {@link UpdateStatement}.
   */
  public UpdateSetClause(UpdateStatement<E> statement) {

    super();
    this.statement = statement;
  }

  @Override
  public UpdateWhere<E> where(CriteriaPredicate predicate) {

    UpdateWhere<E> where = this.statement.getWhere();
    where.and(predicate);
    return where;
  }

  @Override
  public UpdateWhere<E> where(CriteriaPredicate... predicates) {

    UpdateWhere<E> where = this.statement.getWhere();
    where.and(predicates);
    return where;
  }

  @Override
  protected E getEntity() {

    return this.statement.getUpdate().getEntity();
  }

  @Override
  public UpdateStatement<E> get() {

    return this.statement;
  }

}
