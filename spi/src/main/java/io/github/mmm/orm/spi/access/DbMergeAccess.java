/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.access;

import io.github.mmm.orm.statement.merge.MergeStatement;
import io.github.mmm.orm.statement.upsert.UpsertStatement;

/**
 * Interface providing support for {@link #execute(MergeStatement)}.
 *
 * @since 1.0.0
 */
public interface DbMergeAccess {

  /**
   * @param statement the {@link UpsertStatement} to execute.
   * @return the number of records that have been deleted.
   */
  long execute(MergeStatement<?> statement);

}
