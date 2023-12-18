/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.h2;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.orm.Orm;
import io.github.mmm.orm.statement.DbStatementFormatter;

/**
 * Implementation of {@link DbDialect} for H2 database.
 */
public final class H2Dialect extends AbstractDbDialect<H2Dialect> {

  /**
   * The constructor.
   */
  public H2Dialect() {

    super(new H2TypeMapping());
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  protected H2Dialect(Orm orm) {

    super(orm);
  }

  @Override
  public String getId() {

    return "h2";
  }

  @Override
  protected H2Dialect withOrm(Orm newOrm) {

    return new H2Dialect(newOrm);
  }

  @Override
  public DbStatementFormatter createFormatter() {

    return new H2Formatter(getOrm());
  }

}
