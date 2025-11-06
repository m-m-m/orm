/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.merge;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.ValuesClause;

/**
 * {@link ValuesClause} of an {@link MergeStatement}.
 *
 * @param <E> type of the {@link MergeIntoClause#getEntity() entity}.
 * @since 1.0.0
 */
public class MergeValuesClause<E extends EntityBean> extends ValuesClause<E, MergeValuesClause<E>> {

  private final MergeStatement<E> statement;

  /**
   * The constructor.
   *
   * @param statement the owning {@link MergeStatement}.
   */
  public MergeValuesClause(MergeStatement<E> statement) {

    super();
    this.statement = statement;
  }

  @Override
  protected E getEntity() {

    return this.statement.getInto().getEntity();
  }

  @Override
  public MergeStatement<E> get() {

    return this.statement;
  }

}
