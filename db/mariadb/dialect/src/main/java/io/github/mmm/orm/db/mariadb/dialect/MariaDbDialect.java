/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.mariadb.dialect;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.orm.mapping.Orm;

/**
 * Implementation of {@link DbDialect} for H2 database.
 */
public final class MariaDbDialect extends AbstractDbDialect<MariaDbDialect> {

  /**
   * The constructor.
   */
  public MariaDbDialect() {

    super(new MariaDbTypeMapping());
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  protected MariaDbDialect(Orm orm) {

    super(orm);
  }

  @Override
  public String getId() {

    return "mariadb";
  }

  @Override
  protected MariaDbDialect withOrm(Orm newOrm) {

    return new MariaDbDialect(newOrm);
  }

  @Override
  public DbDialectStatementFormatter createFormatter() {

    return new MariaDbFormatter(this);
  }

}
