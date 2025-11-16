/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.r2dbc.tx;

import java.util.concurrent.Callable;

import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.r2dbc.session.R2dbcSession;
import io.github.mmm.orm.tx.DbTransaction;
import io.github.mmm.orm.tx.DbTransactionExecutor;

/**
 * Implementation of {@link DbTransactionExecutor} for JDBC.
 */
public class R2dbcTransactionExecutor implements DbTransactionExecutor {

  private final DbConnectionData connectionData;

  /**
   * The constructor.
   *
   * @param connectionData the {@link DbConnectionData}.
   */
  public R2dbcTransactionExecutor(DbConnectionData connectionData) {

    super();
    this.connectionData = connectionData;
  }

  @Override
  public <R> R doInTx(Callable<R> task) {

    return R2dbcSession.doInTx(this.connectionData, () -> task.call());
  }

  @Override
  public DbTransaction getTransaction() {

    return R2dbcSession.get().getR2dbcConnection();
  }

}
