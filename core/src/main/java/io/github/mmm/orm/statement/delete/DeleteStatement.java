/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.delete;

import java.util.List;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.AbstractDbStatement;
import io.github.mmm.orm.statement.AbstractEntityClause;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.StartClause;

/**
 * {@link DbStatement} to {@link Delete}
 *
 * @param <E> type of the {@link AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class DeleteStatement<E extends EntityBean> extends AbstractDbStatement<E> {

  private final Delete delete;

  private final DeleteFrom<E> from;

  private final DeleteWhere<E> where;

  /**
   * The constructor.
   *
   * @param delete the {@link #getDelete() delete}.
   * @param from the #getFrom
   */
  public DeleteStatement(Delete delete, DeleteFrom<E> from) {

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
   * @return the opening {@link Delete}-{@link DbClause}.
   */
  public Delete getDelete() {

    return this.delete;
  }

  /**
   * @return the {@link DeleteFrom From}-{@link DbClause}.
   */
  public DeleteFrom<E> getFrom() {

    return this.from;
  }

  /**
   * @return the {@link DeleteWhere Where}-{@link DbClause}.
   */
  public DeleteWhere<E> getWhere() {

    return this.where;
  }

  @Override
  protected void addClauses(List<AbstractDbClause> list) {

    list.add(this.delete);
    list.add(this.from);
    list.add(this.where);
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.from.getAliasMap();
  }

}
