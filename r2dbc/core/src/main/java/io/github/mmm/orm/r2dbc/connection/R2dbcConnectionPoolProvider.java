/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.r2dbc.connection;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.connection.DbConnectionPool;
import io.github.mmm.orm.connection.DbConnectionPoolProvider;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.source.DbSource;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ConnectionFactoryOptions.Builder;
import io.r2dbc.spi.Option;

/**
 * Abstract base implementation of {@link DbConnectionPoolProvider} for JDBC.
 *
 * @since 1.0.0
 */
public class R2dbcConnectionPoolProvider implements DbConnectionPoolProvider<R2dbcConnection> {

  @Override
  public String getId() {

    return "pool";
  }

  @Override
  public String getKind() {

    return "r2dbc";
  }

  @Override
  public DbConnectionPool<R2dbcConnection> create(DbSource source, MetaInfo config, DbDialect dialect) {

    ConnectionFactoryOptions options = createOptions(source, config);
    ConnectionFactory factory = ConnectionFactories.get(options);
    ConnectionPool pool = R2dbcConnectionPoolCreator.create(source, config, dialect, factory);
    return new R2dbcConnectionPool(pool, source, dialect);
  }

  private ConnectionFactoryOptions createOptions(DbSource source, MetaInfo config) {

    Builder builder = ConnectionFactoryOptions.builder();
    builder = builder.option(ConnectionFactoryOptions.DRIVER, "pool");
    String url = config.get(DbSource.KEY_URL);
    if (url != null) {
      ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(url);
      builder = builder.from(options);
    }
    builder = option(builder, config, DbSource.KEY_USER, ConnectionFactoryOptions.USER);
    builder = option(builder, config, DbSource.KEY_PASSWORD, ConnectionFactoryOptions.PASSWORD);
    builder = option(builder, config, DbSource.KEY_HOST, ConnectionFactoryOptions.HOST);
    builder = optInt(builder, config, DbSource.KEY_PORT, ConnectionFactoryOptions.PORT);
    builder = option(builder, config, DbSource.KEY_DATABASE, ConnectionFactoryOptions.DATABASE);
    return builder.build();
  }

  private Builder option(Builder builder, MetaInfo config, String key, Option<? super String> option) {

    String value = config.get(key);
    if (value != null) {
      builder = builder.option(option, value);
    }
    return builder;
  }

  private Builder optInt(Builder builder, MetaInfo config, String key, Option<Integer> option) {

    Integer value = config.getAsInteger(key);
    if (value != null) {
      builder = builder.option(option, value);
    }
    return builder;
  }

}
