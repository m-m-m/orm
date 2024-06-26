/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.repository.operation;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.statement.NonUniqueResultException;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * Extends {@link EntityFindOperations} with operations for {@link SelectStatement}s.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public interface DbFindOperations<E extends EntityBean> extends EntityFindOperations<E> {

  /**
   * @param statement the {@link SelectStatement} to query the requested entities.
   * @return an {@link Iterable} with the matching {@link EntityBean entities}.
   */
  Iterable<E> findByQuery(SelectStatement<E> statement);

  /**
   * @param statement the {@link SelectStatement} to query the requested entity. Should produce a single result or no
   *        result.
   * @return the matching {@link EntityBean entity} or {@code null} if not found.
   * @throws NonUniqueResultException if the given {@code statement} produced more than one result.
   */
  E findOneByQuery(SelectStatement<E> statement);

}
