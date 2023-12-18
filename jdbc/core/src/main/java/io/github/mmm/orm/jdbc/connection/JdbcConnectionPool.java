/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import io.github.mmm.orm.connection.DbConnectionPool;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.jdbc.access.session.JdbcSession;
import io.github.mmm.orm.source.DbSource;

/**
 * Implementation of {@link DbConnectionPool} for {@link JdbcSession} based on {@link DataSource}.
 */
public class JdbcConnectionPool implements DbConnectionPool<JdbcConnection> {

  private final DataSource dataSource;

  private final DbSource source;

  private final DbDialect dialect;

  /**
   * The constructor.
   *
   * @param dataSource the {@link DataSource} pre-configured as connection pool.
   * @param source the {@link DbSource}.
   * @param dialect the {@link DbDialect}.
   */
  public JdbcConnectionPool(DataSource dataSource, DbSource source, DbDialect dialect) {

    super();
    this.dataSource = dataSource;
    this.source = source;
    this.dialect = dialect;
  }

  @Override
  public JdbcConnection acquire() {

    try {
      Connection connection = this.dataSource.getConnection();
      return new JdbcConnection(connection, this.source, this.dialect);
    } catch (SQLException e) {
      throw new IllegalStateException("Unable to acquire connection!", e);
    }
  }

  @Override
  public void release(JdbcConnection connection) {

    try {
      connection.connection.close();
      connection.close();
    } catch (SQLException e) {
      throw new IllegalStateException("Unable to release connection!", e);
    }
  }

}
