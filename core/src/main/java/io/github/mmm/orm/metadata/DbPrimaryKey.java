/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

/**
 * Meta-information about a primary key.
 *
 * @see DbTable#getPrimaryKey()
 * @since 1.0.0
 */
public interface DbPrimaryKey extends DbObject {

  /**
   * @return the {@link DbName} of the primary key constraint. <b>ATTENTION:</b> Will be {@code null} if the name was
   *         not explicitly defined in the DDL.
   */
  @Override
  DbName getName();

  /**
   * @return the {@link DbObjectContainer container} with the {@link DbColumn columns} this primary key is composed of.
   *         Typically a primary key is just composed out of a single column.
   */
  DbObjectContainer<DbColumn> getColumns();

}
