/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import io.github.mmm.orm.statement.MainDbClause;
import io.github.mmm.value.PropertyPath;

/**
 * {@link MainDbClause} allowing to {@link #groupBy(PropertyPath) begin} a {@link GroupByClause}-clause.
 *
 * @param <R> type of the result of the selection.
 * @since 1.0.0
 */
public interface ClauseWithGroupBy<R> extends MainDbClause<R> {

  @Override
  SelectStatement<R> get();

  /**
   * @param path the {@link PropertyPath} to add as {@link GroupByClause}-clause.
   * @return the {@link GroupByClause}-clause for fluent API calls.
   */
  default GroupByClause<R> groupBy(PropertyPath<?> path) {

    GroupByClause<R> groupBy = get().getGroupBy();
    if (path != null) {
      groupBy.and(path);
    }
    return groupBy;
  }

  /**
   * @param paths the {@link PropertyPath}s to add as {@link GroupByClause}-clause.
   * @return the {@link GroupByClause}-clause for fluent API calls.
   */
  default GroupByClause<R> groupBy(PropertyPath<?>... paths) {

    GroupByClause<R> groupBy = get().getGroupBy();
    groupBy.and(paths);
    return groupBy;
  }

}
