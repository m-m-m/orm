package io.github.mmm.orm.impl;

import io.github.mmm.orm.dialect.DbContext;

/**
 * Implementation of {@link DbContext} to be used as fallback when no database is actually available.
 */
public class DbContextNone implements DbContext {

  /** The singleton instance. */
  public static final DbContextNone INSTANCE = new DbContextNone();

}
