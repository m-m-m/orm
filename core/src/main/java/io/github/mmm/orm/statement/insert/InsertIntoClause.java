/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.insert;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.IntoClause;
import io.github.mmm.property.criteria.PropertyAssignment;

/**
 * A {@link IntoClause}-{@link DbClause} of an SQL {@link InsertStatement}.
 *
 * @param <E> type of the {@link #getEntity() entity}.
 * @since 1.0.0
 */
public class InsertIntoClause<E extends EntityBean> extends IntoClause<E, InsertValuesClause<E>, InsertIntoClause<E>> {

  private final InsertStatement<E> statement;

  /**
   * The constructor.
   *
   * @param insert the opening {@link InsertClause}.
   * @param entity the {@link #getEntity() entity} to operate on.
   */
  public InsertIntoClause(InsertClause insert, E entity) {

    this(insert, entity, null);
  }

  /**
   * The constructor.
   *
   * @param insert the opening {@link InsertClause}.
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  public InsertIntoClause(InsertClause insert, E entity, String entityName) {

    super(new AliasMap(), entity, entityName);
    this.statement = new InsertStatement<>(insert, this);
  }

  @Override
  public InsertValuesClause<E> value(PropertyAssignment<?> assignment) {

    return this.statement.getValues().value(assignment);
  }

  @Override
  public InsertValuesClause<E> values(PropertyAssignment<?>... assignments) {

    return this.statement.getValues().values(assignments);
  }

  @Override
  protected boolean isIncludeNullValues() {

    return false;
  }

  /**
   * @deprecated SQL does not support ALIAS for INSERT therefore also column names shall not be prefixed with alias.
   */
  @Override
  @Deprecated
  public InsertIntoClause<E> as(String entityAlias) {

    throw new UnsupportedOperationException();
  }

  @Override
  // make visible
  protected AliasMap getAliasMap() {

    return super.getAliasMap();
  }

  InsertStatement<E> getStatement() {

    return this.statement;
  }

}
