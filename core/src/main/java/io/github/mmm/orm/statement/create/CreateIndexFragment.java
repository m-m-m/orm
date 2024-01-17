/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.statement.alter.AlterTableClause;
import io.github.mmm.orm.statement.alter.AlterTableOperations;
import io.github.mmm.property.ReadableProperty;

/**
 * Interface for a fragment or clause to add {@link DbColumnSpec #column(DbColumnName) columns} and #constraints.
 *
 * @param <E> type of the {@link AlterTableClause#getEntity() entity}.
 */
public interface CreateIndexFragment<E extends EntityBean> {

  /**
   * @param property the {@link ReadableProperty property} to to create an index on.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default CreateIndexColumns<E> column(ReadableProperty<?> property) {

    return column(new DbColumnSpec(property));
  }

  /**
   * @param column the {@link DbColumnSpec column} to to create an index on.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  CreateIndexColumns<E> column(DbColumnSpec column);

  /**
   * @param properties the {@link ReadableProperty}s of the columns to create an index on.
   * @return the {@link CreateIndexColumns} for fluent API calls.
   */
  CreateIndexColumns<E> columns(ReadableProperty<?>... properties);

  /**
   * @param columns the {@link DbColumnSpec}s of the columns to create an index on.
   * @return the {@link CreateIndexColumns} for fluent API calls.
   */
  CreateIndexColumns<E> columns(DbColumnSpec... columns);

}
