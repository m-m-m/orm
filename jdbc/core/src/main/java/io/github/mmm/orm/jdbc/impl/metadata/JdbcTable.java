/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.impl.metadata;

import java.sql.DatabaseMetaData;

import io.github.mmm.orm.metadata.DbColumn;
import io.github.mmm.orm.metadata.DbForeignKey;
import io.github.mmm.orm.metadata.DbIndex;
import io.github.mmm.orm.metadata.DbObjectContainer;
import io.github.mmm.orm.metadata.DbPrimaryKey;
import io.github.mmm.orm.metadata.DbTableData;
import io.github.mmm.orm.metadata.impl.DbTableDataImpl;
import io.github.mmm.orm.metadata.impl.DbTableImpl;

/**
 * JDBC implementation of {@link DbTableImpl}.
 */
public class JdbcTable extends DbTableImpl {

  private DatabaseMetaData metaData;

  /**
   * The constructor.
   *
   * @param data the {@link DbTableDataImpl}.
   * @param columns the {@link #getColumns() columns}.
   * @param metaData the JDBC {@link DatabaseMetaData}.
   */
  public JdbcTable(DbTableData data, DbObjectContainer<DbColumn> columns, DatabaseMetaData metaData) {

    super(data, columns, null, null, null);
    this.metaData = metaData;
  }

  @Override
  protected DbObjectContainer<DbForeignKey> loadForeignKeys() {

    return JdbcMetaData.extractForeignKeys(this.qualifiedName, this.metaData);
  }

  @Override
  protected DbObjectContainer<DbIndex> loadIndexes() {

    return JdbcMetaData.extractIndexes(this.qualifiedName, getColumns(), this.metaData);
  }

  @Override
  protected DbPrimaryKey loadPrimaryKey() {

    return JdbcMetaData.extractPrimaryKey(this, this.metaData);
  }

}
