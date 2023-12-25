/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.alter;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.ddl.constraint.DbConstraint;
import io.github.mmm.orm.statement.AbstractEntityClause;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.StartClause;

/**
 * A {@link AlterTable}-{@link DbClause} of an SQL {@link AlterTableStatement}.
 *
 * @param <E> type of the {@link #getEntity() entity}.
 * @since 1.0.0
 */
public class AlterTable<E extends EntityBean> extends AbstractEntityClause<E, E, AlterTable<E>>
    implements StartClause, AlterTableFragment<E> {

  /** Name of {@link AlterTable} for marshaling. */
  public static final String NAME_ALTER_TABLE = "ALTER TABLE";

  private final AlterTableStatement<E> statement;

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} to operate on.
   */
  public AlterTable(E entity) {

    this(entity, null);
  }

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  public AlterTable(E entity, String entityName) {

    super(new AliasMap(), entity, entityName);
    this.statement = new AlterTableStatement<>(this);
  }

  /**
   * @param columns the {@link DbColumnSpec column}s.
   * @return the {@link AlterTableOperations} for fluent API calls.
   */
  public AlterTableOperations<E> addColumns(DbColumnSpec... columns) {

    AlterTableOperations<E> operations = this.statement.getOperations();
    for (DbColumnSpec column : columns) {
      operations.addColumn(column);
    }
    return operations;
  }

  @Override
  public AlterTableOperations<E> addColumn(DbColumnSpec column) {

    return this.statement.getOperations().addColumn(column);
  }

  @Override
  public AlterTableOperations<E> addConstraint(DbConstraint constraint) {

    return this.statement.getOperations().addConstraint(constraint);
  }

  @Override
  public AlterTableOperations<E> dropColumn(DbColumnSpec column) {

    return this.statement.getOperations().dropColumn(column);
  }

  @Override
  public AlterTableOperations<E> dropConstraint(DbConstraint constraint) {

    return this.statement.getOperations().dropConstraint(constraint);
  }

  @Override
  public AlterTableOperations<E> dropConstraint(String constraint) {

    return this.statement.getOperations().dropConstraint(constraint);
  }

  @Override
  public AlterTableOperations<E> renameColumn(DbColumnSpec column, DbColumnSpec newColumn) {

    return this.statement.getOperations().renameColumn(column, newColumn);
  }

  @Override
  public AlterTableOperations<E> renameConstraint(String constraint, String newName) {

    return this.statement.getOperations().renameConstraint(constraint, newName);
  }

  @Override
  public AlterTableOperations<E> renameConstraint(DbConstraint constraint, String newName) {

    return this.statement.getOperations().renameConstraint(constraint, newName);
  }

  @Override
  // make visible
  protected AliasMap getAliasMap() {

    return super.getAliasMap();
  }

}
