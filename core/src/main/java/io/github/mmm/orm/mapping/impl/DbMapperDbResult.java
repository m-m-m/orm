/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping.impl;

import java.util.Iterator;

import io.github.mmm.orm.mapping.DbMapper;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;
import io.github.mmm.orm.result.impl.AbstractDbResult;
import io.github.mmm.orm.result.impl.DbResultObject;
import io.github.mmm.orm.result.impl.DbResultValueObject;

/**
 * A node of the mapping tree for mapping from Java to database and vice versa.
 *
 * @since 1.0.0
 */
public final class DbMapperDbResult implements DbMapper<DbResult> {

  /** The singleton instance. */
  public static final DbMapperDbResult INSTANCE = new DbMapperDbResult();

  @Override
  public DbResult java2db(DbResult javaValue) {

    return javaValue;
  }

  @Override
  public DbResult db2java(DbResult dbResult) {

    if (dbResult instanceof AbstractDbResult) {
      return dbResult;
    }
    // copy data to decouple from database and open transaction...
    int size = dbResult.getSize();
    DbResultValue<?>[] dbValues = new DbResultValue<?>[size];
    Iterator<DbResultValue<?>> iterator = dbResult.iterator();
    for (int i = 0; i < size; i++) {
      DbResultValue<?> dbValue = iterator.next();
      dbValues[i] = new DbResultValueObject<>(dbValue);
    }
    return new DbResultObject(dbValues);
  }

}
