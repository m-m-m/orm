/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import java.util.Iterator;
import java.util.Objects;

import io.github.mmm.base.collection.ArrayIterator;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbObject;
import io.github.mmm.orm.metadata.DbObjectContainer;

/**
 * Implementation of {@link DbObjectContainer}.
 *
 * @param <O> type of the contained {@link DbObject}s.
 * @since 1.0.0
 */
public final class DbObjectContainerImplList<O extends DbObject> extends DbObjectContainerImpl<O> {

  private final O[] children;

  /**
   * The constructor.
   *
   * @param children the contained {@link DbObject}s.
   */
  @SafeVarargs
  public DbObjectContainerImplList(O... children) {

    super();
    this.children = children;
  }

  @Override
  public Iterator<O> iterator() {

    return new ArrayIterator<>(this.children);
  }

  @Override
  public O get(DbName name) {

    for (O child : this.children) {
      if (Objects.equals(child.getName(), name)) {
        return child;
      }
    }
    return null;
  }

}
