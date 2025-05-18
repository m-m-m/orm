/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.connection.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import io.github.mmm.base.collection.ReadOnlyIterator;
import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.orm.connection.DbConnection;
import io.github.mmm.orm.connection.DbConnectionPoolProvider;
import io.github.mmm.orm.connection.DbConnectionPoolProviderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link DbConnectionPoolProviderManager}.
 */
public class DbConnectionPoolProviderManagerImpl implements DbConnectionPoolProviderManager {

  private static final Logger LOG = LoggerFactory.getLogger(DbConnectionPoolProviderManagerImpl.class);

  /** The singleton instance. */
  public static final DbConnectionPoolProviderManagerImpl INSTANCE = new DbConnectionPoolProviderManagerImpl();

  private final Map<String, DbConnectionPoolProvider<?>> providers;

  /**
   * The constructor.
   */
  @SuppressWarnings("rawtypes")
  public DbConnectionPoolProviderManagerImpl() {

    super();
    this.providers = new HashMap<>();
    ServiceLoader<DbConnectionPoolProvider> loader = ServiceLoader.load(DbConnectionPoolProvider.class);
    for (DbConnectionPoolProvider<?> provider : loader) {
      register(provider);
    }
    if (this.providers.isEmpty()) {
      LOG.error("No DbConnectionPoolProvider was found! Please add at least one mmm-orm-jdbc-pool-* dependency to your application.");
    }
  }

  private void register(DbConnectionPoolProvider<?> provider) {

    DbConnectionPoolProvider<?> duplicate = this.providers.put(provider.getId(), provider);
    if (duplicate != null) {
      throw new DuplicateObjectException(duplicate, provider.getId(), provider);
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public <C extends DbConnection> DbConnectionPoolProvider<C> get(String id) {

    DbConnectionPoolProvider provider = this.providers.get(id);
    if (provider == null) {
      throw new ObjectNotFoundException(DbConnectionPoolProvider.class, id);
    }
    return provider;
  }

  @Override
  public Iterator<DbConnectionPoolProvider<?>> iterator() {

    return new ReadOnlyIterator<>(this.providers.values().iterator());
  }

}
