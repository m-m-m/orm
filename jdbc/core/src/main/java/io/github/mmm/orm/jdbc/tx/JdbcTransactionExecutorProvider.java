/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.tx;

import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.tx.DbTransactionExecutor;
import io.github.mmm.orm.tx.DbTransactionExecutorProvider;

/**
 * Implementation of {@link DbTransactionExecutorProvider} for JDBC.
 */
public class JdbcTransactionExecutorProvider implements DbTransactionExecutorProvider {

  @Override
  public DbTransactionExecutor create(DbSource source) {

    DbConnectionData data = DbConnectionData.of(source);
    if (DbSource.VALUE_KIND_JDBC.equals(data.getKind())) {
      return new JdbcTransactionExecutor(data);
    }
    return null;
  }

}
