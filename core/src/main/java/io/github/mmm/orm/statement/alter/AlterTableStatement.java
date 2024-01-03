/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.alter;

import java.util.List;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.AbstractDbStatement;
import io.github.mmm.orm.statement.AbstractEntityClause;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.DbStatementType;
import io.github.mmm.orm.statement.StartClause;
import io.github.mmm.orm.statement.impl.DbStatementTypeImpl;

/**
 * {@link DbStatement} to {@link AlterTable alter a table}.
 *
 * @param <E> type of the {@link AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class AlterTableStatement<E extends EntityBean> extends AbstractDbStatement<E> {

  private final AlterTable<E> alterTable;

  private final AlterTableOperations<E> operations;

  /**
   * The constructor.
   *
   * @param createTable the {@link #getAlterTable() alter table}.
   */
  public AlterTableStatement(AlterTable<E> createTable) {

    super();
    this.alterTable = createTable;
    this.operations = new AlterTableOperations<>(this);
  }

  /**
   * @deprecated use {@link #getAlterTable()} to make it more explicit.
   */
  @Deprecated
  @Override
  public StartClause getStart() {

    return this.alterTable;
  }

  /**
   * @return the opening {@link AlterTable}-{@link DbClause}.
   */
  public AlterTable<E> getAlterTable() {

    return this.alterTable;
  }

  /**
   * @return the {@link AlterTableOperations} clause.
   */
  public AlterTableOperations<E> getOperations() {

    return this.operations;
  }

  @Override
  protected void addClauses(List<AbstractDbClause> list) {

    list.add(this.alterTable);
    list.add(this.operations);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.ALTER_TABLE;
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.alterTable.getAliasMap();
  }

}
