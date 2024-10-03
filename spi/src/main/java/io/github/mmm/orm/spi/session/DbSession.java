/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.session;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.connection.DbConnectionData;

/**
 * Database session data for a single transaction.
 *
 * @since 1.0.0
 */
public interface DbSession {

  /**
   * @return the {@link DbConnectionData}.
   */
  DbConnectionData getConnectionData();

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param entity the {@link EntityBean}.
   * @return the {@link DbEntitySession} responsible for the given {@link EntityBean} (by {@link EntityBean#getType()
   *         type}).
   */
  <E extends EntityBean> DbEntitySession<E> get(E entity);

}
