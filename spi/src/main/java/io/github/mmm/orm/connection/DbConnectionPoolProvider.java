/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.connection;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.source.DbSource;

/**
 * Service provider interface (SPI) to {@link #create(DbSource, MetaInfo, DbDialect) create} instances of
 * {@link DbConnectionPool} for a specific provider implementation.
 *
 * @param <C> type of the {@link DbConnection}.
 */
public interface DbConnectionPoolProvider<C extends DbConnection> {

  /**
   * @return the identifier of this provider.
   */
  String getId();

  /**
   * @return the {@link DbSource#KEY_KIND kind} of database connection.
   */
  String getKind();

  /**
   * @param url the database connection URL (e.g. JDBC URL).
   * @return {@code true} if this {@link DbConnectionPoolProvider} is responsible for the given {@code url},
   *         {@code false} otherwise.
   */
  default boolean isResponsible(String url) {

    return url.startsWith(getKind());
  }

  /**
   * @return {@code true} if a real connection pool is provided, {@code false} otherwise.
   */
  default boolean isPool() {

    return true;
  }

  /**
   * @param source the {@link DbSource}.
   * @param config the {@link MetaInfo} with the database configuration.
   * @param dialect the {@link DbDialect} to use.
   * @return the new {@link DbConnectionPool} instance for the given configuration.
   */
  DbConnectionPool<C> create(DbSource source, MetaInfo config, DbDialect dialect);

}
