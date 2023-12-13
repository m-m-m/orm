/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import io.github.mmm.orm.metadata.DbColumnReference;
import io.github.mmm.orm.metadata.DbForeignKey;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbObjectContainer;

/**
 * Implementation of {@link DbForeignKey}.
 *
 * @since 1.0.0
 */
public class DbForeignKeyImpl extends DbObjectWithNameImpl implements DbForeignKey {

  private final DbObjectContainer<DbColumnReference> columns;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param columns the {@link #getColumns() columns}.
   */
  public DbForeignKeyImpl(DbName name, DbObjectContainer<DbColumnReference> columns) {

    super(name);
    this.columns = columns;
  }

  @Override
  public DbObjectContainer<DbColumnReference> getColumns() {

    return this.columns;
  }

  @Override
  public void toString(StringBuilder sb, boolean detailed) {

    sb.append("FK");
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
