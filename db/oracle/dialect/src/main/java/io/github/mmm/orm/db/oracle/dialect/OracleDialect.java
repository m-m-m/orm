/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.oracle.dialect;

import java.util.Map;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.statement.DbStatementFormatter;

/**
 * Implementation of {@link DbDialect} for Oracle database.
 */
public class OracleDialect extends AbstractDbDialect<OracleDialect> {

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
  public String getId() {

    return "oracle";
  }

  @Override
  public String getType() {

    return "oracle";
  }

  @Override
  protected OracleDialect withOrm(Orm newOrm) {

    return new OracleDialect(newOrm);
  }

  @Override
  public DbStatementFormatter createFormatter() {

    return new OracleFormatter(this);
  }

  @Override
  protected String autoConfigureUrl(Map<String, String> config, DbSource source, String kind) {

    return kind + ":oracle:thin:@//localhost:1521/xepdb1";
  }

}
