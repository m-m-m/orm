/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Provides ORM support via R2DBC.
 *
 * @provides io.github.mmm.orm.tx.DbTransactionExecutorProvider
 */
module io.github.mmm.orm.r2dbc {

  requires transitive io.github.mmm.orm.spi;

  requires transitive org.reactivestreams;

  requires reactor.core;

  requires r2dbc.spi;

  requires r2dbc.pool;

  provides io.github.mmm.orm.tx.DbTransactionExecutorProvider
      with io.github.mmm.orm.r2dbc.tx.R2dbcTransactionExecutorProvider;

  exports io.github.mmm.orm.r2dbc.connection;

}
