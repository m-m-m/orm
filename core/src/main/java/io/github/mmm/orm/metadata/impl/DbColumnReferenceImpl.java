/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import io.github.mmm.orm.metadata.DbColumnReference;
import io.github.mmm.orm.metadata.DbForeignKey;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;

/**
 * Implementation of {@link DbForeignKey}.
 *
 * @since 1.0.0
 */
public class DbColumnReferenceImpl extends DbObjectWithNameImpl implements DbColumnReference {

  private final DbQualifiedName tableName;

  /**
   * The constructor.
   *
   * @param columnName the {@link #getName() column name}.
   * @param tableName the {@link #getTableName() table name}.
   */
  public DbColumnReferenceImpl(DbName columnName, DbQualifiedName tableName) {

    super(columnName);
    this.tableName = tableName;
  }

  @Override
  public DbQualifiedName getTableName() {

    return this.tableName;
  }

  @Override
  public void toString(StringBuilder sb, boolean detaild) {

    sb.append(this.tableName);
    sb.append("->");
    sb.append(this.name);
  }

}
