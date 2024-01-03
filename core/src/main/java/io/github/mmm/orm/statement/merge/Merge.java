/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.merge;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.StartClause;

/**
 * {@link Merge}-{@link DbClause} to merge data in the database.
 *
 * @since 1.0.0
 */
public final class Merge extends AbstractDbClause implements StartClause {

  /** Name of {@link Merge} for marshaling. */
  public static final String NAME_MERGE = "MERGE";

  /**
   * The constructor.
   */
  public Merge() {

    super();
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param entity the {@link EntityBean entity} to merge into.
   * @return the {@link MergeInto} for fluent API calls.
   */
  public <E extends EntityBean> MergeInto<E> into(E entity) {

    return new MergeInto<>(this, entity);
  }

}
