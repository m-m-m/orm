/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.impl;

import io.github.mmm.orm.statement.DbStatementType;

/**
 * Implementation of {@link DbStatementType}.
 *
 * @since 1.0.0
 */
public enum DbStatementTypeImpl implements DbStatementType {

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.select.SelectStatement}. */
  SELECT(false),

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.insert.InsertStatement}. */
  INSERT(false),

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.update.UpdateStatement}. */
  UPDATE(false),

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.delete.DeleteStatement}. */
  DELETE(false),

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.merge.MergeStatement}. */
  MERGE(false),

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.upsert.UpsertStatement}. */
  UPSERT(false),

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.create.CreateTableStatement}. */
  CREATE_TABLE(true),

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.create.CreateSequenceStatement}. */
  CREATE_SEQUENCE(true),

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.create.CreateIndexStatement}. */
  CREATE_INDEX(true),

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.alter.AlterTableStatement}. */
  ALTER_TABLE(true),

  /** {@link DbStatementType} of {@link io.github.mmm.orm.statement.drop.DropTableStatement}. */
  DROP_TABLE(true);

  private final boolean ddl;

  private DbStatementTypeImpl(boolean ddl) {

    this.ddl = ddl;
  }

  @Override
  public boolean isDdl() {

    return this.ddl;
  }

  @Override
  public boolean isDml() {

    return !this.ddl;
  }

  @Override
  public boolean isQuery() {

    return (this == SELECT);
  }

}
