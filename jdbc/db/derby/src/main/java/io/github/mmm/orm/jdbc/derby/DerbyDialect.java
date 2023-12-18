/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.derby;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.orm.Orm;
import io.github.mmm.orm.statement.DbStatementFormatter;

/**
 * Implementation of {@link DbDialect} for H2 database.
 */
public final class DerbyDialect extends AbstractDbDialect<DerbyDialect> {

  /**
   * The constructor.
   */
  public DerbyDialect() {

    super(new DerbyTypeMapping());
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  protected DerbyDialect(Orm orm) {

    super(orm);
  }

  @Override
  public String getId() {

    return "h2";
  }

  @Override
  protected DerbyDialect withOrm(Orm newOrm) {

    return new DerbyDialect(newOrm);
  }

  @Override
  public DbStatementFormatter createFormatter() {

    return new DerbyFormatter(getOrm());
  }

}
