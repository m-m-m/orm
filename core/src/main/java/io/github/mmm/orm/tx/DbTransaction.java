/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.tx;

import io.github.mmm.orm.source.DbSource;

/**
 * Interface for a transaction.
 *
 * @since 1.0.0
 */
public interface DbTransaction {

  /**
   * @return {@code true} if this transaction is still open, {@code false} otherwise (if closed as committed or
   *         rolled-backe).
   */
  boolean isOpen();

  /**
   * @return the {@link DbSource} of this transaction.
   */
  DbSource getSource();

}
