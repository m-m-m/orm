/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

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
 * {@link DbStatement} to {@link CreateIndex create an index}
 *
 * @param <E> type of the {@link AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class CreateIndexStatement<E extends EntityBean> extends AbstractDbStatement<E> {

  private final CreateIndex createIndex;

  private final CreateIndexOn<E> on;

  private final CreateIndexColumns<E> column;

  /**
   * The constructor.
   *
   * @param createIndex the {@link #getCreateIndex() create index clause}.
   * @param on the {@link #getOn() on clause}.
   */
  public CreateIndexStatement(CreateIndex createIndex, CreateIndexOn<E> on) {

    super();
    this.createIndex = createIndex;
    this.on = on;
    this.column = new CreateIndexColumns<>(this);
  }

  /**
   * @deprecated use {@link #getCreateIndex()} to make it more explicit.
   */
  @Deprecated
  @Override
  public StartClause getStart() {

    return this.createIndex;
  }

  /**
   * @return the opening {@link CreateIndex}-{@link DbClause}.
   */
  public CreateIndex getCreateIndex() {

    return this.createIndex;
  }

  /**
   * @return the {@link CreateIndexOn}-{@link DbClause}.
   */
  public CreateIndexOn<E> getOn() {

    return this.on;
  }

  /**
   * @return the {@link CreateIndexColumns}-{@link DbClause}.
   */
  public CreateIndexColumns<E> getColumn() {

    return this.column;
  }

  @Override
  protected void addClauses(List<AbstractDbClause> list) {

    list.add(this.createIndex);
    list.add(this.on);
    list.add(this.column);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.CREATE_INDEX;
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.on.getAliasMap();
  }

}
