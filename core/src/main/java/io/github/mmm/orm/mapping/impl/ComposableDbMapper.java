/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping.impl;

import java.util.Iterator;

import io.github.mmm.orm.mapping.DbMapper;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;

/**
 * Abstract base implementation of {@link DbMapper} that can be composed.
 *
 * @param <J> type of the Java object to map.
 * @since 1.0.0
 */
public abstract class ComposableDbMapper<J> implements DbMapper<J> {

  @Override
  public J db2java(DbResult dbValue) {

    return db2java(dbValue.iterator());
  }

  /**
   * @param dbIterator the {@link Iterator} of {@link DbResultValue}s to map.
   * @return the mapped Java object.
   */
  public abstract J db2java(Iterator<DbResultValue<?>> dbIterator);

}
