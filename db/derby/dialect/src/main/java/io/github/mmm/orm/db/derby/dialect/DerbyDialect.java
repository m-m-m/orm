/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.derby.dialect;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.orm.mapping.Orm;

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

    return "derby";
  }

  @Override
  protected DerbyDialect withOrm(Orm newOrm) {

    return new DerbyDialect(newOrm);
  }

  @Override
  public DbDialectStatementFormatter createFormatter() {

    return new DerbyFormatter(this);
  }

}
