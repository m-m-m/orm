/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.h2.dialect;

import java.util.Map;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.orm.Orm;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.statement.DbStatementFormatter;

/**
 * Implementation of {@link DbDialect} for H2 database.
 */
public final class H2Dialect extends AbstractDbDialect<H2Dialect> {

  /**
   * The constructor.
   */
  public H2Dialect() {

    super(new H2TypeMapping());
  }

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  protected H2Dialect(Orm orm) {

    super(orm);
  }

  @Override
  public String getId() {

    return "h2";
  }

  @Override
  protected H2Dialect withOrm(Orm newOrm) {

    return new H2Dialect(newOrm);
  }

  @Override
  public DbStatementFormatter createFormatter() {

    return new H2Formatter(getOrm());
  }

  @Override
  public void autoConfigure(Map<String, String> config, DbSource source) {

    if (config.get(DbSource.KEY_USER) == null) {
      config.put(DbSource.KEY_USER, "sa");
    }
    super.autoConfigure(config, source);
  }

  @Override
  protected String autoConfigureUrl(Map<String, String> config, DbSource source, String kind) {

    return kind + ":h2:mem:";
  }

}
