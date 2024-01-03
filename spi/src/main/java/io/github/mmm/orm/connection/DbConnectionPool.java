/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.connection;

import java.io.Closeable;

/**
 * This is the interface for a pool of {@link DbConnection}s. It is an internal API that allows to {@link #acquire()} a
 * {@link DbConnection} from the pool. After it has been used, it has to be {@link #release(DbConnection) released}. To
 * avoid resource leaks this needs to be handled with care so you should always use a {@code try} block and
 * {@link #release(DbConnection) release} in {@code finally}.
 *
 * @param <C> type of the {@link DbConnection}.
 */
public interface DbConnectionPool<C extends DbConnection> extends Closeable {

  /**
   * @return the acquired {@link DbConnection} that has been borrowed from this pool. The caller has to guarantee to
   *         give it back after usage via {@link #release(DbConnection)}.
   */
  C acquire();

  /**
   * @param connection the {@link DbConnection} to return back to this pool so it can be reused by others again via
   *        {@link #acquire()}.
   */
  void release(C connection);

  @Override
  void close();

}
