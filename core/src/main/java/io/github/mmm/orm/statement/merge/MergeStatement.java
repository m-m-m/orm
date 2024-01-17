/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.merge;

import java.util.function.Consumer;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.DbStatementType;
import io.github.mmm.orm.statement.IntoValuesStatement;
import io.github.mmm.orm.statement.impl.DbStatementTypeImpl;
import io.github.mmm.orm.statement.upsert.UpsertInto;
import io.github.mmm.orm.statement.upsert.UpsertValues;

/**
 * {@link DbStatement} to {@link MergeClause} data into the database.
 *
 * @param <E> type of the {@link UpsertInto#getEntity() entity}.
 * @since 1.0.0
 */
public class MergeStatement<E extends EntityBean> extends IntoValuesStatement<E> {

  private final MergeClause merge;

  private final MergeInto<E> into;

  private final MergeValues<E> values;

  /**
   * The constructor.
   *
   * @param merge the {@link #getMerge() MERGE}.
   * @param into the {@link #getInto() INTO}.
   */
  public MergeStatement(MergeClause merge, MergeInto<E> into) {

    super();
    this.merge = merge;
    this.into = into;
    this.values = new MergeValues<>(this);
  }

  /**
   * @deprecated use {@link #getMerge()} to make it more explicit.
   */
  @Deprecated
  @Override
  public MergeClause getStart() {

    return this.merge;
  }

  /**
   * @return the opening {@link MergeClause}-{@link DbClause clause}.
   */
  public MergeClause getMerge() {

    return this.merge;
  }

  /**
   * @return the {@link MergeInto INTO}-{@link DbClause clause}.
   */
  @Override
  public MergeInto<E> getInto() {

    return this.into;
  }

  /**
   * @return the {@link UpsertValues VALUES}-{@link DbClause clause} or {@code null} if none was added.
   */
  @Override
  public MergeValues<E> getValues() {

    return this.values;
  }

  @Override
  protected void addClauses(Consumer<AbstractDbClause> consumer) {

    consumer.accept(this.merge);
    consumer.accept(this.into);
    consumer.accept(this.values);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.MERGE;
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.into.getAliasMap();
  }

}
