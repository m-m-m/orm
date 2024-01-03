/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.r2dbc.connection;

import io.github.mmm.orm.connection.DbConnectionPool;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.source.DbSource;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.spi.ConnectionFactory;

/**
 * Implementation of {@link DbConnectionPool} for {@link R2dbcConnection} based on {@link ConnectionFactory}.
 */
public class R2dbcConnectionPool implements DbConnectionPool<R2dbcConnection> {

  private ConnectionPool pool;

  private final DbSource source;

  private final DbDialect dialect;

  /**
   * The constructor.
   *
   * @param pool the {@link ConnectionPool}.
   * @param source the {@link DbSource}.
   * @param dialect the {@link DbDialect}.
   */
  public R2dbcConnectionPool(ConnectionPool pool, DbSource source, DbDialect dialect) {

    super();
    this.pool = pool;
    this.source = source;
    this.dialect = dialect;
  }

  @Override
  public R2dbcConnection acquire() {

    return new R2dbcConnection(this.pool.create(), this.source, this.dialect);
  }

  @Override
  public void release(R2dbcConnection connection) {

    // Nothing to be done here - needs to be handled by subscriber...
  }

  @Override
  public void close() {

    if (this.pool != null) {
      this.pool.dispose();
      this.pool = null;
    }
  }

}
