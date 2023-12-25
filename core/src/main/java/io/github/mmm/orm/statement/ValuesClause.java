/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.entity.bean.EntityBean;

/**
 * {@link ValuesClause}-{@link DbClause} containing {@link #getAssignments() assignments} for values to insert.
 *
 * @param <E> type of the {@link EntityBean} to query.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class ValuesClause<E extends EntityBean, SELF extends ValuesClause<E, SELF>>
    extends AssignmentClause<E, SELF> {

  /**
   * The constructor.
   */
  public ValuesClause() {

    super();
  }

}
