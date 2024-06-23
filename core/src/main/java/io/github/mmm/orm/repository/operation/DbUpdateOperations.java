/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.repository.operation;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.select.SelectStatement;
import io.github.mmm.orm.statement.update.UpdateStatement;

/**
 * Extends {@link EntityFindOperations} with operations for {@link SelectStatement}s.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public interface DbUpdateOperations<E extends EntityBean> extends EntityFindOperations<E> {

  /**
   * @param statement the {@link UpdateStatement} to execute.
   * @return the number of {@link EntityBean entities} that have been updated.
   */
  long update(UpdateStatement<E> statement);

}
