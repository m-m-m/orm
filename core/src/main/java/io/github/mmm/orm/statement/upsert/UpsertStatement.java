/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.upsert;

import java.util.function.Consumer;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.DbStatementType;
import io.github.mmm.orm.statement.IntoValuesStatement;
import io.github.mmm.orm.statement.impl.DbStatementTypeImpl;

/**
 * {@link DbStatement} to {@link UpsertClause} data into the database.
 *
 * @param <E> type of the {@link UpsertInto#getEntity() entity}.
 * @since 1.0.0
 */
public class UpsertStatement<E extends EntityBean> extends IntoValuesStatement<E> {

  private final UpsertClause upsert;

  private final UpsertInto<E> into;

  private final UpsertValues<E> values;

  /**
   * The constructor.
   *
   * @param upsert the {@link #getUpsert() upsert}.
   * @param into the #getInto
   */
  public UpsertStatement(UpsertClause upsert, UpsertInto<E> into) {

    super();
    this.upsert = upsert;
    this.into = into;
    this.values = new UpsertValues<>(this);
  }

  /**
   * @deprecated use {@link #getUpsert()} to make it more explicit.
   */
  @Deprecated
  @Override
  public UpsertClause getStart() {

    return this.upsert;
  }

  /**
   * @return the opening {@link UpsertClause}-{@link DbClause clause}.
   */
  public UpsertClause getUpsert() {

    return this.upsert;
  }

  /**
   * @return the {@link UpsertInto INTO}-{@link DbClause clause}.
   */
  @Override
  public UpsertInto<E> getInto() {

    return this.into;
  }

  /**
   * @return the {@link UpsertValues VALUES}-{@link DbClause clause} or {@code null} if none was added.
   */
  @Override
  public UpsertValues<E> getValues() {

    return this.values;
  }

  @Override
  protected void addClauses(Consumer<AbstractDbClause> consumer) {

    consumer.accept(this.upsert);
    consumer.accept(this.into);
    consumer.accept(this.values);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.UPSERT;
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.into.getAliasMap();
  }

}
