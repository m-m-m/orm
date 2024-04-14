/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.access;

import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.spi.access.impl.DbAccessProviderAdapter;
import io.github.mmm.orm.tx.DbTransactionExecutor;

/**
 * Interface providing generic database access with support for all kind of
 * {@link io.github.mmm.orm.statement.DbStatement statements}.
 */
public interface DbAccess extends DbCreateAccess, DbDeleteAccess, DbInsertAccess, DbMergeAccess, DbSelectAccess,
    DbUpdateAccess, DbUpsertAccess {

  /**
   * @param source the {@link DbSource}.
   * @return the corresponding {@link DbTransactionExecutor}.
   */
  static DbAccess get(DbSource source) {

    return DbAccessProviderAdapter.INSTANCE.create(source);
  }

}
