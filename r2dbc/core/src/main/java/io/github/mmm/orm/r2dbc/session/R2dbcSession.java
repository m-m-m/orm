/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.r2dbc.session;

import java.lang.ScopedValue.CallableOp;
import java.util.concurrent.Callable;

import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.r2dbc.connection.R2dbcConnection;
import io.github.mmm.orm.r2dbc.connection.R2dbcConnectionPool;
import io.github.mmm.orm.r2dbc.tx.R2dbcTransactionExecutor;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.spi.session.AbstractDbSession;
import io.github.mmm.orm.tx.DbTransaction;
import io.r2dbc.spi.Connection;
import reactor.core.publisher.Mono;

/**
 * Database session data for a single transaction.
 */
public class R2dbcSession extends AbstractDbSession implements DbTransaction {

  private static final ScopedValue<R2dbcSession> SESSION = ScopedValue.newInstance();

  final R2dbcConnection r2dbcConnection;

  /**
   * The constructor.
   *
   * @param jdbcConnection the {@link R2dbcConnection}.
   * @param connectionData the {@link DbConnectionData}.
   */
  private R2dbcSession(R2dbcConnection jdbcConnection, DbConnectionData connectionData) {

    super(connectionData);
    this.r2dbcConnection = jdbcConnection;
  }

  /**
   * @return the {@link R2dbcConnection}.
   */
  public R2dbcConnection getR2dbcConnection() {

    return this.r2dbcConnection;
  }

  /**
   * @return the {@link DbConnectionData}.
   */
  @Override
  public DbConnectionData getConnectionData() {

    return this.connectionData;
  }

  @Override
  public boolean isOpen() {

    return this.r2dbcConnection.isOpen();
  }

  @Override
  public DbSource getSource() {

    return this.r2dbcConnection.getSource();
  }

  /**
   * @return the {@link R2dbcSession} for the current transaction.
   */
  public static R2dbcSession get() {

    R2dbcSession session = SESSION.get();
    if (session == null) {
      throw new IllegalStateException("No session found - missing transactional context!");
    }
    return session;
  }

  /**
   * @param <R> type of the returned result.
   * @param connectionData the {@link DbConnectionData}.
   * @param task the {@link Callable} task to run in transaction.
   * @return the result of {@link Callable#call()}.
   * @see R2dbcTransactionExecutor#doInTx(Callable)
   */
  public static <R> R doInTx(DbConnectionData connectionData, CallableOp<R, Exception> task) {

    R2dbcConnection jdbcConnection = null;
    R2dbcConnectionPool connectionPool = (R2dbcConnectionPool) connectionData.getPool();
    try {
      jdbcConnection = connectionPool.acquire();
      R2dbcSession session = new R2dbcSession(jdbcConnection, connectionData);
      Mono<Connection> connection = session.getR2dbcConnection().getConnection();
      R result = ScopedValue.where(SESSION, session).call(task);
      return result;
    } catch (Throwable t) {
      // if (connection != null) {
      // try {
      // connection.rollback();
      // } catch (SQLException e) {
      // t.addSuppressed(e);
      // }
      // }
      if (t instanceof RuntimeException re) {
        throw re;
      } else if (t instanceof Error e) {
        throw e;
      }
      throw new IllegalStateException(t);
    } finally {
      connectionPool.release(jdbcConnection);
    }
  }

}
