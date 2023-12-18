/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.pool.c3po;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.jdbc.connection.JdbcConnectionPoolProvider;
import io.github.mmm.orm.source.DbSource;

/**
 * Implementation of {@link DbDialect} for H2 database.
 */
public final class C3poConnectionPoolProvider extends JdbcConnectionPoolProvider {

  @Override
  public String getId() {

    return "c3po";
  }

  @Override
  protected DataSource createDataSource(MetaInfo config) {

    ComboPooledDataSource pool = new ComboPooledDataSource();
    pool.setProperties(config.asProperties());
    pool.setJdbcUrl(config.getRequired(DbSource.KEY_URL));
    pool.setUser(config.getRequired(DbSource.KEY_USER));
    pool.setPassword(config.getRequired(DbSource.KEY_PASSWORD));
    return pool;
  }

}
