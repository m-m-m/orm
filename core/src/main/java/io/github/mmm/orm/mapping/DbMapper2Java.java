/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping;

import io.github.mmm.orm.result.DbResult;

/**
 * Interface to map from database to Java object.
 *
 * @param <J> type of the Java object to map.
 * @since 1.0.0
 */
public abstract interface DbMapper2Java<J> {

  /**
   * @param dbValue the {@link DbResult} to map.
   * @return the mapped Java object.
   */
  J db2java(DbResult dbValue);

}
