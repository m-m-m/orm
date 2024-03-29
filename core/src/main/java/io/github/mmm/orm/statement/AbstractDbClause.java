/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.orm.impl.DbContextNone;

/**
 * Abstract base class implementing {@link DbClause}.
 *
 * @since 1.0.0
 */
public abstract class AbstractDbClause implements DbClause {

  /**
   * The constructor.
   */
  public AbstractDbClause() {

    super();
  }

  @Override
  public String toString() {

    BasicDbStatementFormatter formatter = new BasicDbStatementFormatter();
    formatter.formatClause(this, DbContextNone.INSTANCE);
    return formatter.toString();
  }
}
