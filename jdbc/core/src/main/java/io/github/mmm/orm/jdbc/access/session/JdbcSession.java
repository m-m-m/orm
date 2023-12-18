/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.access.session;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.jdbc.connection.JdbcConnection;
import io.github.mmm.orm.session.DbSession;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.tx.DbTransaction;

/**
 * Database session data for a single transaction.
 */
public class JdbcSession implements DbSession<JdbcEntitySession<?>>, DbTransaction {

  private Map<String, JdbcEntitySession<?>> entitySessions;

  final Connection connection;

  final JdbcConnection jdbcConnection;

  /**
   * The constructor.
   *
   * @param jdbcConnection the {@link JdbcConnection}.
   */
  public JdbcSession(JdbcConnection jdbcConnection) {

    super();
    this.jdbcConnection = jdbcConnection;
    this.connection = jdbcConnection.getConnection();
    this.entitySessions = new HashMap<>();
  }

  @Override
  public JdbcEntitySession<?> get(EntityBean entity) {

    String key = entity.getType().getQualifiedName();
    return this.entitySessions.computeIfAbsent(key, k -> new JdbcEntitySession<>(this.connection));
  }

  /**
   * @return the raw JDBC {@link Connection}.
   */
  public Connection getConnection() {

    return this.connection;
  }

  @Override
  public boolean isOpen() {

    return this.jdbcConnection.isOpen();
  }

  @Override
  public DbSource getSource() {

    return this.jdbcConnection.getSource();
  }

}
