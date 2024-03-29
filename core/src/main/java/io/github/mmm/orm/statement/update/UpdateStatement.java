/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.update;

import java.util.function.Consumer;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.AbstractDbStatement;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.DbStatementType;
import io.github.mmm.orm.statement.impl.DbStatementTypeImpl;
import io.github.mmm.orm.statement.insert.InsertClause;
import io.github.mmm.orm.statement.insert.InsertInto;

/**
 * {@link DbStatement} to {@link InsertClause insert} data into the database.
 *
 * @param <E> type of the {@link InsertInto#getEntity() entity}.
 * @since 1.0.0
 */
public class UpdateStatement<E extends EntityBean> extends AbstractDbStatement<E> {

  private final UpdateClause<E> update;

  private final UpdateSet<E> set;

  private final UpdateWhere<E> where;

  /**
   * The constructor.
   *
   * @param update the {@link #getUpdate() update}.
   */
  public UpdateStatement(UpdateClause<E> update) {

    super();
    this.update = update;
    this.set = new UpdateSet<>(this);
    this.where = new UpdateWhere<>(this);
  }

  /**
   * @deprecated use {@link #getUpdate()} to make it more explicit.
   */
  @Deprecated
  @Override
  public UpdateClause<E> getStart() {

    return this.update;
  }

  /**
   * @return the opening {@link UpdateClause}-{@link DbClause}.
   */
  public UpdateClause<E> getUpdate() {

    return this.update;
  }

  /**
   * @return the {@link UpdateSet Set}-{@link DbClause}.
   */
  public UpdateSet<E> getSet() {

    return this.set;
  }

  /**
   * @return the {@link UpdateWhere Where}-{@link DbClause}.
   */
  public UpdateWhere<E> getWhere() {

    return this.where;
  }

  @Override
  protected void addClauses(Consumer<AbstractDbClause> consumer) {

    consumer.accept(this.update);
    consumer.accept(this.set);
    consumer.accept(this.where);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.UPDATE;
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.update.getAliasMap();
  }

}
