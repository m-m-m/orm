/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.insert;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.IncompleteStartClause;

/**
 * {@link IncompleteStartClause} to insert data into the database.
 *
 * @since 1.0.0
 */
public final class InsertClause extends AbstractDbClause implements IncompleteStartClause {

  /** Name of {@link InsertClause} for marshaling. */
  public static final String NAME_INSERT = "INSERT";

  private InsertStatement<?> statement;

  /**
   * The constructor.
   */
  public InsertClause() {

    super();
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param entity the {@link EntityBean entity} to insert into.
   * @return the {@link InsertIntoClause} for fluent API calls.
   */
  public <E extends EntityBean> InsertIntoClause<E> into(E entity) {

    InsertIntoClause<E> insertInto = new InsertIntoClause<>(this, entity);
    this.statement = insertInto.getStatement();
    return insertInto;
  }

  @Override
  public InsertStatement<?> getStatement() {

    return this.statement;
  }

}
