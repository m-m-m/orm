/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.connection;

import io.github.mmm.orm.connection.impl.DbConnectionPoolProviderManagerImpl;

/**
 * Internal interface to get access to the registered {@link DbConnectionPoolProvider}s.
 */
public interface DbConnectionPoolProviderManager extends Iterable<DbConnectionPoolProvider<?>> {

  /**
   * @param <C> type of the {@link DbConnection}.
   * @param id the {@link DbConnectionPoolProvider#getId() identifier} of the {@link DbConnectionPoolProvider}.
   * @return the requested {@link DbConnectionPoolProvider}.
   * @see io.github.mmm.orm.source.DbSource#KEY_POOL
   */
  <C extends DbConnection> DbConnectionPoolProvider<C> get(String id);

  /**
   * @return the instance of {@link DbConnectionPoolProviderManagerImpl}.
   */
  static DbConnectionPoolProviderManager get() {

    return DbConnectionPoolProviderManagerImpl.INSTANCE;
  }

}
