/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.access;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.create.CreateIndexStatement;
import io.github.mmm.orm.statement.create.CreateTable;
import io.github.mmm.orm.statement.create.CreateTableStatement;

/**
 * Interface providing support for {@link #createTable(CreateTableStatement)} and
 * {@link #createIndex(CreateIndexStatement)}.
 */
public interface DbCreateAccess {

  /**
   * @param statement the {@link CreateTableStatement} to execute.
   */
  void createTable(CreateTableStatement<?> statement);

  /**
   * @param entity the {@link EntityBean} prototype to create the database table for.
   */
  default void createTable(EntityBean entity) {

    CreateTableStatement<EntityBean> statement = new CreateTable<>(entity).columns().get();
    createTable(statement);
  }

  /**
   * Synchronized the database table for the given {@link EntityBean}. If the table does not exist, it will be
   * {@link #createTable(EntityBean) created}, otherwise missing columns will be added. If all is up-to-date, nothing
   * needs to be done.
   *
   * @param entity the {@link EntityBean} prototype to sync the database table for.
   */
  void syncTable(EntityBean entity);

  /**
   * @param statement the {@link CreateIndexStatement} to execute.
   */
  void createIndex(CreateIndexStatement<?> statement);

}
