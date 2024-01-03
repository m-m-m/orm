/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.r2dbc.connection;

import java.time.Duration;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.base.temporal.TemporalType;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.source.DbSource;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.pool.ConnectionPoolConfiguration.Builder;
import io.r2dbc.spi.ConnectionFactory;

/**
 * Factory to configure and create {@link ConnectionPool}.
 */
class R2dbcConnectionPoolCreator {

  static ConnectionPool create(DbSource source, MetaInfo config, DbDialect dialect, ConnectionFactory factory) {

    Builder builder = ConnectionPoolConfiguration.builder(factory);
    int maxSize = config.getAsInteger(true, "maxSize", 20);
    builder = builder.maxSize(maxSize);
    builder = builder.maxIdleTime(getDuration(config, "maxIdleTime"));
    builder = builder.maxAcquireTime(getDuration(config, "maxAcquireTime"));
    builder = builder.maxCreateConnectionTime(getDuration(config, "maxCreateConnectionTime"));
    builder = builder.maxLifeTime(getDuration(config, "maxLifeTime"));
    builder = builder.maxValidationTime(getDuration(config, "maxValidationTime"));
    ConnectionPoolConfiguration configuration = builder.build();
    ConnectionPool pool = new ConnectionPool(configuration);
    return pool;
  }

  private static Duration getDuration(MetaInfo config, String key) {

    return config.getGeneric(true, false, key, TemporalType.DURATION);
  }

}
