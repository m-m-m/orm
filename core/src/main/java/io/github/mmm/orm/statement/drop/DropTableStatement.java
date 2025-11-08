/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.drop;

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
 * {@link DbStatement} to {@link DropTableClause drop a table}.
 *
 * @param <E> type of the {@link AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class DropTableStatement<E extends EntityBean> extends AbstractDbStatement<E> {

  private final DropTableClause<E> dropTable;

  /**
   * The constructor.
   *
   * @param dropTable the {@link #getDropTable() drop table}.
   */
  public DropTableStatement(DropTableClause<E> dropTable) {

    super();
    this.dropTable = dropTable;
  }

  /**
   * @deprecated use {@link #getDropTable()} to make it more explicit.
   */
  @Deprecated
  @Override
  public StartClause getStart() {

    return this.dropTable;
  }

  /**
   * @return the opening {@link DropTableClause}-{@link DbClause}.
   */
  public DropTableClause<E> getDropTable() {

    return this.dropTable;
  }

  @Override
  protected void addClauses(Consumer<AbstractDbClause> consumer) {

    consumer.accept(this.dropTable);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.DROP_TABLE;
  }

  @Override
  protected AliasMap getAliasMap() {

    return this.dropTable.getAliasMap();
  }

}
