/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

/**
 * Meta-information about a database table. Extends {@link DbTableData} with advanced details like {@link #getColumns()
 * columns}, {@link #getPrimaryKey() primary key}, {@link #getForeignKeys() foreign keys}, and {@link #getIndexes()
 * indexes}.
 *
 * @since 1.0.0
 */
public interface DbTable extends DbTableData {

  /**
   * @return the {@link DbPrimaryKey primary key}.
   */
  DbPrimaryKey getPrimaryKey();

  /**
   * @return the {@link DbObjectContainer container} with the {@link DbColumn columns} of this table.
   */
  DbObjectContainer<DbColumn> getColumns();

  /**
   * @return the {@link DbObjectContainer container} with the {@link DbForeignKey foreign keys} of this table.
   */
  DbObjectContainer<DbForeignKey> getForeignKeys();

  /**
   * @return the {@link DbObjectContainer container} with the {@link DbIndex indexes} of this table.
   */
  DbObjectContainer<DbIndex> getIndexes();

}
