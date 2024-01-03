/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.entity.bean.EntityBean;

/**
 * {@link AbstractDbStatement} for "... INTO ... VALUES" statements like
 * {@link io.github.mmm.orm.statement.insert.InsertStatement}, {@link io.github.mmm.orm.statement.merge.MergeStatement},
 * or {@link io.github.mmm.orm.statement.upsert.UpsertStatement}.
 *
 * @param <E> type of the {@link AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public abstract class IntoValuesStatement<E extends EntityBean> extends AbstractDbStatement<E> {

  /**
   * @return the {@link IntoClause}.
   */
  public abstract IntoClause<E, ?, ?> getInto();

  /**
   * @return the {@link ValuesClause}.
   */
  public abstract ValuesClause<E, ?> getValues();
}
