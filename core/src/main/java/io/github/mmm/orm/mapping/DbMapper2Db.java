/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping;

import io.github.mmm.orm.result.DbResult;

/**
 * Interface to map a Java object to the database.
 *
 * @param <J> type of the Java object to map.
 * @since 1.0.0
 */
public interface DbMapper2Db<J> {

  /**
   * @param javaValue the Java object to map.
   * @return the mapped {@link DbResult}.
   */
  DbResult java2db(J javaValue);

}
