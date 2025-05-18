/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.sqlite.jdbc.connection;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.jdbc.connection.JdbcConnectionPoolProvider;
import io.github.mmm.orm.source.DbSource;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

/**
 * Implementation of {@link JdbcConnectionPoolProvider} for SQLite.
 *
 * @since 1.0.0
 */
public class SqliteConnectionPoolProvider extends JdbcConnectionPoolProvider {

  @Override
  public String getId() {

    return "sqlite";
  }

  @Override
  public boolean isResponsible(String url) {

    return url.startsWith("jdbc:sqlite:");
  }

  @Override
  public boolean isPool() {
    return false;
  }

  @Override
  protected DataSource createDataSource(MetaInfo config) {

    SQLiteDataSource dataSource = new SQLiteDataSource();
    dataSource.setUrl(config.getRequired(DbSource.KEY_URL));
    // no login data (username/password)
    return dataSource;
  }

  @Override
  protected void close(DataSource dataSource) {

    // nothing to do - no pool
  }

}
