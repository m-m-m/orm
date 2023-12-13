/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.oracle;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.orm.Orm;
import io.github.mmm.orm.statement.DbStatementFormatter;

/**
 * Implementation of {@link DbDialect} for Oracle database.
 */
public final class OracleDialect extends AbstractDbDialect<OracleDialect> {

  /**
   * The constructor.
   */
  public OracleDialect() {

    super(new OracleTypeMapping());
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  protected OracleDialect(Orm orm) {

    super(orm);
  }

  @Override
  public String getName() {

    return "oracle";
  }

  @Override
  protected OracleDialect withOrm(Orm newOrm) {

    return new OracleDialect(newOrm);
  }

  @Override
  public DbStatementFormatter createFormatter() {

    return new OracleFormatter(getOrm());
  }

}
