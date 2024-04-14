package io.github.mmm.orm.spi.access;

import io.github.mmm.orm.source.DbSource;

/**
 * Provider of {@link DbAccess}.
 *
 * @since 1.0.0
 */
public interface DbAccessProvider {

  /**
   * @param source the {@link DbSource}.
   * @return the corresponding {@link DbAccess}.
   */
  DbAccess create(DbSource source);

}
