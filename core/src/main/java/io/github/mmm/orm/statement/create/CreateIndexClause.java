/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.IncompleteStartClause;
import io.github.mmm.orm.statement.StartClause;

/**
 * A {@link CreateIndexClause}-{@link StartClause} of an SQL {@link CreateIndexStatement}.
 *
 * @since 1.0.0
 * @see io.github.mmm.orm.statement.DbStatement#createIndex()
 */
public class CreateIndexClause extends AbstractDbClause implements IncompleteStartClause {

  /** Name of {@link CreateIndexClause} for marshaling. */
  public static final String NAME_CREATE_INDEX = "CREATE INDEX";

  private CreateIndexStatement<?> statement;

  private String name;

  /**
   * The constructor to use an auto-generated {@link #getName() name}.
   */
  public CreateIndexClause() {

    this("");
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name of the index}.
   */
  public CreateIndexClause(String name) {

    super();
    this.name = name;
  }

  /**
   * @return the name of the index. Will be empty if it should be auto-generated.
   */
  public String getName() {

    return this.name;
  }

  /**
   * @param name new value of {@link #getName()}.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return {@code true} if unique, {@code false} otherwise.
   */
  public boolean isUnique() {

    return false;
  }

  /**
   * @param <E> type of the {@link CreateIndexOnClause#getEntity() entity}.
   * @param entity the {@link CreateIndexOnClause#getEntity() entity} (table) to create an index on.
   * @return the {@link CreateIndexOnClause} for fluent API calls.
   */
  public <E extends EntityBean> CreateIndexOnClause<E> on(E entity) {

    return on(entity, null);
  }

  /**
   * @param <E> type of the {@link CreateIndexOnClause#getEntity() entity}.
   * @param entity the {@link CreateIndexOnClause#getEntity() entity} (table) to create an index on.
   * @param entityName the {@link CreateIndexOnClause#getEntityName() entity name} (table name) to create an index on.
   * @return the {@link CreateIndexOnClause} for fluent API calls.
   */
  public <E extends EntityBean> CreateIndexOnClause<E> on(E entity, String entityName) {

    CreateIndexOnClause<E> onClause = new CreateIndexOnClause<>(this, entity, entityName);
    this.statement = onClause.get();
    return onClause;
  }

  @Override
  public CreateIndexStatement<?> getStatement() {

    return this.statement;
  }

}
