/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import io.github.mmm.base.collection.ReadOnlyIterator;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbObject;
import io.github.mmm.orm.metadata.DbObjectContainer;

/**
 * Implementation of {@link DbObjectContainer}.
 *
 * @param <O> type of the contained {@link DbObject}s.
 * @since 1.0.0
 */
public final class DbObjectContainerImplMap<O extends DbObject> extends DbObjectContainerImpl<O> {

  private final Map<DbName, O> map;

  /**
   * The constructor.
   *
   * @param children the contained {@link DbObject}s.
   */
  @SafeVarargs
  public DbObjectContainerImplMap(O... children) {

    super();
    this.map = new HashMap<>(children.length);
    for (O child : children) {
      this.map.put(child.getName(), child);
    }
  }

  /**
   * The constructor.
   *
   * @param map the ready to use {@link Map} with the contained {@link DbObject}s.
   */
  public DbObjectContainerImplMap(Map<DbName, O> map) {

    super();
    Objects.requireNonNull(map);
    this.map = map;
  }

  @Override
  public Iterator<O> iterator() {

    return new ReadOnlyIterator<>(this.map.values().iterator());
  }

  @Override
  public O get(DbName name) {

    return this.map.get(name);
  }

}
