/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import io.github.mmm.orm.metadata.DbColumn;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbObjectContainer;
import io.github.mmm.orm.metadata.DbPrimaryKey;

/**
 * Implementation of {@link DbPrimaryKey}.
 *
 * @since 1.0.0
 */
public class DbPrimaryKeyImpl extends DbObjectWithNameImpl implements DbPrimaryKey {

  private final DbObjectContainer<DbColumn> columns;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param columns the {@link #getColumns() columns}.
   */
  public DbPrimaryKeyImpl(DbName name, DbObjectContainer<DbColumn> columns) {

    super(name);
    this.columns = columns;
  }

  @Override
  public DbObjectContainer<DbColumn> getColumns() {

    return this.columns;
  }

  @Override
  public void toString(StringBuilder sb, boolean detailed) {

    sb.append("PK");
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
