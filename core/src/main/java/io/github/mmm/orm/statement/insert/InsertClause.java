/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.insert;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.StartClause;

/**
 * {@link StartClause} to insert data into the database.
 *
 * @since 1.0.0
 */
public final class InsertClause extends AbstractDbClause implements StartClause {

  /** Name of {@link InsertClause} for marshaling. */
  public static final String NAME_INSERT = "INSERT";

  /**
   * The constructor.
   */
  public InsertClause() {

    super();
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param entity the {@link EntityBean entity} to insert into.
   * @return the {@link InsertInto} for fluent API calls.
   */
  public <E extends EntityBean> InsertInto<E> into(E entity) {

    return new InsertInto<>(this, entity);
  }

}
