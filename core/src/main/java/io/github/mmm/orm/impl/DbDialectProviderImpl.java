/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.collection.ReadOnlyIterator;
import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.dialect.DbDialectProvider;

/**
 * Implementation of {@link DbDialectProvider}.
 *
 * @since 1.0.0
 */
public class DbDialectProviderImpl implements DbDialectProvider {

  /** The singleton instance. */
  public static final DbDialectProvider INSTANCE = new DbDialectProviderImpl();

  private static final Logger LOG = LoggerFactory.getLogger(DbDialectProviderImpl.class);

  private final Map<String, AbstractDbDialect<?>> dialects;

  /**
   * The constructor.
   */
  public DbDialectProviderImpl() {

    super();
    this.dialects = new HashMap<>();
    ServiceLoader<DbDialect> loader = ServiceLoader.load(DbDialect.class);
    for (DbDialect dialect : loader) {
      register((AbstractDbDialect<?>) dialect);
    }
  }

  private void register(AbstractDbDialect<?> dialect) {

    AbstractDbDialect<?> duplicate = this.dialects.put(dialect.getId(), dialect);
    if (duplicate != null) {
      LOG.info("Overriding dialect " + dialect.getId() + " from " + duplicate.getClass().getName() + " to "
          + dialect.getClass().getName());
    }
  }

  @Override
  public DbDialect get(String name) {

    DbDialect dialect = this.dialects.get(name);
    if (dialect == null) {
      throw new ObjectNotFoundException("DbDialect", name);
    }
    return dialect;
  }

  @Override
  public boolean has(String name) {

    return this.dialects.containsKey(name);
  }

  @Override
  public DbDialect getByDbUrl(String url) {

    for (AbstractDbDialect<?> dialect : this.dialects.values()) {
      if (dialect.isResponsible(url)) {
        return dialect;
      }
    }
    throw new ObjectNotFoundException("DbDialect", url);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public Iterator<DbDialect> iterator() {

    return new ReadOnlyIterator<>((Iterator) this.dialects.values());
  }

}
