/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.connection.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.connection.DbConnectionPool;
import io.github.mmm.orm.connection.DbConnectionPoolProvider;
import io.github.mmm.orm.connection.DbConnectionPoolProviderManager;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.dialect.DbDialectProvider;
import io.github.mmm.orm.source.DbSource;

/**
 * Manager of {@link DbConnectionData}.
 *
 * @since 1.0.0
 */
public final class DbConnectionDataManager {

  private static final Logger LOG = LoggerFactory.getLogger(DbConnectionDataManager.class);

  /** The singleton instance. */
  public static final DbConnectionDataManager INSTANCE = new DbConnectionDataManager();

  private final Map<String, DbConnectionDataImpl> data;

  private DbConnectionDataManager() {

    super();
    this.data = new ConcurrentHashMap<>();
  }

  /**
   * @param source the {@link DbSource}.
   * @return the existing {@link DbConnectionData} or {@code null} if not {@link #getOrCreate(DbSource) created} before.
   */
  public DbConnectionData get(DbSource source) {

    return this.data.get(source.getId());
  }

  /**
   * @param source the {@link DbSource}.
   * @return the corresponding {@link DbConnectionData}. Will be created if not yet exists.
   * @see DbConnectionData#of(DbSource)
   */
  public DbConnectionData getOrCreate(DbSource source) {

    return this.data.computeIfAbsent(source.getId(), k -> create(source));
  }

  private DbConnectionDataImpl create(DbSource source) {

    MetaInfo config = MetaInfo.config().with(source.getPropertyPrefix());
    Map<String, String> map = config.asMap();
    config = MetaInfo.empty().with(map);
    AbstractDbDialect<?> dialect = getDialect(map, source);
    dialect.autoConfigure(map, source);
    DbConnectionPool<?> pool = createPool(config, source, dialect, map);
    return new DbConnectionDataImpl(source, dialect, config, pool);
  }

  private DbConnectionPool<?> createPool(MetaInfo config, DbSource source, DbDialect dialect, Map<String, String> map) {

    String poolId = config.get(DbSource.KEY_POOL);
    DbConnectionPoolProviderManager manager = DbConnectionPoolProviderManager.get();
    DbConnectionPoolProvider<?> provider = null;
    if (poolId == null) {
      String url = config.getRequired(DbSource.KEY_URL);
      for (DbConnectionPoolProvider<?> currentProvider : manager) {
        if (currentProvider.isResponsible(url)) {
          if (provider == null) {
            provider = currentProvider;
          } else {
            LOG.warn(
                "Connection pool provider is ambiguous: already found {} but also found {}. Please specify property {} explicitly.",
                provider.getId(), currentProvider.getId(), source.getPropertyKey(DbSource.KEY_POOL));
          }
        }
      }
      if (provider != null) {
        map.put(DbSource.KEY_POOL, provider.getId());
      }
    } else {
      provider = manager.get(poolId);
    }
    if (provider == null) {
      throw new ObjectNotFoundException("DbConnectionPoolProvider");
    }
    return provider.create(source, config, dialect);
  }

  private AbstractDbDialect<?> getDialect(Map<String, String> config, DbSource source) {

    DbDialectProvider provider = DbDialectProvider.get();
    DbDialect dialect = null;
    String dialectId = config.get(DbSource.KEY_DIALECT);
    if (dialectId == null) {
      String url = config.get(DbSource.KEY_URL);
      if (url == null) {
        for (DbDialect d : provider) {
          if (dialect != null) {
            dialect = null;
            break; // force ObjectNotFoundException because dialect is ambiguous (multiple dialects present)
          }
          dialect = d;
        }
      } else {
        dialect = provider.getByDbUrl(url);
      }
      if (dialect == null) {
        throw new ObjectNotFoundException("Property", source.getPropertyKey(DbSource.KEY_DIALECT));
      }
      config.put(DbSource.KEY_DIALECT, dialect.getId());
    } else {
      dialect = provider.get(dialectId);
    }
    return (AbstractDbDialect<?>) dialect;
  }

}
