/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.orm.ddl.operation.TableOperation;
import io.github.mmm.orm.statement.alter.AlterTable;
import io.github.mmm.orm.statement.alter.AlterTableOperations;
import io.github.mmm.orm.statement.create.CreateIndex;
import io.github.mmm.orm.statement.create.CreateIndexColumns;
import io.github.mmm.orm.statement.create.CreateIndexOn;
import io.github.mmm.orm.statement.create.CreateTable;
import io.github.mmm.orm.statement.create.CreateTableContents;
import io.github.mmm.orm.statement.delete.Delete;
import io.github.mmm.orm.statement.insert.Insert;
import io.github.mmm.orm.statement.merge.Merge;
import io.github.mmm.orm.statement.select.GroupBy;
import io.github.mmm.orm.statement.select.Having;
import io.github.mmm.orm.statement.select.OrderBy;
import io.github.mmm.orm.statement.select.Select;
import io.github.mmm.orm.statement.update.Update;
import io.github.mmm.orm.statement.upsert.Upsert;

/**
 * Interface for visitor on {@link DbClause}. Override individual methods you are interested in. Typically do something
 * before and/or after delegating to parent method ({@code super.on...(...)}).
 *
 * @since 1.0.0
 */
public interface DbClauseVisitor {

  /**
   * @param clause the {@link DbClause} to visit.
   * @return this {@link DbClauseVisitor} itself for fluent API calls.
   */
  default DbClauseVisitor onClause(DbClause clause) {

    if (clause instanceof StartClause) {
      onStart((StartClause) clause);
    } else if (clause instanceof MainDbClause) {
      onMainClause((MainDbClause<?>) clause);
    } else {
      onOtherClause(clause);
    }
    return this;
  }

  /**
   * @param clause the {@link DbClause} that is neither a {@link StartClause} nor a {@link MainDbClause}.
   * @return this {@link DbClauseVisitor} itself for fluent API calls.
   */
  default DbClauseVisitor onOtherClause(DbClause clause) {

    if (clause instanceof IntoClause<?, ?, ?> into) {
      onInto(into);
    } else if (clause instanceof CreateIndexOn<?> on) {
      onCreateIndexOn(on);
    }
    return this;
  }

  /**
   * @param clause the {@link DbClause} to visit.
   * @return this {@link DbClauseVisitor} itself for fluent API calls.
   */
  default DbClauseVisitor onMainClause(MainDbClause<?> clause) {

    if (clause instanceof FromClause<?, ?, ?> from) {
      onFrom(from);
    } else if (clause instanceof WhereClause<?, ?> where) {
      onWhere(where);
    } else if (clause instanceof GroupBy<?> groupBy) {
      onGroupBy(groupBy);
    } else if (clause instanceof Having<?> having) {
      onHaving(having);
    } else if (clause instanceof OrderBy<?> orderBy) {
      onOrderBy(orderBy);
    } else if (clause instanceof ValuesClause<?, ?> values) {
      onValues(values);
    } else if (clause instanceof SetClause<?, ?> set) {
      onSet(set);
    } else if (clause instanceof CreateTableContents<?> columns) {
      onCreateTableContents(columns);
    } else if (clause instanceof CreateIndexColumns<?> columns) {
      onCreateIndexColumns(columns);
    } else if (clause instanceof AlterTableOperations<?> addColumns) {
      onAlterTableOperations(addColumns);
    }
    return this;
  }

  /**
   * @param start the {@link StartClause} to visit.
   */
  default void onStart(StartClause start) {

    if (start instanceof Select) {
      onSelect((Select<?>) start);
    } else if (start instanceof Update update) {
      onUpdate(update);
    } else if (start instanceof Insert insert) {
      onInsert(insert);
    } else if (start instanceof Delete delete) {
      onDelete(delete);
    } else if (start instanceof Merge merge) {
      onMerge(merge);
    } else if (start instanceof Upsert upsert) {
      onUpsert(upsert);
    } else if (start instanceof CreateTable<?> createTable) {
      onCreateTable(createTable);
    } else if (start instanceof CreateIndex createIndex) {
      onCreateIndex(createIndex);
    } else if (start instanceof AlterTable<?> alterTable) {
      onAlterTable(alterTable);
    }
  }

  /**
   * @param select the {@link Select}-{@link DbClause} to visit.
   */
  default void onSelect(Select<?> select) {

  }

  /**
   * @param update the {@link Update}-{@link DbClause} to visit.
   */
  default void onUpdate(Update<?> update) {

  }

  /**
   * @param insert the {@link Insert}-{@link DbClause} to visit.
   */
  default void onInsert(Insert insert) {

  }

  /**
   * @param delete the {@link Delete}-{@link DbClause} to visit.
   */
  default void onDelete(Delete delete) {

  }

  /**
   * @param merge the {@link Merge}-{@link DbClause} to visit.
   */
  default void onMerge(Merge merge) {

  }

  /**
   * @param upsert the {@link Upsert}-{@link DbClause} to visit.
   */
  default void onUpsert(Upsert upsert) {

  }

  /**
   * @param createTable the {@link CreateTable}-{@link DbClause} to visit.
   */
  default void onCreateTable(CreateTable<?> createTable) {

  }

  /**
   * @param createIndex the {@link CreateIndex}-{@link DbClause} to visit.
   */
  default void onCreateIndex(CreateIndex createIndex) {

  }

  /**
   * @param alterTable the {@link AlterTable}-{@link DbClause} to visit.
   */
  default void onAlterTable(AlterTable<?> alterTable) {

  }

  /**
   * @param from the {@link FromClause}-{@link DbClause} to visit.
   */
  default void onFrom(FromClause<?, ?, ?> from) {

  }

  /**
   * @param where the {@link WhereClause}-{@link DbClause} to visit.
   */
  default void onWhere(WhereClause<?, ?> where) {

  }

  /**
   * @param groupBy the {@link GroupBy}-{@link DbClause} to visit.
   */
  default void onGroupBy(GroupBy<?> groupBy) {

  }

  /**
   * @param having the {@link Having}-{@link DbClause} to visit.
   */
  default void onHaving(Having<?> having) {

  }

  /**
   * @param orderBy the {@link OrderBy}-{@link DbClause} to visit.
   */
  default void onOrderBy(OrderBy<?> orderBy) {

  }

  /**
   * @param into the {@link IntoClause}-{@link DbClause} to visit.
   */
  default void onInto(IntoClause<?, ?, ?> into) {

  }

  /**
   * @param values the {@link ValuesClause}-{@link DbClause} to visit.
   */
  default void onValues(ValuesClause<?, ?> values) {

  }

  /**
   * @param contents the {@link CreateTableContents}-{@link DbClause} to visit.
   */
  default void onCreateTableContents(CreateTableContents<?> contents) {

  }

  /**
   * @param set the {@link SetClause}-{@link DbClause} to visit.
   */
  default void onSet(SetClause<?, ?> set) {

  }

  /**
   * @param on the {@link CreateIndexOn}-{@link DbClause} to visit.
   */
  default void onCreateIndexOn(CreateIndexOn<?> on) {

  }

  /**
   * @param columns the {@link CreateIndexColumns}-{@link DbClause} to visit.
   */
  default void onCreateIndexColumns(CreateIndexColumns<?> columns) {

  }

  /**
   * @param operations the {@link AlterTableOperations}-{@link DbClause} to visit.
   */
  default void onAlterTableOperations(AlterTableOperations<?> operations) {

    for (TableOperation operation : operations.getOperations()) {
      onAlterTableOperation(operation);
    }
  }

  /**
   * @param operation the {@link TableOperation} to visit.
   */
  default void onAlterTableOperation(TableOperation operation) {

  }
}
