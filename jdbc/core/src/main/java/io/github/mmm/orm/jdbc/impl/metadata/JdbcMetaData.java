/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.impl.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.base.exception.ObjectMismatchException;
import io.github.mmm.base.sort.SortOrder;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.metadata.DbColumn;
import io.github.mmm.orm.metadata.DbColumnReference;
import io.github.mmm.orm.metadata.DbForeignKey;
import io.github.mmm.orm.metadata.DbIndex;
import io.github.mmm.orm.metadata.DbMetaData;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbObjectContainer;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.metadata.DbTable;
import io.github.mmm.orm.metadata.DbTableData;
import io.github.mmm.orm.metadata.DbTableType;
import io.github.mmm.orm.metadata.impl.DbColumnImpl;
import io.github.mmm.orm.metadata.impl.DbColumnReferenceImpl;
import io.github.mmm.orm.metadata.impl.DbObjectContainerImplEmpty;
import io.github.mmm.orm.metadata.impl.DbObjectContainerImplList;
import io.github.mmm.orm.metadata.impl.DbObjectContainerImplMap;
import io.github.mmm.orm.metadata.impl.DbObjectContainerImplSingle;
import io.github.mmm.orm.metadata.impl.DbPrimaryKeyImpl;
import io.github.mmm.orm.metadata.impl.DbTableDataImpl;

/**
 * Implementation of {@link DbMetaData} for JDBC.
 *
 * @since 1.0.0
 */
public class JdbcMetaData implements DbMetaData {

  private static final Logger LOG = LoggerFactory.getLogger(JdbcMetaData.class);

  private static final String RS_TABLE_CATALOG = "TABLE_CAT";

  private static final String RS_TABLE_SCHEMA = "TABLE_SCHEM";

  private static final String RS_TABLE_NAME = "TABLE_NAME";

  private static final String RS_TABLE_TYPE = "TABLE_TYPE";

  private static final String RS_REMARKS = "REMARKS";

  private static final String RS_COLUMN_NAME = "COLUMN_NAME";

  private static final String RS_COLUMN_DATA_TYPE = "DATA_TYPE"; // SQL type (int)

  private static final String RS_COLUMN_TYPE_NAME = "TYPE_NAME";

  private static final String RS_COLUMN_SIZE = "COLUMN_SIZE";

  private static final String RS_COLUMN_DECIMAL_DIGITS = "DECIMAL_DIGITS";

  private static final String RS_COLUMN_NULLABLE = "IS_NULLABLE";

  private static final String RS_INDEX_TYPE = "TYPE";

  private static final String RS_INDEX_NAME = "INDEX_NAME";

  private static final String RS_ASC_OR_DESC = "ASC_OR_DESC";

  private static final String RS_FK_NAME = "FK_NAME";

  private static final String RS_FK_COLUMN_NAME = "FKCOLUMN_NAME";

  private static final String RS_PK_NAME = "PK_NAME";

  private static final String RS_PK_TABLE_CATALOG = "PKTABLE_CAT";

  private static final String RS_PK_TABLE_SCHEMA = "PKTABLE_SCHEM";

  private static final String RS_PK_TABLE_NAME = "PKTABLE_NAME";

  private static final DbName EMPTY = DbName.of("''");

  private final Connection connection;

  private final DatabaseMetaData metaData;

  private final DbDialect dialect;

  private DbName currentCatalog;

  private DbName currentSchema;

  /**
   * The constructor.
   *
   * @param connection the JDBC {@link Connection}.
   * @param dialect the {@link DbDialect} to use.
   */
  public JdbcMetaData(Connection connection, DbDialect dialect) {

    super();
    try {
      this.connection = connection;
      this.metaData = connection.getMetaData();
      this.dialect = dialect;
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public DbName getCurrentCatalog() {

    if (this.currentCatalog == null) {
      try {
        String catalog = this.connection.getCatalog();
        if (catalog == null) {
          this.currentCatalog = EMPTY;
        } else {
          this.currentCatalog = DbName.of(catalog);
        }
      } catch (SQLException e) {
        throw new IllegalStateException(e);
      }
    }
    if (this.currentCatalog == EMPTY) {
      return null;
    }
    return this.currentCatalog;
  }

  @Override
  public DbName getCurrentSchema() {

    if (this.currentSchema == null) {
      try {
        String schema = this.connection.getSchema();
        if (schema == null) {
          this.currentSchema = EMPTY;
        } else {
          this.currentSchema = DbName.of(schema);
        }
      } catch (SQLException e) {
        throw new IllegalStateException(e);
      }
    }
    if (this.currentSchema == EMPTY) {
      return null;
    }
    return this.currentSchema;
  }

  private String format(DbName name) {

    return DbName.format(name, this.dialect);
  }

  private static String formatPlain(DbName name) {

    return DbName.format(name, null);
  }

  @Override
  public Iterable<DbTableData> getTables(String name) {

    return getTables(DbName.getName(getCurrentCatalog()), DbName.getName(getCurrentSchema()), name);
  }

  @Override
  public List<DbTableData> getTables(String catalog, String schema, String name) {

    try {
      List<DbTableData> tables = new ArrayList<>();
      try (ResultSet resultSet = this.metaData.getTables(catalog, schema, name, null)) {
        while (resultSet.next()) {
          DbTableDataImpl tableData = extractTableData(resultSet);
          tables.add(tableData);
        }
      }
      return tables;
    } catch (SQLException e) {
      // TODO
      throw new IllegalStateException(e);
    }
  }

  @Override
  public DbTable getTable(DbQualifiedName qName) {

    try {
      Objects.requireNonNull(qName);
      String catalog = format(qName.getCatalog());
      String schema = format(qName.getSchema());
      String name = format(qName.getName());
      if ((catalog == null) && (schema == null)) {
        catalog = DbName.getName(getCurrentCatalog());
        schema = DbName.getName(getCurrentSchema());
      }
      return getTable(catalog, schema, name);
    } catch (SQLException e) {
      // TODO
      throw new IllegalStateException(e);
    }
  }

  @Override
  public DbTable getTable(DbName name) {

    try {
      return getTable(DbName.getName(getCurrentCatalog()), DbName.getName(getCurrentSchema()), format(name));
    } catch (SQLException e) {
      // TODO
      throw new IllegalStateException(e);
    }
  }

  private DbTable getTable(String catalog, String schema, String name) throws SQLException {

    DbTableData tableData = null;
    try (ResultSet resultSet = this.metaData.getTables(catalog, schema, name, null)) {
      while (resultSet.next()) {
        if (tableData == null) {
          tableData = extractTableData(resultSet);
        } else {
          DbTableData duplicate = extractTableData(resultSet);
          throw new DuplicateObjectException(duplicate, name, tableData);
        }
      }
    }
    if (tableData == null) {
      return null;
    }
    return getTable(tableData);
  }

  @Override
  public DbTable getTable(DbTableData tableData) {

    try {
      Objects.requireNonNull(tableData);
      assert (tableData instanceof DbTableDataImpl);
      DbQualifiedName qName = tableData.getQualifiedName();
      DbObjectContainer<DbColumn> columns;
      String catalog = formatPlain(qName.getCatalog());
      String schema = formatPlain(qName.getSchema());
      String name = qName.getName().get();
      try (ResultSet resultSet = this.metaData.getColumns(catalog, schema, name, "%")) {
        columns = extractColumns(resultSet);
      }
      return new JdbcTable(tableData, columns, this.metaData);
    } catch (SQLException e) {
      // TODO
      throw new IllegalStateException(e);
    }
  }

  private static DbObjectContainer<DbColumn> extractColumns(ResultSet resultSet) throws SQLException {

    Map<DbName, DbColumn> map = new HashMap<>();
    while (resultSet.next()) {
      DbColumnImpl column = extractColumn(resultSet);
      map.put(column.getName(), column);
    }
    return new DbObjectContainerImplMap<>(map);
  }

  private static DbColumnImpl extractColumn(ResultSet resultSet) throws SQLException {

    DbName name = DbName.of(resultSet.getString(RS_COLUMN_NAME));
    String comment = resultSet.getString(RS_REMARKS);
    Boolean nullable = asBoolean(resultSet.getString(RS_COLUMN_NULLABLE));
    int typeCode = resultSet.getInt(RS_COLUMN_DATA_TYPE);
    String typeName = resultSet.getString(RS_COLUMN_TYPE_NAME);
    int size = resultSet.getInt(RS_COLUMN_SIZE);
    int decimalDigits = resultSet.getInt(RS_COLUMN_DECIMAL_DIGITS);
    return new DbColumnImpl(name, comment, nullable, typeCode, typeName, size, decimalDigits);
  }

  private static Boolean asBoolean(String value) {

    if (value == null) {
      return null;
    }
    if ("TRUE".equalsIgnoreCase(value) || "YES".equalsIgnoreCase(value)) {
      return Boolean.TRUE;
    } else if ("FALSE".equalsIgnoreCase(value) || "NO".equalsIgnoreCase(value)) {
      return Boolean.FALSE;
    } else {
      throw new IllegalArgumentException(value);
    }
  }

  private static DbTableDataImpl extractTableData(ResultSet resultSet) throws SQLException {

    DbQualifiedName qName = extractTableName(resultSet);
    String type = resultSet.getString(RS_TABLE_TYPE);
    String comment = resultSet.getString(RS_REMARKS);
    return new DbTableDataImpl(qName, comment, DbTableType.of(type));
  }

  private static DbQualifiedName extractTableName(ResultSet resultSet) throws SQLException {

    DbName catalog = DbName.of(resultSet.getString(RS_TABLE_CATALOG));
    DbName schema = DbName.of(resultSet.getString(RS_TABLE_SCHEMA));
    DbName name = DbName.of(resultSet.getString(RS_TABLE_NAME));
    DbQualifiedName qName = new DbQualifiedName(catalog, schema, name);
    return qName;
  }

  static DbPrimaryKeyImpl extractPrimaryKey(DbTable table, DatabaseMetaData metaData) {

    DbQualifiedName qualifiedName = table.getQualifiedName();
    String catalog = formatPlain(qualifiedName.getCatalog());
    String schema = formatPlain(qualifiedName.getSchema());
    String name = formatPlain(qualifiedName.getName());
    try (ResultSet resultSet = metaData.getPrimaryKeys(catalog, schema, name)) {
      return extractPrimaryKey(resultSet, table.getColumns());
    } catch (SQLException e) {
      // TODO
      throw new IllegalStateException(e);
    }
  }

  private static DbPrimaryKeyImpl extractPrimaryKey(ResultSet resultSet, DbObjectContainer<DbColumn> columns)
      throws SQLException {

    JdbcContainerBuilder<DbColumn> columnBuilder = new JdbcContainerBuilder<>();
    String name = null;
    while (resultSet.next()) {
      String currentName = resultSet.getString(RS_PK_NAME);
      if (name == null) {
        name = currentName;
      } else if (!name.equals(currentName)) {
        throw new ObjectMismatchException(currentName, name);
      }
      String columnName = resultSet.getString(RS_COLUMN_NAME);
      DbColumn currentColumn = columns.get(DbName.of(columnName));
      columnBuilder.add(currentColumn);
    }
    if (columnBuilder.isEmpty()) {
      return null;
    }
    return new DbPrimaryKeyImpl(DbName.of(name), columnBuilder.build());
  }

  /**
   * @param qualifiedName the {@link DbQualifiedName} of the {@link DbTable table}.
   * @param metaData the {@link DatabaseMetaData}.
   * @return the {@link DbObjectContainer} with the {@link DbForeignKey}s for the specified {@link DbTable table}.
   */
  static DbObjectContainer<DbForeignKey> extractForeignKeys(DbQualifiedName qualifiedName, DatabaseMetaData metaData) {

    String catalog = formatPlain(qualifiedName.getCatalog());
    String schema = formatPlain(qualifiedName.getSchema());
    String name = qualifiedName.getName().get();
    try (ResultSet resultSet = metaData.getImportedKeys(catalog, schema, name)) {
      Map<DbName, JdbcFkBuilder> fkBuilderMap = new HashMap<>();
      while (resultSet.next()) {
        DbName fkName = DbName.of(resultSet.getString(RS_FK_NAME));
        JdbcFkBuilder builder = fkBuilderMap.computeIfAbsent(fkName, k -> new JdbcFkBuilder(fkName));
        DbColumnReference colRef = extractColumnReference(resultSet);
        builder.add(colRef);
      }
      int size = fkBuilderMap.size();
      if (size == 0) {
        return DbObjectContainerImplEmpty.get();
      } else if (size == 1) {
        return new DbObjectContainerImplSingle<>(fkBuilderMap.values().iterator().next().buildFk());
      } else {
        DbForeignKey[] array = new DbForeignKey[fkBuilderMap.size()];
        int i = 0;
        for (JdbcFkBuilder builder : fkBuilderMap.values()) {
          array[i++] = builder.buildFk();
        }
        return new DbObjectContainerImplList<>(array);
      }
    } catch (SQLException e) {
      // TODO
      throw new IllegalStateException(e);
    }
  }

  private static DbColumnReference extractColumnReference(ResultSet resultSet) throws SQLException {

    DbName tableCatalog = DbName.of(resultSet.getString(RS_PK_TABLE_CATALOG));
    DbName tableSchema = DbName.of(resultSet.getString(RS_PK_TABLE_SCHEMA));
    DbName tableLocalName = DbName.of(resultSet.getString(RS_PK_TABLE_NAME));
    DbQualifiedName tableName = new DbQualifiedName(tableCatalog, tableSchema, tableLocalName);
    DbName columnName = DbName.of(RS_FK_COLUMN_NAME);
    return new DbColumnReferenceImpl(columnName, tableName);
  }

  static DbObjectContainer<DbIndex> extractIndexes(DbQualifiedName qualifiedName, DbObjectContainer<DbColumn> columns,
      DatabaseMetaData metaData) {

    try {
      JdbcContainerBuilder<DbIndex> builder = new JdbcContainerBuilder<>();
      String catalog = formatPlain(qualifiedName.getCatalog());
      String schema = formatPlain(qualifiedName.getSchema());
      String tableName = qualifiedName.getName().get();
      try (ResultSet resultSet = metaData.getIndexInfo(catalog, schema, tableName, false, true)) {
        Map<DbName, JdbcIndexBuilder> indexBuilderMap = new HashMap<>();
        while (resultSet.next()) {
          DbName name = DbName.of(resultSet.getString(RS_INDEX_NAME));
          JdbcIndexBuilder indexBuilder = indexBuilderMap.computeIfAbsent(name, k -> new JdbcIndexBuilder(name));
          extractIndex(resultSet, indexBuilder, columns);
        }
        for (JdbcIndexBuilder indexBuilder : indexBuilderMap.values()) {
          builder.add(indexBuilder.buildIndex());
        }
        return builder.build();
      }
    } catch (SQLException e) {
      // TODO
      throw new IllegalStateException(e);
    }
  }

  private static void extractIndex(ResultSet resultSet, JdbcIndexBuilder indexBuilder,
      DbObjectContainer<DbColumn> columns) throws SQLException {

    int type = resultSet.getInt(RS_INDEX_TYPE);
    indexBuilder.setType(type);
    DbName columnName = DbName.of(resultSet.getString(RS_COLUMN_NAME));
    if (columnName != null) {
      DbColumn column = columns.get(columnName);
      if (column == null) {
        LOG.warn("Column {} not found - most probably an function based index.", columnName);
      } else {
        indexBuilder.add(column);
      }
    }
    String orderCode = resultSet.getString(RS_ASC_OR_DESC);
    SortOrder order = null;
    if ("A".equalsIgnoreCase(orderCode)) {
      order = SortOrder.ASC;
    } else if ("D".equalsIgnoreCase(orderCode)) {
      order = SortOrder.DESC;
    }
    indexBuilder.setSortOrder(order);
  }

}
