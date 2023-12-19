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
import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.service.ServiceHelper;
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
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public DbDialectProviderImpl() {

    super();
    this.dialects = new HashMap<>();
    ServiceLoader<AbstractDbDialect<?>> serviceLoader = (ServiceLoader) ServiceLoader.load(DbDialect.class);
    ServiceHelper.all(serviceLoader, this.dialects, AbstractDbDialect::getId);
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

    String type = getType(url);
    if (type != null) {
      DbDialect dialect = null;
      for (AbstractDbDialect<?> currentDialect : this.dialects.values()) {
        if (type.equals(currentDialect.getType())) {
          if (dialect == null) {
            dialect = currentDialect;
          } else {
            throw new DuplicateObjectException("DbDialect", dialect.getId(), currentDialect.getId());
          }
        }
      }
      if (dialect != null) {
        return dialect;
      }
    }
    throw new ObjectNotFoundException("DbDialect", url);
  }

  private String getType(String url) {

    int start = url.indexOf(':');
    if (start > 0) {
      start++;
      int end = url.indexOf(':', start);
      if (end > start) {
        return url.substring(start, end);
      }
    }
    LOG.warn("Could not determine database type from connection URL '{}'", url);
    return null;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public Iterator<DbDialect> iterator() {

    return new ReadOnlyIterator<>((Iterator) this.dialects.values().iterator());
  }

}
