/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.upsert;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.StartClause;

/**
 * {@link StartClause} to insert or if already present update data in the database. Only supported by modern databases.
 *
 * @since 1.0.0
 */
public final class UpsertClause extends AbstractDbClause implements StartClause {

  /** Name of {@link UpsertClause} for marshaling. */
  public static final String NAME_UPSERT = "UPSERT";

  /**
   * The constructor.
   */
  public UpsertClause() {

    super();
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param entity the {@link EntityBean entity} to upsert into.
   * @return the {@link UpsertInto} for fluent API calls.
   */
  public <E extends EntityBean> UpsertInto<E> into(E entity) {

    return new UpsertInto<>(this, entity);
  }

}
