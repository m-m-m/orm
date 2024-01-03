/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.pool.dbcp;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.jdbc.connection.JdbcConnectionPoolProvider;

/**
 * Implementation of {@link DbDialect} for H2 database.
 */
public final class DbcpConnectionPoolProvider extends JdbcConnectionPoolProvider {

  @Override
  public String getId() {

    return "dbcp";
  }

  @Override
  protected DataSource createDataSource(MetaInfo config) {

    try {
      BasicDataSource pool = BasicDataSourceFactory.createDataSource(config.asProperties());
      return pool;
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to create dbcp datasource for connection pool. Check your configuration ("
          + config.getKeyPrefix() + "*)!", e);
    }
  }

  @Override
  protected void close(DataSource dataSource) {

    try {
      BasicDataSource pool = (BasicDataSource) dataSource;
      pool.close();
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to close DBCP connection pool!", e);
    }
  }
}
