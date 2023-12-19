/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.tx.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import io.github.mmm.base.service.ServiceHelper;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.tx.DbTransactionExecutor;
import io.github.mmm.orm.tx.DbTransactionExecutorProvider;

/**
 * Adapter implementation of {@link DbTransactionExecutorProvider} that holds the {@link DbTransactionExecutor}s in
 * memory.
 *
 * @since 1.0.0
 */
public final class DbTransactionExceutorProviderAdapter implements DbTransactionExecutorProvider {

  /** The singleton instance. */
  public static final DbTransactionExceutorProviderAdapter INSTANCE = new DbTransactionExceutorProviderAdapter();

  private final List<DbTransactionExecutorProvider> providers;

  private final Map<String, DbTransactionExecutor> executors;

  /**
   * The constructor.
   */
  public DbTransactionExceutorProviderAdapter() {

    super();
    this.providers = new ArrayList<>();
    ServiceHelper.all(ServiceLoader.load(DbTransactionExecutorProvider.class), this.providers);
    this.executors = new ConcurrentHashMap<>();
  }

  @Override
  public DbTransactionExecutor create(DbSource source) {

    return this.executors.computeIfAbsent(source.getId(), k -> doCreate(source));
  }

  private DbTransactionExecutor doCreate(DbSource source) {

    for (DbTransactionExecutorProvider provider : this.providers) {
      DbTransactionExecutor executor = provider.create(source);
      if (executor != null) {
        return executor;
      }
    }
    throw new IllegalStateException("Could not create DbTransactionExecutor after trying " + this.providers.size()
        + " provider(s): " + this.providers);
  }

}
