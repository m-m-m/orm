/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

/**
 * A {@link StartClause} that is not a {@link MainDbClause} and therefore incomplete before the statement is properly
 * build and received via {@link MainDbClause#get()}. However, for generic access it offers the method
 * {@link #getStatement()}.
 *
 * @since 1.0.0
 */
public interface IncompleteStartClause extends StartClause {

  /**
   * @return the actual {@link DbStatement} containing all {@link DbClause}s and representing your entire SQL. May be
   *         {@code null} or result in an incomplete statement. This method should therefore not be used by API
   *         end-users.
   */
  DbStatement<?> getStatement();

}
