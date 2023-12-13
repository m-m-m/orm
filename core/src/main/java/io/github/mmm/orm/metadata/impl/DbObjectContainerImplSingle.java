/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import java.util.Iterator;
import java.util.Objects;

import io.github.mmm.base.collection.SingleElementIterator;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbObject;
import io.github.mmm.orm.metadata.DbObjectContainer;

/**
 * Implementation of {@link DbObjectContainer}.
 *
 * @param <O> type of the contained {@link DbObject}s.
 * @since 1.0.0
 */
public final class DbObjectContainerImplSingle<O extends DbObject> extends DbObjectContainerImpl<O> {

  private final O child;

  /**
   * The constructor.
   *
   * @param child the only contained {@link DbObject}.
   */
  public DbObjectContainerImplSingle(O child) {

    super();
    Objects.requireNonNull(child);
    this.child = child;
  }

  @Override
  public Iterator<O> iterator() {

    return new SingleElementIterator<>(this.child);
  }

  @Override
  public O get(DbName name) {

    if (this.child.getName().equals(name)) {
      return this.child;
    }
    return null;
  }

}
