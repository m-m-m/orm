/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import java.util.function.Consumer;

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
 * {@link DbStatement} to {@link CreateTableClause create a table}.
 *
 * @param <E> type of the {@link AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class CreateTableStatement<E extends EntityBean> extends AbstractDbStatement<E> {

  private final CreateTableClause<E> createTable;

  private final CreateTableContentsClause<E> contents;

  /**
   * The constructor.
   *
   * @param createTable the {@link #getCreateTable() create table}.
   */
  public CreateTableStatement(CreateTableClause<E> createTable) {

    super();
    this.createTable = createTable;
    this.contents = new CreateTableContentsClause<>(this);
  }

  /**
   * @deprecated use {@link #getCreateTable()} to make it more explicit.
   */
  @Deprecated
  @Override
  public StartClause getStart() {

    return this.createTable;
  }

  /**
   * @return the opening {@link CreateTableClause}-{@link DbClause}.
   */
  public CreateTableClause<E> getCreateTable() {

    return this.createTable;
  }

  /**
   * @return columns
   */
  public CreateTableContentsClause<E> getContents() {

    return this.contents;
  }

  @Override
  protected void addClauses(Consumer<AbstractDbClause> consumer) {

    consumer.accept(this.createTable);
    consumer.accept(this.contents);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.CREATE_TABLE;
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.createTable.getAliasMap();
  }

}
