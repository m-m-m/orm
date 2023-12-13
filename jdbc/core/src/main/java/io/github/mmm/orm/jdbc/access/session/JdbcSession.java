/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.access.session;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.access.session.DbConnection;
import io.github.mmm.orm.access.session.DbSession;
import io.github.mmm.orm.config.DbSource;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.jdbc.impl.metadata.JdbcMetaData;
import io.github.mmm.orm.tx.DbTransaction;

/**
 * Database session data for a single transaction.
 */
public class JdbcSession implements DbSession<JdbcEntitySession<?>>, DbConnection, DbTransaction {

  private Map<String, JdbcEntitySession<?>> entitySessions;

  final Connection connection;

  final DbSource source;

  private final DbDialect dialect;

  private JdbcMetaData metaData;

  private boolean open;

  /**
   * The constructor.
   *
   * @param connection the JDBC {@link Connection}.
   * @param source the {@link #getSource() source}-
   * @param dialect the {@link DbDialect}.
   */
  public JdbcSession(Connection connection, DbSource source, DbDialect dialect) {

    super();
    this.entitySessions = new HashMap<>();
    this.connection = connection;
    this.source = source;
    this.dialect = dialect;
    this.open = true;
  }

  @Override
  public JdbcEntitySession<?> get(EntityBean entity) {

    String key = entity.getType().getQualifiedName();
    return this.entitySessions.computeIfAbsent(key, k -> new JdbcEntitySession<>(this.connection));
  }

  @Override
  public DbSource getSource() {

    return this.source;
  }

  @Override
  public boolean isOpen() {

    return this.open;
  }

  /**
   * @return the raw JDBC {@link Connection}.
   */
  public Connection getConnection() {

    return this.connection;
  }

  @Override
  public JdbcMetaData getMetaData() {

    if (this.metaData == null) {
      this.metaData = new JdbcMetaData(this.connection, this.dialect);
    }
    return this.metaData;
  }

  void close() {

    this.open = false;
    this.entitySessions = null; // help GC and prevent further usage
  }

}
