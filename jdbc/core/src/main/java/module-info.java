/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides ORM support via JDBC.
 *
 * @provides io.github.mmm.orm.tx.DbTransactionExecutorProvider
 * @provides io.github.mmm.orm.spi.access.DbAccessProvider
 */
module io.github.mmm.orm.jdbc {

  requires transitive io.github.mmm.orm.spi;

  requires transitive java.sql;

  provides io.github.mmm.orm.tx.DbTransactionExecutorProvider //
      with io.github.mmm.orm.jdbc.tx.JdbcTransactionExecutorProvider;

  provides io.github.mmm.orm.spi.access.DbAccessProvider //
      with io.github.mmm.orm.jdbc.access.JdbcAccessProvider;

  exports io.github.mmm.orm.jdbc.access;

  exports io.github.mmm.orm.jdbc.connection;

}
