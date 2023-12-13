/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import io.github.mmm.base.sort.SortOrder;
import io.github.mmm.orm.metadata.DbColumn;
import io.github.mmm.orm.metadata.DbIndex;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbObjectContainer;

/**
 * Implementation of {@link DbIndex}.
 *
 * @since 1.0.0
 */
public class DbIndexImpl extends DbObjectWithNameImpl implements DbIndex {

  private final SortOrder sortOrder;

  private final DbObjectContainer<DbColumn> columns;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param sortOrder the {@link #getSortOrder() sort order}.
   * @param columns the {@link #getColumns() columns}.
   */
  public DbIndexImpl(DbName name, SortOrder sortOrder, DbObjectContainer<DbColumn> columns) {

    super(name);
    this.sortOrder = sortOrder;
    this.columns = columns;
  }

  @Override
  public SortOrder getSortOrder() {

    return this.sortOrder;
  }

  @Override
  public DbObjectContainer<DbColumn> getColumns() {

    return this.columns;
  }

  @Override
  public void toString(StringBuilder sb, boolean detailed) {

    sb.append("Index");
    if (this.name == null) {
      sb.append(' ');
      sb.append(this.name);
    }
    if (detailed) {
      sb.append(' ');
      sb.append(this.columns);
    }
  }

}
