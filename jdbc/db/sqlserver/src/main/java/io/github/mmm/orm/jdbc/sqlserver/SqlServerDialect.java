/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.sqlserver;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.orm.Orm;
import io.github.mmm.orm.statement.DbStatementFormatter;

/**
 * Implementation of {@link DbDialect} for <a href="https://docs.microsoft.com/en-us/sql/">MS SQL Server</a>.
 *
 * @since 1.0.0
 */
public class SqlServerDialect extends AbstractDbDialect<SqlServerDialect> {

  /**
   * The constructor.
   */
  public SqlServerDialect() {

    super(new SqlServerTypeMapper());
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  protected SqlServerDialect(Orm orm) {

    super(orm);
  }

  @Override
  public String getId() {

    return "sqlserver";
  }

  @Override
  protected SqlServerDialect withOrm(Orm newOrm) {

    return new SqlServerDialect(newOrm);
  }

  @Override
  public DbStatementFormatter createFormatter() {

    return new SqlServerFormatter(getOrm());
  }

}
