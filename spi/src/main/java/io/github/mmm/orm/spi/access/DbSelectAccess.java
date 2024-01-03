/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.access;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.orm.statement.NonUniqueResultException;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * Interface providing support for {@link #select(SelectStatement)} and {@link #selectOne(SelectStatement)}.
 */
public interface DbSelectAccess {

  /**
   * @param <R> type of the result objects to select.
   * @param statement the {@link SelectStatement} to query the requested objects.
   * @return an {@link Iterable} with the matching objects.
   */
  <R> Iterable<R> select(SelectStatement<R> statement);

  /**
   * @param <R> type of the result object to select.
   * @param statement the {@link SelectStatement} to query the requested object. Should produce a single result or no
   *        result.
   * @return the matching object or {@code null} if not found.
   * @throws NonUniqueResultException if the given {@link SelectStatement} produced more than one result.
   */
  <R> R selectOne(SelectStatement<R> statement);

  /**
   * @param <E> type of the {@link EntityBean} to select.
   * @param id the {@link EntityBean#Id() ID} of the {@link EntityBean} to select.
   * @param prototype the (empty) {@link EntityBean} instance to act as template.
   * @return the {@link EntityBean} with the given {@link Id} or {@code null} if no such {@link EntityBean} exists.
   */
  <E extends EntityBean> E selectById(Id<E> id, E prototype);

}
