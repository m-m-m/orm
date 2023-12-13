/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.access.session;

import io.github.mmm.orm.config.DbSource;

/**
 * Interface to get the {@link DbConnectionPool} for a given {@link DbSource}.
 */
public interface DbConnectionPoolProvider {

  /**
   * @param <C> type of the underlying {@link DbConnection}.
   * @param source the {@link DbSource}.
   * @return the {@link DbConnectionPool} responsible for the given {@link DbSource}.
   */
  <C extends DbConnection> DbConnectionPool<C> get(DbSource source);

}
