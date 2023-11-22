/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.repository;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.repository.EntityRepository;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * {@link EntityRepository} allowing {@link #findByQuery(SelectStatement)}
 *
 * @param <E> type of the managed {@link EntityBean}.
 */
public interface DbRepository<E extends EntityBean> extends EntityRepository<E> {

  /**
   * @param statement the {@link SelectStatement} to query the requested entities.
   * @return an {@link Iterable} with the matching {@link EntityBean entities}.
   */
  Iterable<E> findByQuery(SelectStatement<E> statement);

  /**
   * @param statement the {@link SelectStatement} to query the requested entity. Should produce a single result or no
   *        result.
   * @return the matching {@link EntityBean entity} or {@code null} if not found.
   * @throws RuntimeException if the given {@code query} produced multiple results (matched to more than one entity).
   */
  E findOneByQuery(SelectStatement<E> statement);

}
