/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.insert;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.ValuesClause;

/**
 * {@link ValuesClause}-{@link DbClause} of an {@link InsertStatement}.
 *
 * @param <E> type of the {@link InsertIntoClause#getEntity() entity}.
 * @since 1.0.0
 */
public class InsertValuesClause<E extends EntityBean> extends ValuesClause<E, InsertValuesClause<E>> {

  private final InsertStatement<E> statement;

  /**
   * The constructor.
   *
   * @param statement the owning {@link InsertStatement}.
   */
  public InsertValuesClause(InsertStatement<E> statement) {

    super();
    this.statement = statement;
  }

  @Override
  protected E getEntity() {

    return this.statement.getInto().getEntity();
  }

  @Override
  public InsertStatement<E> get() {

    return this.statement;
  }

}
