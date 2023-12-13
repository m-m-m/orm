/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.tx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import io.github.mmm.orm.access.session.DbConnectionPool;
import io.github.mmm.orm.jdbc.access.session.JdbcConnectionPool;
import io.github.mmm.orm.jdbc.access.session.JdbcSession;
import io.github.mmm.orm.tx.DbTransaction;
import io.github.mmm.orm.tx.DbTransactionExecutor;

/**
 *
 */
public class JdbcTransactionExecutor implements DbTransactionExecutor {

  // static final ScopedValue<JdbcSession> SESSION = ScopedValue.newInstance();

  private static final ThreadLocal<JdbcSession> SESSION = new ThreadLocal<>();

  private final JdbcConnectionPool connectionPool;

  /**
   * The constructor.
   *
   * @param connectionPool the {@link DbConnectionPool}.
   */
  public JdbcTransactionExecutor(JdbcConnectionPool connectionPool) {

    super();
    this.connectionPool = connectionPool;
  }

  @Override
  public <R> R doInTx(Callable<R> task) {

    JdbcSession session = null;
    Connection connection = null;
    try {
      session = this.connectionPool.acquire();
      // R result = ScopedValue.where(SESSION, session).call(task);
      SESSION.set(session);
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
      SESSION.set(null);
      this.connectionPool.release(session);
    }
  }

  @Override
  public DbTransaction getTransaction() {

    return SESSION.get();
  }

  /**
   * @return the current {@link JdbcSession} for the running transaction.
   */
  public static JdbcSession getSession() {

    return SESSION.get();
  }

}
