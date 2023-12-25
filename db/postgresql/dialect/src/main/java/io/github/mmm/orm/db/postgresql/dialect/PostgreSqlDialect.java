/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.postgresql.dialect;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.statement.DbStatementFormatter;

/**
 * Implementation of {@link DbDialect} for H2 database.
 */
public class PostgreSqlDialect extends AbstractDbDialect<PostgreSqlDialect> {

  /**
   * The constructor.
   */
  public PostgreSqlDialect() {

    super(new PostgreSqlTypeMapper());
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  protected PostgreSqlDialect(Orm orm) {

    super(orm);
  }

  @Override
  public String getId() {

    return "postgresql";
  }

  @Override
  protected PostgreSqlDialect withOrm(Orm newOrm) {

    return new PostgreSqlDialect(newOrm);
  }

  @Override
  public DbStatementFormatter createFormatter() {

    return new PostgreSqlFormatter(this);
  }

}
