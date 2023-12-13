/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbObject;

/**
 * Implementation of {@link DbObject}.
 *
 * @since 1.0.0
 */
public abstract class DbObjectWithNameImpl extends DbObjectImpl implements DbObject {

  /** @see #getName() */
  protected final DbName name;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public DbObjectWithNameImpl(DbName name) {

    super();
    this.name = name;
  }

  @Override
  public DbName getName() {

    return this.name;
  }

}
