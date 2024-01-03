/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.statement.AbstractEntityClause;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.property.ReadableProperty;

/**
 * A {@link CreateIndexOn}-{@link DbClause} of an SQL {@link CreateIndexStatement}.
 *
 * @param <E> type of the {@link #getEntity() entity}.
 * @since 1.0.0
 */
public class CreateIndexOn<E extends EntityBean> extends AbstractEntityClause<E, E, CreateIndexOn<E>>
    implements CreateIndexFragment<E> {

  /** Name of {@link CreateIndexOn} for marshaling. */
  public static final String NAME_ON = "ON";

  private final CreateIndexStatement<E> statement;

  /**
   * The constructor.
   *
   * @param createIndex the {@link CreateIndex}-{@link DbClause}.
   * @param entity the {@link #getEntity() entity} to operate on.
   */
  public CreateIndexOn(CreateIndex createIndex, E entity) {

    this(createIndex, entity, null);
  }

  /**
   * The constructor.
   *
   * @param createIndex the {@link CreateIndex}-{@link DbClause}.
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  public CreateIndexOn(CreateIndex createIndex, E entity, String entityName) {

    super(new AliasMap(), entity, entityName);
    this.statement = new CreateIndexStatement<>(createIndex, this);
  }

  @Override
  public CreateIndexColumns<E> column(DbColumnSpec column) {

    return this.statement.getColumn().column(column);
  }

  @Override
  public CreateIndexColumns<E> columns(DbColumnSpec... columns) {

    return this.statement.getColumn().columns(columns);
  }

  @Override
  public CreateIndexColumns<E> columns(ReadableProperty<?>... properties) {

    return this.statement.getColumn().columns(properties);
  }

  @Override
  // make visible
  protected AliasMap getAliasMap() {

    return super.getAliasMap();
  }

}
