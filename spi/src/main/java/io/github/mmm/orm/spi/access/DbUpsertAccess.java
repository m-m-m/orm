/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.access;

import io.github.mmm.orm.statement.upsert.UpsertStatement;

/**
 * Interface providing support for {@link #execute(UpsertStatement)}.
 */
public interface DbUpsertAccess {

  /**
   * @param statement the {@link UpsertStatement} to execute.
   * @return the number of records that have been deleted.
   */
  long execute(UpsertStatement<?> statement);

}
