/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.tx;

import io.github.mmm.orm.source.DbSource;

/**
 * Provider of {@link DbTransactionExecutor}.
 *
 * @see DbTransactionExecutor#get(DbSource)
 * @since 1.0.0
 */
public interface DbTransactionExecutorProvider {

  /**
   * @param source the {@link DbSource}.
   * @return the corresponding {@link DbTransactionExecutor}.
   */
  DbTransactionExecutor create(DbSource source);

}
