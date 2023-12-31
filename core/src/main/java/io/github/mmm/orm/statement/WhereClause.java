/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.entity.bean.EntityBean;

/**
 * A {@link WhereClause}-{@link DbClause} of an SQL {@link StartClause}.
 *
 * @param <E> type of the {@link EntityBean} to query.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class WhereClause<E, SELF extends WhereClause<E, SELF>> extends PredicateClause<E, SELF>
    implements MainDbClause<E> {

  /**
   * The constructor.
   */
  public WhereClause() {

    super();
  }

}
