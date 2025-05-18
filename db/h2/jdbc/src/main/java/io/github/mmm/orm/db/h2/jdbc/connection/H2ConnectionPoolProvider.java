/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.h2.jdbc.connection;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.jdbc.connection.JdbcConnectionPoolProvider;
import io.github.mmm.orm.source.DbSource;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

/**
 * Implementation of {@link JdbcConnectionPoolProvider} for H2 database.
 *
 * @since 1.0.0
 */
public class H2ConnectionPoolProvider extends JdbcConnectionPoolProvider {

  @Override
  public String getId() {

    return "h2";
  }

  @Override
  public boolean isResponsible(String url) {

    return url.startsWith("jdbc:h2:");
  }

  @Override
  public boolean isPool() {
    return false;
  }

  @Override
  protected DataSource createDataSource(MetaInfo config) {

    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL(config.getRequired(DbSource.KEY_URL));
    dataSource.setUser(config.getRequired(DbSource.KEY_USER));
    dataSource.setPassword(config.getRequired(DbSource.KEY_PASSWORD));
    return dataSource;
  }

  @Override
  protected void close(DataSource dataSource) {

    // nothing to do - no pool
  }

}
