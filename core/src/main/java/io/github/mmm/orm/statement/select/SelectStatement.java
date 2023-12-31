/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import java.util.List;

import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.AbstractDbStatement;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.DbStatementType;
import io.github.mmm.orm.statement.impl.DbStatementTypeImpl;

/**
 * {@link DbStatement} to query data from the database using a {@link Select}.
 *
 * @param <R> type of the result of the selection.
 * @since 1.0.0
 */
public class SelectStatement<R> extends AbstractDbStatement<R> {

  private final Select<R> select;

  private final SelectFrom<R, ?> from;

  private final SelectWhere<R> where;

  private final GroupBy<R> groupBy;

  private final Having<R> having;

  private final OrderBy<R> orderBy;

  /**
   * The constructor.
   *
   * @param select the {@link #getStart() starting} {@link Select SELECT}.
   * @param from the {@link #getFrom() FROM}.
   */
  protected SelectStatement(Select<R> select, SelectFrom<R, ?> from) {

    super();
    select.setStatement(this);
    this.select = select;
    this.from = from;
    this.where = new SelectWhere<>(this);
    this.groupBy = new GroupBy<>(this);
    this.having = new Having<>(this);
    this.orderBy = new OrderBy<>(this);
  }

  /**
   * @deprecated use {@link #getSelect()} to make it more explicit.
   */
  @Deprecated
  @Override
  public Select<R> getStart() {

    return this.select;
  }

  /**
   * @return the opening {@link Select}.
   */
  public Select<R> getSelect() {

    return this.select;
  }

  /**
   * @return the {@link SelectFrom FROM} {@link DbClause}.
   */
  public SelectFrom<R, ?> getFrom() {

    return this.from;
  }

  /**
   * @return the {@link SelectWhere Where}-{@link DbClause}.
   */
  public SelectWhere<R> getWhere() {

    return this.where;
  }

  /**
   * @return the {@link GroupBy}-{@link DbClause}.
   */
  public GroupBy<R> getGroupBy() {

    return this.groupBy;
  }

  /**
   * @return the {@link Having}-{@link DbClause}.
   */
  public Having<R> getHaving() {

    return this.having;
  }

  /**
   * @return the {@link OrderBy}-{@link DbClause}.
   */
  public OrderBy<R> getOrderBy() {

    return this.orderBy;
  }

  @Override
  protected void addClauses(List<AbstractDbClause> list) {

    list.add(this.select);
    list.add(this.from);
    list.add(this.where);
    list.add(this.groupBy);
    list.add(this.having);
    list.add(this.orderBy);
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
