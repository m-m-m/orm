/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.insert;

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
 * {@link DbStatement} to {@link InsertClause insert} data into the database.
 *
 * @param <E> type of the {@link InsertInto#getEntity() entity}.
 * @since 1.0.0
 */
// TODO: support sub-query as alternative to values
// INSERT INTO table4 ( name, age, sex, city, id, number, nationality)
// SELECT name, age, sex, city, p.id, number, n.nationality
// FROM table1 p
// INNER JOIN table2 c ON c.Id = p.Id
// INNER JOIN table3 n ON p.Id = n.Id
public class InsertStatement<E extends EntityBean> extends IntoValuesStatement<E> {

  private final InsertClause insert;

  private final InsertInto<E> into;

  private final InsertValues<E> values;

  /**
   * The constructor.
   *
   * @param insert the {@link #getInsert() insert}.
   * @param into the #getInto
   */
  public InsertStatement(InsertClause insert, InsertInto<E> into) {

    super();
    this.insert = insert;
    this.into = into;
    this.values = new InsertValues<>(this);
  }

  /**
   * @deprecated use {@link #getInsert()} to make it more explicit.
   */
  @Deprecated
  @Override
  public InsertClause getStart() {

    return this.insert;
  }

  /**
   * @return the opening {@link InsertClause}-{@link DbClause}.
   */
  public InsertClause getInsert() {

    return this.insert;
  }

  /**
   * @return the {@link InsertInto Into}-{@link DbClause}.
   */
  @Override
  public InsertInto<E> getInto() {

    return this.into;
  }

  /**
   * @return the {@link InsertValues Values}-{@link DbClause} or {@code null} if none was added.
   */
  @Override
  public InsertValues<E> getValues() {

    return this.values;
  }

  @Override
  protected void addClauses(Consumer<AbstractDbClause> consumer) {

    consumer.accept(this.insert);
    consumer.accept(this.into);
    consumer.accept(this.values);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.INSERT;
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.into.getAliasMap();
  }

}
