/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.jdbc.connection.JdbcConnection;
import io.github.mmm.orm.jdbc.connection.JdbcConnectionPool;
import io.github.mmm.orm.jdbc.tx.JdbcTransactionExecutor;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.spi.session.DbSession;
import io.github.mmm.orm.tx.DbTransaction;

/**
 * Database session data for a single transaction.
 */
public class JdbcSession implements DbSession, DbTransaction {

  // private static final ScopedValue<JdbcSession> SESSION_HOLDER = ScopedValue.newInstance();

  private static final ThreadLocal<JdbcSession> SESSION = new ThreadLocal<>();

  private Map<String, JdbcEntitySession<?>> entitySessions;

  final Connection connection;

  final JdbcConnection jdbcConnection;

  final DbConnectionData connectionData;

  /**
   * The constructor.
   *
   * @param jdbcConnection the {@link JdbcConnection}.
   * @param connectionData the {@link DbConnectionData}.
   */
  private JdbcSession(JdbcConnection jdbcConnection, DbConnectionData connectionData) {

    super();
    this.jdbcConnection = jdbcConnection;
    this.connection = jdbcConnection.getConnection();
    this.connectionData = connectionData;
    this.entitySessions = new HashMap<>();
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public <E extends EntityBean> JdbcEntitySession<E> get(E entity) {

    String key = entity.getType().getQualifiedName();
    JdbcEntitySession session = this.entitySessions.computeIfAbsent(key, k -> new JdbcEntitySession<>(this.connection));
    return session;
  }

  /**
   * @return the raw JDBC {@link Connection}.
   */
  public Connection getConnection() {

    return this.connection;
  }

  /**
   * @return the {@link JdbcConnection}.
   */
  public JdbcConnection getJdbcConnection() {

    return this.jdbcConnection;
  }

  /**
   * @return the {@link DbConnectionData}.
   */
  public DbConnectionData getConnectionData() {

    return this.connectionData;
  }

  @Override
  public boolean isOpen() {

    return this.jdbcConnection.isOpen();
  }

  @Override
  public DbSource getSource() {

    return this.jdbcConnection.getSource();
  }

  /**
   * @return the {@link JdbcSession} for the current transaction.
   */
  public static JdbcSession get() {

    JdbcSession session = SESSION.get();
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
   * @see JdbcTransactionExecutor#doInTx(Callable)
   */
  public static <R> R doInTx(DbConnectionData connectionData, Callable<R> task) {

    JdbcSession parent = SESSION.get(); // obsolete with ScopedValue
    Connection connection = null;
    JdbcConnection jdbcConnection = null;
    JdbcConnectionPool connectionPool = (JdbcConnectionPool) connectionData.getPool();
    try {
      jdbcConnection = connectionPool.acquire();
      JdbcSession session = new JdbcSession(jdbcConnection, connectionData);
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
      SESSION.set(parent); // obsolete with ScopedValue
      connectionPool.release(jdbcConnection);
    }
  }

}
