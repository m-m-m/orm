/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.delete;

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
 * {@link DbStatement} to {@link DeleteClause}
 *
 * @param <E> type of the {@link AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class DeleteStatement<E extends EntityBean> extends AbstractDbStatement<E> {

  private final DeleteClause delete;

  private final DeleteFromClause<E> from;

  private final DeleteWhere<E> where;

  /**
   * The constructor.
   *
   * @param delete the {@link #getDelete() delete}.
   * @param from the #getFrom
   */
  public DeleteStatement(DeleteClause delete, DeleteFromClause<E> from) {

    super();
    this.delete = delete;
    this.from = from;
    this.where = new DeleteWhere<>(this);
  }

  /**
   * @deprecated use {@link #getDelete()} to make it more explicit.
   */
  @Deprecated
  @Override
  public StartClause getStart() {

    return this.delete;
  }

  /**
   * @return the opening {@link DeleteClause}-{@link DbClause}.
   */
  public DeleteClause getDelete() {

    return this.delete;
  }

  /**
   * @return the {@link DeleteFromClause From}-{@link DbClause}.
   */
  public DeleteFromClause<E> getFrom() {

    return this.from;
  }

  /**
   * @return the {@link DeleteWhere Where}-{@link DbClause}.
   */
  public DeleteWhere<E> getWhere() {

    return this.where;
  }

  @Override
  protected void addClauses(Consumer<AbstractDbClause> consumer) {

    consumer.accept(this.delete);
    consumer.accept(this.from);
    consumer.accept(this.where);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.DELETE;
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.from.getAliasMap();
  }

}
