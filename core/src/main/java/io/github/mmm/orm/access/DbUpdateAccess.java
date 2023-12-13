/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.access;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.update.UpdateStatement;

/**
 * Interface providing support for {@link #update(UpdateStatement)}.
 */
public interface DbUpdateAccess {

  /**
   * @param statement the {@link UpdateStatement} to execute.
   * @return the number of records that have been deleted.
   */
  long update(UpdateStatement<?> statement);

  /**
   * @param entity the {@link EntityBean} to update.
   */
  void update(EntityBean entity);

  /**
   * @param entities the {@link EntityBean}s to update as batch operation.
   */
  void updateAll(EntityBean... entities);

}
