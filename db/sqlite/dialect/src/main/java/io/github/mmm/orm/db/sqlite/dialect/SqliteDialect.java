/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.sqlite.dialect;

import java.util.Map;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.source.DbSource;

/**
 * Implementation of {@link DbDialect} for SQLite database.
 */
public class SqliteDialect extends AbstractDbDialect<SqliteDialect> {

  /** The filename of the default database. */
  public static final String DEFAULT_DB = "test.db";

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

    return "sqlite";
  }

  @Override
  protected SqliteDialect withOrm(Orm newOrm) {

    return new SqliteDialect(newOrm);
  }

  @Override
  public DbDialectStatementFormatter createFormatter() {

    return new SqliteFormatter(this);
  }

  @Override
  public void autoConfigure(Map<String, String> config, DbSource source) {

    if (config.get(DbSource.KEY_USER) == null) {
      config.put(DbSource.KEY_USER, "test");
    }
    super.autoConfigure(config, source);
  }

  @Override
  protected String autoConfigureUrl(Map<String, String> config, DbSource source, String kind) {

    return kind + ":sqlite:target/" + DEFAULT_DB;
  }

  @Override
  public boolean isSupportingSequence() {

    return true; // this is a lie but we better emulate sequence as table
  }

}
