/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.r2dbc.connection;

import io.github.mmm.orm.connection.DbConnection;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.metadata.DbMetaData;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.tx.DbTransaction;
import io.r2dbc.spi.Connection;
import reactor.core.publisher.Mono;

/**
 * Database session data for a single transaction.
 */
public class R2dbcConnection implements DbConnection, DbTransaction {

  final Mono<Connection> connection;

  final DbSource source;

  private final DbDialect dialect;

  private boolean open;

  /**
   * The constructor.
   *
   * @param connection the {@link Mono} of the R2DBC {@link Connection}.
   * @param source the {@link #getSource() source}-
   * @param dialect the {@link DbDialect}.
   */
  public R2dbcConnection(Mono<Connection> connection, DbSource source, DbDialect dialect) {

    super();
    this.connection = connection;
    this.source = source;
    this.dialect = dialect;
    this.open = true;
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
   * @return the {@link Mono} of the R2DBC {@link Connection}.
   */
  public Mono<Connection> getConnection() {

    return this.connection;
  }

  @Override
  public DbMetaData getMetaData() {

    // TODO https://github.com/r2dbc/r2dbc-spi/issues/203
    return null;
  }

  void close() {

    this.open = false;
  }

}
