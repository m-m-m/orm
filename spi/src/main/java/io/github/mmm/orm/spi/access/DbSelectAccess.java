/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.access;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * Interface providing support for {@link #select(SelectStatement)} and {@link #selectOne(SelectStatement)}.
 */
public interface DbSelectAccess {

  /**
   * @param <E> type of the {@link EntityBean} to select.
   * @param statement the {@link SelectStatement} to query the requested entities.
   * @return an {@link Iterable} with the matching {@link EntityBean entities}.
   */
  <E extends EntityBean> Iterable<E> select(SelectStatement<E> statement);

  /**
   * @param <E> type of the {@link EntityBean} to select.
   * @param statement the {@link SelectStatement} to query the requested entity. Should produce a single result or no
   *        result.
   * @return the matching {@link EntityBean entity} or {@code null} if not found.
   * @throws RuntimeException if the given {@code query} produced multiple results (matched to more than one entity).
   */
  <E extends EntityBean> E selectOne(SelectStatement<E> statement);

  /**
   * @param <E> type of the {@link EntityBean} to select.
   * @param id the {@link EntityBean#Id() ID} of the {@link EntityBean} to select.
   * @param prototype the (empty) {@link EntityBean} instance to act as template.
   * @return the {@link EntityBean} with the given {@link Id} or {@code null} if no such {@link EntityBean} exists.
   */
  <E extends EntityBean> E selectById(Id<E> id, E prototype);

}
