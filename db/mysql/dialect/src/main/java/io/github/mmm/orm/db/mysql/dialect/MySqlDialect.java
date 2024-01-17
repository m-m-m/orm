/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.mysql.dialect;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.statement.AbstractDbStatementFormatter;

/**
 * Implementation of {@link DbDialect} for H2 database.
 */
public final class MySqlDialect extends AbstractDbDialect<MySqlDialect> {

  /**
   * The constructor.
   */
  public MySqlDialect() {

    super(new MySqlTypeMapping());
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  protected MySqlDialect(Orm orm) {

    super(orm);
  }

  @Override
  public String getId() {

    return "mysql";
  }

  @Override
  protected MySqlDialect withOrm(Orm newOrm) {

    return new MySqlDialect(newOrm);
  }

  @Override
  public AbstractDbStatementFormatter createFormatter() {

    return new MySqlFormatter(this);
  }

}
