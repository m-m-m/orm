/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

import io.github.mmm.base.sort.SortOrder;

/**
 * Interface for a foreign key of a {@link DbTable}.
 */
public interface DbIndex extends DbObject {

  /**
   * @return the {@link SortOrder} or {@code null} if undefined (sequence is not supported or index is statistic).
   */
  SortOrder getSortOrder();

  /**
   * @return the {@link DbObjectContainer container} with the indexed {@link DbColumn columns}. A simple index is only
   *         indexing a single column. However, an index that indexes multiple columns is quite common.
   */
  DbObjectContainer<DbColumn> getColumns();

}
