/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.ddl.constraint.DbConstraint;
import io.github.mmm.orm.statement.AbstractEntityClause;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.StartClause;
import io.github.mmm.property.ReadableProperty;

/**
 * A {@link CreateTable}-{@link DbClause} of an SQL {@link CreateTableStatement}.
 *
 * @param <E> type of the {@link #getEntity() entity}.
 * @since 1.0.0
 */
public class CreateTable<E extends EntityBean> extends AbstractEntityClause<E, E, CreateTable<E>>
    implements StartClause, CreateTableFragment<E> {

  /** Name of {@link CreateTable} for marshaling. */
  public static final String NAME_CREATE_TABLE = "CREATE TABLE";

  private final CreateTableStatement<E> statement;

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} to operate on.
   */
  public CreateTable(E entity) {

    this(entity, null);
  }

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  public CreateTable(E entity, String entityName) {

    super(new AliasMap(), entity, entityName);
    this.statement = new CreateTableStatement<>(this);
  }

  @Override
  public CreateTableContents<E> column(DbColumnSpec column) {

    return this.statement.getContents().column(column);
  }

  @Override
  public CreateTableContents<E> constraint(DbConstraint constraint) {

    return this.statement.getContents().constraint(constraint);
  }

  /**
   * @param columns the {@link DbColumnSpec column}s.
   * @return the {@link CreateTableContents} for fluent API calls.
   */
  public CreateTableContents<E> columns(DbColumnSpec... columns) {

    CreateTableContents<E> contents = this.statement.getContents();
    for (DbColumnSpec column : columns) {
      contents.column(column);
    }
    return contents;
  }

  /**
   * Creates {@link #column(ReadableProperty) columns} for all {@link EntityBean#getProperties() properties} of the
   * {@link #getEntity() entity}.
   *
   * @return the {@link CreateTableContents} for fluent API calls.
   */
  public CreateTableContents<E> columns() {

    CreateTableContents<E> columns = this.statement.getContents();
    for (ReadableProperty<?> property : getEntity().getProperties()) {
      columns.column(property, true);
    }
    return columns;
  }

  @Override
  // make visible
  protected AliasMap getAliasMap() {

    return super.getAliasMap();
  }

}
