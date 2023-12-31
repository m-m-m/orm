/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.delete;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.WhereClause;

/**
 * A {@link WhereClause}-{@link DbClause} of an SQL {@link DeleteStatement}.
 *
 * @param <E> type of the {@link DeleteFrom#getEntity() entity}.
 * @since 1.0.0
 */
public class DeleteWhere<E extends EntityBean> extends WhereClause<E, DeleteWhere<E>> {

  private final DeleteStatement<E> statement;

  /**
   * The constructor.
   *
   * @param statement the owning {@link DeleteStatement}.
   */
  public DeleteWhere(DeleteStatement<E> statement) {

    super();
    this.statement = statement;
  }

  @Override
  public DeleteStatement<E> get() {

    return this.statement;
  }

}
