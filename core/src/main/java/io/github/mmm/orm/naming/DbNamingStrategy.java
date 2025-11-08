/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.naming;

import java.util.List;

import io.github.mmm.base.text.CaseSyntax;
import io.github.mmm.bean.BeanType;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.statement.AbstractEntityClause;
import io.github.mmm.orm.statement.create.CreateIndexStatement;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.value.PropertyPath;
import io.github.mmm.value.ReadablePath;
import io.github.mmm.value.ReadablePath.PathBuilder;
import io.github.mmm.value.converter.TypeMapper;

/**
 * Interface to define the naming strategy to map {@link EntityBean}s to a database.
 *
 * @see #getColumnName(String)
 * @see #getTableName(EntityBean)
 * @since 1.0.0
 */
public interface DbNamingStrategy {

  /**
   * @return the maximum length for names of DDL elements (tables, columns, constraints, indexes, etc.) supported by the
   *         underlying database. To avoid trouble you should name your entities and properties short but precise.
   *         However, if you somehow exceed the limit, your names will be truncated automatically.
   */
  default int getMaximumNameLength() {

    return 128;
  }

  /**
   * @param name the suggested name of a DDL element.
   * @return the given {@code name} clipped to ensure {@link #getMaximumNameLength()} is not exceeded.
   */
  default String clipName(String name) {

    int max = getMaximumNameLength();
    if (name.length() > max) {
      return name.substring(0, max);
    }
    return name;
  }

  /**
   * @param property the {@link ReadableProperty} to derive the column name from.
   * @return the column name for the given {@link ReadableProperty property}.
   */
  default String getColumnName(ReadableProperty<?> property) {

    String columnName = property.getMetadata().getMetaInfo().get(EntityBean.META_KEY_COLUMN);
    return getColumnName(columnName, property);
  }

  /**
   * @param property the {@link PropertyPath} to derive the column name from.
   * @return the column name for the given {@link PropertyPath property}.
   */
  default String getColumnName(PropertyPath<?> property) {

    if (property instanceof ReadableProperty<?> rp) {
      return getColumnName(rp);
    } else {
      return getColumnName(null, property);
    }
  }

  /**
   * @param columnName the raw column name that has already be resolved.
   * @param property the {@link ReadableProperty} to derive the column name from.
   * @return the column name for the given {@link ReadableProperty property}.
   */
  default String getColumnName(String columnName, PropertyPath<?> property) {

    if (columnName == null) {
      columnName = getColumnName(property.getName());
    }
    ReadablePath parent = property.parentPath();
    if (parent != null) {
      PathBuilder builder = PathBuilder.of();
      parent.path(builder);
      builder.add(columnName);
      return builder.toString();
    }
    return clipName(columnName);
  }

  /**
   * @param property the {@link PropertyPath} to derive the column name from.
   * @param typeMapper the explicit {@link TypeMapper}.
   * @return the column name for the given {@link ReadableProperty property}.
   */
  default String getColumnName(PropertyPath<?> property, TypeMapper<?, ?> typeMapper) {

    return getColumnName(getColumnName(property), typeMapper);
  }

  /**
   * @param rawColumnName the raw column name to map.
   * @param typeMapper the explicit {@link TypeMapper}.
   * @return the column name for the given {@link ReadableProperty property}.
   */
  default String getColumnName(String rawColumnName, TypeMapper<?, ?> typeMapper) {

    if (typeMapper == null) {
      return rawColumnName;
    }
    String suffix = typeMapper.getSuffix();
    if ((suffix != null) && !suffix.isEmpty()) {
      suffix = getColumnName(suffix);
    }
    String columnName = typeMapper.getNameMode().format(rawColumnName, suffix);
    return clipName(columnName);
  }

  /**
   * @param rawColumnName the raw column name to map. E.g. {@link WritableProperty#getName() property name}.
   * @return the final column name.
   */
  default String getColumnName(String rawColumnName) {

    return clipName(rawColumnName);
  }

  /**
   * @param entity the {@link EntityBean} to map to a database table.
   * @return the physical table name.
   */
  default String getTableName(EntityBean entity) {

    BeanType type = entity.getType();
    String tableName = type.getMetaInfo().get(EntityBean.META_KEY_TABLE);
    if (tableName == null) {
      tableName = getTableName(type.getSimpleName());
    }
    return clipName(tableName);
  }

  /**
   * @param entityClause the {@link AbstractEntityClause}.
   * @return the physical table name.
   */
  default String getTableName(AbstractEntityClause<?, ?, ?> entityClause) {

    EntityBean entity = entityClause.getEntity();
    if (entity != null) {
      return getTableName(entity);
    }
    return entityClause.getEntityName();
  }

  /**
   * @param rawTableName the raw table name to map. May be the {@link io.github.mmm.bean.BeanType#getStableName() stable
   *        entity name}.
   * @return the database table name.
   */
  default String getTableName(String rawTableName) {

    return clipName(rawTableName);
  }

  /**
   * @param createIndex the {@link CreateIndexStatement}.
   * @return the static prefix for an {@link #getIndexName(CreateIndexStatement) index name}.
   */
  default String getIndexNamePrefix(CreateIndexStatement<?> createIndex) {

    if (createIndex.getCreateIndex().isUnique()) {
      return "UX";
    } else {
      return "IX";
    }
  }

  /**
   * @return the static infix for an {@link #getIndexName(CreateIndexStatement) index name} used to separate the
   *         columns.
   */
  default String getIndexNameInfix() {

    return "_";
  }

  /**
   * @param createIndex the {@link CreateIndexStatement}.
   * @return the auto-generated index name.
   */
  default String getIndexName(CreateIndexStatement<?> createIndex) {

    EntityBean entity = createIndex.getOn().getEntity();
    List<DbColumnSpec> columns = createIndex.getColumn().getColumns();
    String tableName = getTableName(entity);
    int capacity = 3 + tableName.length() + (columns.size() * 5);
    int max = getMaximumNameLength();
    if (capacity > max) {
      capacity = max;
    }
    StringBuilder sb = new StringBuilder(capacity);
    String infix = getIndexNameInfix();
    sb.append(getIndexNamePrefix(createIndex));
    sb.append(infix);
    sb.append(tableName);
    for (DbColumnSpec column : columns) {
      column.getName();
      String columnName = getColumnName(column.getProperty());
      sb.append(infix);
      sb.append(columnName);
      if (sb.length() > max) {
        break;
      }
    }
    String indexName = sb.toString();
    return clipName(indexName);
  }

  /**
   * @return the default {@link DbNamingStrategy}.
   */
  static DbNamingStrategy of() {

    return DbNamingStrategyDefault.INSTANCE;
  }

  /**
   * @param caseSyntax the {@link CaseSyntax} to use.
   * @return the {@link DbNamingStrategy} converting to the given {@link CaseSyntax}.
   */
  static DbNamingStrategy of(CaseSyntax caseSyntax) {

    return DbNamingStrategyCaseSyntax.of(caseSyntax);
  }

  /**
   * @return the {@link DbNamingStrategy} for legacy
   *         <a href="https://en.wikipedia.org/wiki/Relational_database">RDBMS</a>.
   */
  static DbNamingStrategy ofRdbms() {

    return of(CaseSyntax.UPPER_SNAKE_CASE);
  }
}
