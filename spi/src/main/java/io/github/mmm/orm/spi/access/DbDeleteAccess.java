/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.access;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.orm.statement.delete.DeleteStatement;

/**
 * Interface providing support for {@link #delete(DeleteStatement)}.
 */
public interface DbDeleteAccess {

  /**
   * @param statement the {@link DeleteStatement} to execute.
   * @return the number of records that have been deleted.
   */
  long delete(DeleteStatement<?> statement);

  /**
   * @param <E> type of the {@link EntityBean} to delete.
   * @param id the {@link Id} of the {@link EntityBean} to delete.
   * @param prototype the (empty) {@link EntityBean} instance to act as template.
   * @return {@code true} if the {@link EntityBean} with the given {@link Id} has been deleted, {@code false} otherwise
   *         (no such entity exists).
   */
  <E extends EntityBean> boolean deleteById(Id<E> id, E prototype);

  /**
   * @param <E> type of the {@link EntityBean}s to delete.
   * @param ids the {@link Iterable} with the {@link Id}s to delete.
   * @param prototype the (empty) {@link EntityBean} instance to act as template.
   * @return the actual number of {@link EntityBean}s that have been deleted.
   */
  <E extends EntityBean> int deleteAllById(Iterable<Id<E>> ids, E prototype);

}
