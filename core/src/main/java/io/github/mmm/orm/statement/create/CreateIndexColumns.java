/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import java.util.ArrayList;
import java.util.List;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.statement.AbstractTypedClause;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.MainDbClause;
import io.github.mmm.property.ReadableProperty;

/**
 * A {@link CreateIndexColumns}-{@link DbClause} of an SQL {@link CreateIndexStatement}.
 *
 * @param <E> type of the {@link io.github.mmm.orm.statement.AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class CreateIndexColumns<E extends EntityBean> extends AbstractTypedClause<E, CreateIndexColumns<E>>
    implements MainDbClause<E>, CreateIndexFragment<E> {

  private final CreateIndexStatement<E> statement;

  private final List<DbColumnSpec> columns;

  /**
   * The constructor.
   *
   * @param statement the {@link CreateIndexStatement}.
   */
  public CreateIndexColumns(CreateIndexStatement<E> statement) {

    super();
    this.statement = statement;
    this.columns = new ArrayList<>();
  }

  @Override
  public CreateIndexStatement<E> get() {

    return this.statement;
  }

  @Override
  public CreateIndexColumns<E> column(DbColumnSpec column) {

    this.columns.add(column);
    return this;
  }

  @Override
  public CreateIndexColumns<E> columns(DbColumnSpec... cols) {

    for (DbColumnSpec column : cols) {
      this.columns.add(column);
    }
    return this;
  }

  @Override
  public CreateIndexColumns<E> columns(ReadableProperty<?>... properties) {

    for (ReadableProperty<?> property : properties) {
      column(property);
    }
    return this;
  }

  /**
   * @return the {@link List} of {@link DbColumnSpec}s.
   */
  public List<DbColumnSpec> getColumns() {

    return this.columns;
  }

}
