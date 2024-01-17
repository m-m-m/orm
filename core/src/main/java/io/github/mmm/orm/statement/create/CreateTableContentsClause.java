/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import java.util.ArrayList;
import java.util.List;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.ddl.constraint.DbConstraint;
import io.github.mmm.orm.statement.AbstractTypedClause;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.MainDbClause;

/**
 * A {@link CreateTableContentsClause}-{@link DbClause} of an SQL {@link CreateTableStatement}.
 *
 * @param <E> type of the {@link io.github.mmm.orm.statement.AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class CreateTableContentsClause<E extends EntityBean> extends AbstractTypedClause<E, CreateTableContentsClause<E>>
    implements MainDbClause<E>, CreateTableFragment<E> {

  private final CreateTableStatement<E> statement;

  private final List<DbColumnSpec> columns;

  private final List<DbConstraint> constraints;

  /**
   * The constructor.
   *
   * @param statement the {@link CreateTableStatement}.
   */
  public CreateTableContentsClause(CreateTableStatement<E> statement) {

    super();
    this.statement = statement;
    this.columns = new ArrayList<>();
    this.constraints = new ArrayList<>();
  }

  @Override
  public CreateTableStatement<E> get() {

    return this.statement;
  }

  /**
   * @return the {@link List} of {@link DbColumnSpec}s.
   */
  public List<DbColumnSpec> getColumns() {

    return this.columns;
  }

  /**
   * @return the {@link List} of {@link DbConstraint}s.
   */
  public List<DbConstraint> getConstraints() {

    return this.constraints;
  }

  @Override
  public CreateTableContentsClause<E> column(DbColumnSpec column) {

    this.columns.add(column);
    return this;
  }

  /**
   * @param constraint the {@link DbConstraint} to add.
   * @return this {@link CreateTableContentsClause} for fluent API calls.
   */
  @Override
  public CreateTableContentsClause<E> constraint(DbConstraint constraint) {

    this.constraints.add(constraint);
    return this;
  }

}
