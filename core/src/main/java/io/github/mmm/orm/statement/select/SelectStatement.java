/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import java.util.function.Consumer;

import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.AbstractDbStatement;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.DbStatementType;
import io.github.mmm.orm.statement.impl.DbStatementTypeImpl;

/**
 * {@link DbStatement} to query data from the database using a {@link SelectClause}.
 *
 * @param <R> type of the result of the selection.
 * @since 1.0.0
 */
public class SelectStatement<R> extends AbstractDbStatement<R> {

  private final SelectClause<R> select;

  private final SelectFromClause<R, ?> from;

  private final SelectWhere<R> where;

  private final GroupByClause<R> groupBy;

  private final HavingClause<R> having;

  private final OrderByClause<R> orderBy;

  /**
   * The constructor.
   *
   * @param select the {@link #getStart() starting} {@link SelectClause SELECT}.
   * @param from the {@link #getFrom() FROM}.
   */
  protected SelectStatement(SelectClause<R> select, SelectFromClause<R, ?> from) {

    super();
    this.select = select;
    this.from = from;
    this.where = new SelectWhere<>(this);
    this.groupBy = new GroupByClause<>(this);
    this.having = new HavingClause<>(this);
    this.orderBy = new OrderByClause<>(this);
  }

  /**
   * @deprecated use {@link #getSelect()} to make it more explicit.
   */
  @Deprecated
  @Override
  public SelectClause<R> getStart() {

    return this.select;
  }

  /**
   * @return the opening {@link SelectClause}.
   */
  public SelectClause<R> getSelect() {

    return this.select;
  }

  /**
   * @return the {@link SelectFromClause FROM} {@link DbClause}.
   */
  public SelectFromClause<R, ?> getFrom() {

    return this.from;
  }

  /**
   * @return the {@link SelectWhere Where}-{@link DbClause}.
   */
  public SelectWhere<R> getWhere() {

    return this.where;
  }

  /**
   * @return the {@link GroupByClause}-{@link DbClause}.
   */
  public GroupByClause<R> getGroupBy() {

    return this.groupBy;
  }

  /**
   * @return the {@link HavingClause}-{@link DbClause}.
   */
  public HavingClause<R> getHaving() {

    return this.having;
  }

  /**
   * @return the {@link OrderByClause}-{@link DbClause}.
   */
  public OrderByClause<R> getOrderBy() {

    return this.orderBy;
  }

  @Override
  protected void addClauses(Consumer<AbstractDbClause> consumer) {

    consumer.accept(this.select);
    consumer.accept(this.from);
    consumer.accept(this.where);
    consumer.accept(this.groupBy);
    consumer.accept(this.having);
    consumer.accept(this.orderBy);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.SELECT;
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.from.getAliasMap();
  }

}
