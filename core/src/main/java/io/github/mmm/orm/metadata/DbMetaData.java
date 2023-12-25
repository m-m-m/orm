/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

/**
 * Meta-information about the database (DDL).
 *
 * @since 1.0.0
 */
public interface DbMetaData {

  /**
   * @return an {@link Iterable} with the {@link DbTableData} of all tables in the current catalog/schema.
   */
  default Iterable<DbTableData> getTables() {

    return getTables(null);
  }

  /**
   * @param name the {@link DbQualifiedName#getName() name} or name-pattern of the tables.
   * @return an {@link Iterable} with the {@link DbTableData} of the matching tables in the current catalog/schema.
   */
  Iterable<DbTableData> getTables(String name);

  /**
   * @param catalog the {@link DbQualifiedName#getCatalog() catalog}. May be {@code null}.
   * @param schema the {@link DbQualifiedName#getSchema() schema}. May be {@code null}.
   * @param name the {@link DbQualifiedName#getName() name} or name-pattern of the tables.
   * @return an {@link Iterable} with the {@link DbTableData} of the matching tables.
   */
  Iterable<DbTableData> getTables(String catalog, String schema, String name);

  /**
   * @param name the {@link DbTable#getQualifiedName() qualified table name}.
   * @return the {@link DbTable} with the given {@link DbTable#getQualifiedName() name} or {@code null} if no such table
   *         exists.
   */
  DbTable getTable(DbQualifiedName name);

  /**
   * @param name the {@link DbTable#getQualifiedName() qualified table name}.
   * @return the {@link DbTable} with the given {@link DbTable#getQualifiedName() name} or {@code null} if no such table
   *         exists.
   */
  DbTable getTable(DbName name);

  /**
   * @param tableData the {@link DbTableData} (from {@link #getTables(String, String, String)}).
   * @return the {@link DbTable} populated with additional information.
   */
  DbTable getTable(DbTableData tableData);

  /**
   * @return the current {@link DbQualifiedName#getCatalog() catalog}.
   */
  DbName getCurrentCatalog();

  /**
   * @return the current {@link DbQualifiedName#getSchema() schema}.
   */
  DbName getCurrentSchema();

}
