/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.delete;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.StartClause;

/**
 * {@link StartClause} to delete data from the database.
 *
 * @since 1.0.0
 */
public final class Delete extends AbstractDbClause implements StartClause {

  /** Name of {@link Delete} for marshaling. */
  public static final String NAME_DELETE = "DELETE";

  /**
   * The constructor.
   */
  public Delete() {

    super();
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param entity the {@link EntityBean entity} to delete from.
   * @return the {@link DeleteFrom} for fluent API calls.
   */
  public <E extends EntityBean> DeleteFrom<E> from(E entity) {

    return new DeleteFrom<>(this, entity);
  }

}
