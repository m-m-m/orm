/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.access.session;

import io.github.mmm.orm.metadata.DbMetaData;

/**
 * Interface for a database connection.
 */
public interface DbConnection {

  /**
   * @return the {@link DbMetaData}.
   */
  DbMetaData getMetaData();

}
