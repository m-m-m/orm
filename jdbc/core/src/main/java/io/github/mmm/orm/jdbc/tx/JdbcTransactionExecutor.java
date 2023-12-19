/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.tx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.jdbc.access.session.JdbcSession;
import io.github.mmm.orm.jdbc.connection.JdbcConnection;
import io.github.mmm.orm.jdbc.connection.JdbcConnectionPool;
import io.github.mmm.orm.tx.DbTransaction;
import io.github.mmm.orm.tx.DbTransactionExecutor;

/**
 * Implementation of {@link DbTransactionExecutor} for JDBC.
 */
public class JdbcTransactionExecutor implements DbTransactionExecutor {

  // private final ScopedValue<JdbcSession> sessionHolder = ScopedValue.newInstance();

  private final ThreadLocal<JdbcSession> sessionHolder = new ThreadLocal<>();

  private final DbConnectionData connectionData;

  private final JdbcConnectionPool connectionPool;

  /**
   * The constructor.
   *
   * @param connectionData the {@link DbConnectionData}.
   */
  public JdbcTransactionExecutor(DbConnectionData connectionData) {

    super();
    this.connectionData = connectionData;
    this.connectionPool = (JdbcConnectionPool) connectionData.getPool();
  }

  @Override
  public <R> R doInTx(Callable<R> task) {

    Connection connection = null;
    JdbcConnection jdbcConnection = null;
    try {
      jdbcConnection = this.connectionPool.acquire();
      // R result = ScopedValue.where(SESSION, session).call(task);
      JdbcSession session = new JdbcSession(jdbcConnection);
      this.sessionHolder.set(session);
      connection = session.getConnection();
      R result = task.call();
      connection.commit();
      return result;
    } catch (Throwable t) {
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException e) {
          t.addSuppressed(e);
        }
      }
      if (t instanceof RuntimeException re) {
        throw re;
      } else if (t instanceof Error e) {
        throw e;
      }
      throw new IllegalStateException(t);
    } finally {
      this.sessionHolder.set(null);
      this.connectionPool.release(jdbcConnection);
    }
  }

  @Override
  public DbTransaction getTransaction() {

    return this.sessionHolder.get().getJdbcConnection();
  }

  /**
   * @return the current {@link JdbcSession} for the running transaction.
   */
  public JdbcSession getSession() {

    return this.sessionHolder.get();
  }

}
