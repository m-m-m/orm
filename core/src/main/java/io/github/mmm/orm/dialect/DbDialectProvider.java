/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.dialect;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.orm.impl.DbDialectProviderImpl;

/**
 * Provider for {@link DbDialect}s.
 *
 * @since 1.0.0
 */
public interface DbDialectProvider extends Iterable<DbDialect> {

  /**
   * @param id the {@link DbDialect#getId() identifier} of the requested {@link DbDialect}.
   * @return the requested {@link DbDialect}.
   * @throws ObjectNotFoundException if no {@link DbDialect} could be found for the given {@code name}.
   */
  DbDialect get(String id);

  /**
   * @param url the database connection URL (e.g. JDBC URL).
   * @return the {@link DbDialect} that is responsible for this URL.
   * @throws ObjectNotFoundException if no {@link DbDialect} could be found for the given {@code url}.
   */
  DbDialect getByDbUrl(String url);

  /**
   * @param name the {@link DbDialect#getId() name} of the {@link DbDialect} to find.
   * @return {@code true} if the {@link DbDialect} with the given {@link DbDialect#getId() name} is present,
   *         {@code false} otherwise.
   */
  boolean has(String name);

  /**
   * @return the singleton instance of this {@link DbDialectProvider}.
   */
  static DbDialectProvider get() {

    return DbDialectProviderImpl.INSTANCE;
  }

}
