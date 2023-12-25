/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.tx;

import java.util.concurrent.Callable;

import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.jdbc.session.JdbcSession;
import io.github.mmm.orm.tx.DbTransaction;
import io.github.mmm.orm.tx.DbTransactionExecutor;

/**
 * Implementation of {@link DbTransactionExecutor} for JDBC.
 */
public class JdbcTransactionExecutor implements DbTransactionExecutor {

  private final DbConnectionData connectionData;

  /**
   * The constructor.
   *
   * @param connectionData the {@link DbConnectionData}.
   */
  public JdbcTransactionExecutor(DbConnectionData connectionData) {

    super();
    this.connectionData = connectionData;
  }

  @Override
  public <R> R doInTx(Callable<R> task) {

    return JdbcSession.doInTx(this.connectionData, task);
  }

  @Override
  public DbTransaction getTransaction() {

    return JdbcSession.get().getJdbcConnection();
  }

}
