/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.DbStatement;

/**
 * {@link SelectClause} to query the {@link SelectFrom#getEntity() primary entity}.
 *
 * @param <R> type of the result of the selection.
 * @see DbStatement#select(EntityBean)
 * @since 1.0.0
 */
public class SelectEntityClause<R extends EntityBean> extends SelectClause<R> {

  /**
   * The constructor.
   *
   * @param entity the entity.
   */
  public SelectEntityClause(R entity) {

    super(entity);
    setResultName(VALUE_RESULT_ENTITY);
  }

  /**
   * The constructor for internal usage.
   *
   * @param alias the alias to use as {@link #getResultName() result name}.
   */
  public SelectEntityClause(String alias) {

    super(null);
    if ((alias == null) || alias.isEmpty()) {
      setResultName(VALUE_RESULT_ENTITY);
    } else {
      setResultName(alias);
    }
  }

  /**
   * @return the {@link SelectFrom} for fluent API calls.
   */
  public SelectFrom<R, R> from() {

    return new SelectFrom<>(this, getResultBean());
  }

  @Override
  public boolean isSelectEntity() {

    return true;
  }

  @Override
  public boolean isSelectResult() {

    return false;
  }

  @Override
  public boolean isSelectSingle() {

    return false;
  }

}
