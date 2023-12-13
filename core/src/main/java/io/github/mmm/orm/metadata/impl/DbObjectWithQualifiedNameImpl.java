/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import io.github.mmm.orm.metadata.DbObjectWithQualifiedName;
import io.github.mmm.orm.metadata.DbQualifiedName;

/**
 * Implementation of {@link DbObjectWithQualifiedName}.
 *
 * @since 1.0.0
 */
public abstract class DbObjectWithQualifiedNameImpl extends DbObjectImpl implements DbObjectWithQualifiedName {

  /** @see #getQualifiedName() */
  protected final DbQualifiedName qualifiedName;

  /**
   * The constructor.
   *
   * @param qualifiedName the {@link #getQualifiedName() qualified name}.
   */
  public DbObjectWithQualifiedNameImpl(DbQualifiedName qualifiedName) {

    super();
    this.qualifiedName = qualifiedName;
  }

  @Override
  public DbQualifiedName getQualifiedName() {

    return this.qualifiedName;
  }

}
