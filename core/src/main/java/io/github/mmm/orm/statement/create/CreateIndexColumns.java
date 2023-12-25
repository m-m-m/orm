/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.MainDbClause;
import io.github.mmm.orm.statement.PropertyClause;

/**
 * A {@link CreateIndexColumns}-{@link DbClause} of an SQL {@link CreateIndexStatement}.
 *
 * @param <E> type of the {@link io.github.mmm.orm.statement.AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class CreateIndexColumns<E extends EntityBean> extends PropertyClause<E, CreateIndexColumns<E>>
    implements MainDbClause<E> {

  private final CreateIndexStatement<E> statement;

  /**
   * The constructor.
   *
   * @param statement the {@link CreateIndexStatement}.
   */
  public CreateIndexColumns(CreateIndexStatement<E> statement) {

    super();
    this.statement = statement;
  }

  @Override
  public CreateIndexStatement<E> get() {

    return this.statement;
  }

}
