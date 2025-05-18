/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.pool.hikari;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.jdbc.connection.JdbcConnectionPoolProvider;
import io.github.mmm.orm.source.DbSource;

/**
 * Implementation of {@link JdbcConnectionPoolProvider} for {@link HikariDataSource hikari}.
 */
public final class HikariConnectionPoolProvider extends JdbcConnectionPoolProvider {

  @Override
  public String getId() {

    return "hikari";
  }

  @Override
  protected DataSource createDataSource(MetaInfo metaInfo) {

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(metaInfo.getRequired(DbSource.KEY_URL));
    config.setUsername(metaInfo.getRequired(DbSource.KEY_USER));
    config.setPassword(metaInfo.getRequired(DbSource.KEY_PASSWORD));
    for (String key : metaInfo) {
      if (!DbSource.STANDARD_KEYS.contains(key)) {
        config.addDataSourceProperty(key, metaInfo.get(key));
      }
    }
    return new HikariDataSource(config);
  }

  @Override
  protected void close(DataSource dataSource) {

    HikariDataSource pool = (HikariDataSource) dataSource;
    pool.close();
  }

}
