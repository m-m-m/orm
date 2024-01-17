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
 * {@link DbStatement} to {@link CreateIndexClause create an index}
 *
 * @param <E> type of the {@link AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class CreateIndexStatement<E extends EntityBean> extends AbstractDbStatement<E> {

  private final CreateIndexClause createIndex;

  private final CreateIndexOnClause<E> on;

  private final CreateIndexColumns<E> column;

  /**
   * The constructor.
   *
   * @param createIndex the {@link #getCreateIndex() create index clause}.
   * @param on the {@link #getOn() on clause}.
   */
  public CreateIndexStatement(CreateIndexClause createIndex, CreateIndexOnClause<E> on) {

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
   * @return the opening {@link CreateIndexClause}-{@link DbClause}.
   */
  public CreateIndexClause getCreateIndex() {

    return this.createIndex;
  }

  /**
   * @return the {@link CreateIndexOnClause}-{@link DbClause}.
   */
  public CreateIndexOnClause<E> getOn() {

    return this.on;
  }

  /**
   * @return the {@link CreateIndexColumns}-{@link DbClause}.
   */
  public CreateIndexColumns<E> getColumn() {

    return this.column;
  }

  @Override
  protected void addClauses(Consumer<AbstractDbClause> consumer) {

    consumer.accept(this.createIndex);
    consumer.accept(this.on);
    consumer.accept(this.column);
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
