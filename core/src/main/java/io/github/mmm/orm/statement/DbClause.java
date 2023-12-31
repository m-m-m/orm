/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

/**
 * A {@link DbClause} is a top-level fragment (part) of an SQL {@link DbStatement}.
 *
 * @since 1.0.0
 */
public abstract interface DbClause {

  /**
   * @return {@code true} if this {@link DbClause} should be omitted as it is optional and entirely empty, {@code false}
   *         otherwise.
   */
  default boolean isOmit() {

    return false;
  }

}
