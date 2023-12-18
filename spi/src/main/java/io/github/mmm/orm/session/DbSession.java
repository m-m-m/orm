/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.session;

import io.github.mmm.entity.bean.EntityBean;

/**
 * Database session data for a single transaction.
 *
 * @param <ES> type of the managed {@link DbEntitySession}s.
 */
public interface DbSession<ES extends DbEntitySession<?>> {

  /**
   * @param entity the {@link EntityBean}.
   * @return the {@link DbEntitySession} responsible for the given {@link EntityBean} (by {@link EntityBean#getType()
   *         type}).
   */
  ES get(EntityBean entity);

}
