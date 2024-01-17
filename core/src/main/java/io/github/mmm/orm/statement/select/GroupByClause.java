/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.PropertyClause;

/**
 * A {@link GroupByClause}-{@link DbClause} of an SQL {@link SelectStatement}.
 *
 * @param <R> type of the result of the selection.
 * @since 1.0.0
 */
public class GroupByClause<R> extends PropertyClause<R, GroupByClause<R>> implements ClauseWithHaving<R>, ClauseWithOrderBy<R> {

  /** Name of {@link GroupByClause} for marshaling. */
  public static final String NAME_GROUP_BY = "GROUP BY";

  /** @see #get() */
  private final SelectStatement<R> statement;

  /**
   * The constructor.
   *
   * @param statement the owning {@link SelectStatement}.
   */
  public GroupByClause(SelectStatement<R> statement) {

    super();
    this.statement = statement;
  }

  @Override
  public SelectStatement<R> get() {

    return this.statement;
  }

}
