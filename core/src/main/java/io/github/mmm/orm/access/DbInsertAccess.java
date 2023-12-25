/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.access;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.insert.InsertStatement;

/**
 * Interface providing support for {@link #insert(InsertStatement)}.
 */
public interface DbInsertAccess {

  /**
   * @param statement the {@link InsertStatement} to execute.
   */
  void insert(InsertStatement<?> statement);

  /**
   * @param entity the {@link EntityBean} to insert.
   */
  void insert(EntityBean entity);

  /**
   * @param entities the {@link EntityBean}s to insert as batch operation.
   */
  default void insertAll(EntityBean... entities) {

    for (EntityBean entity : entities) {
      insert(entity);
    }
  }

}
