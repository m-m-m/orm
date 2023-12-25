/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.sqlite.dialect;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.statement.DbStatementFormatter;

/**
 * Implementation of {@link DbDialect} for SQLite database.
 */
public class SqliteDialect extends AbstractDbDialect<SqliteDialect> {

  /**
   * The constructor.
   */
  public SqliteDialect() {

    super(new SqliteTypeMapper());
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  protected SqliteDialect(Orm orm) {

    super(orm);
  }

  @Override
  public String getId() {

    return "postgresql";
  }

  @Override
  protected SqliteDialect withOrm(Orm newOrm) {

    return new SqliteDialect(newOrm);
  }

  @Override
  public DbStatementFormatter createFormatter() {

    return new SqliteFormatter(this);
  }

}
