/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.naming;

import io.github.mmm.base.text.CaseSyntax;
import io.github.mmm.bean.BeanType;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.AbstractEntityClause;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.value.ReadablePath;
import io.github.mmm.value.ReadablePath.PathBuilder;

/**
 * Interface to define the naming strategy to map {@link EntityBean}s to a database.
 *
 * @see #getColumnName(String)
 * @see #getTableName(EntityBean)
 * @since 1.0.0
 */
public interface DbNamingStrategy {

  /**
   * @param property the {@link ReadableProperty} to derive the column name from.
   * @return the column name for the given {@link ReadableProperty property}.
   */
  default String getColumnName(ReadableProperty<?> property) {

    String columnName = property.getMetadata().getMetaInfo().get(EntityBean.META_KEY_COLUMN);
    if (columnName == null) {
      columnName = getColumnName(property.getName());
    }
    ReadablePath parent = property.parentPath();
    ;
    if (parent != null) {
      PathBuilder builder = PathBuilder.of();
      parent.path(builder);
      builder.add(columnName);
      return builder.toString();
    }
    return columnName;
  }

  /**
   * @param rawColumnName the raw column name to map. May be the {@link WritableProperty#getName() property name} or
   *        {@link io.github.mmm.value.converter.TypeMapper#mapName(String) remapped and decomposed} from it.
   * @return the final column name.
   */
  default String getColumnName(String rawColumnName) {

    return rawColumnName;
  }

  /**
   * @param bean the {@link EntityBean} to map to a database table.
   * @return the physical table name.
   */
  default String getTableName(EntityBean bean) {

    BeanType type = bean.getType();
    String tableName = type.getMetaInfo().get(EntityBean.META_KEY_TABLE);
    if (tableName == null) {
      tableName = getTableName(type.getSimpleName());
    }
    return tableName;
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

    return rawTableName;
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
