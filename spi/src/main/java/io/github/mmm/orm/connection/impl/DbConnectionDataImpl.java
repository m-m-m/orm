/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.connection.impl;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.connection.DbConnectionPool;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.source.DbSource;

/**
 * Implementation of {@link DbConnectionData}.
 */
public class DbConnectionDataImpl implements DbConnectionData {

  private final DbSource source;

  private final DbDialect dialect;

  private final MetaInfo config;

  private final DbConnectionPool<?> pool;

  private final DbQualifiedName qualifiedNameTemplate;

  /**
   * The constructor.
   *
   * @param source the {@link #getSource() source}.
   * @param dialect the {@link #getDialect() dialect}.
   * @param config the {@link #getConfig() config}.
   * @param pool the {@link #getPool() pool}.
   */
  public DbConnectionDataImpl(DbSource source, DbDialect dialect, MetaInfo config, DbConnectionPool<?> pool) {

    super();
    this.source = source;
    this.dialect = dialect;
    this.config = config;
    this.pool = pool;
    this.qualifiedNameTemplate = new DbQualifiedName(DbName.of(getCatalog()), DbName.of(getSchema()),
        DbName.of("template"));
  }

  @Override
  public DbDialect getDialect() {

    return this.dialect;
  }

  @Override
  public DbSource getSource() {

    return this.source;
  }

  @Override
  public MetaInfo getConfig() {

    return this.config;
  }

  @Override
  public DbConnectionPool<?> getPool() {

    return this.pool;
  }

  @Override
  public DbQualifiedName getQualifiedNameTemplate() {

    return this.qualifiedNameTemplate;
  }

  @Override
  public String toString() {

    return this.source + "@" + this.dialect.getId() + "[" + this.config.get(DbSource.KEY_URL) + "]";
  }

}
