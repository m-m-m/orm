/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.connection;

import javax.sql.DataSource;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.connection.DbConnectionPool;
import io.github.mmm.orm.connection.DbConnectionPoolProvider;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.source.DbSource;

/**
 * Abstract base implementation of {@link DbConnectionPoolProvider} for JDBC.
 *
 * @since 1.0.0
 */
public abstract class JdbcConnectionPoolProvider implements DbConnectionPoolProvider<JdbcConnection> {

  @Override
  public String getKind() {

    return "jdbc";
  }

  @SuppressWarnings("exports")
  @Override
  public DbConnectionPool<JdbcConnection> create(DbSource source, MetaInfo config, DbDialect dialect) {

    DataSource dataSource = createDataSource(config);
    return new JdbcConnectionPool(dataSource, source, dialect);
  }

  /**
   * @param config the configuration as {@link MetaInfo}.
   * @return the connection pool as {@link DataSource}.
   */
  protected abstract DataSource createDataSource(MetaInfo config);

}
