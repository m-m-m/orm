/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import java.util.Collections;
import java.util.Iterator;

import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbObject;
import io.github.mmm.orm.metadata.DbObjectContainer;

/**
 * Implementation of {@link DbObjectContainer}.
 *
 * @param <O> type of the contained {@link DbObject}s.
 * @since 1.0.0
 */
public final class DbObjectContainerImplEmpty<O extends DbObject> extends DbObjectContainerImpl<O> {

  private static final DbObjectContainerImplEmpty<DbObject> INSTANCE = new DbObjectContainerImplEmpty<>();

  /**
   * The constructor.
   */
  private DbObjectContainerImplEmpty() {

    super();
  }

  @Override
  public Iterator<O> iterator() {

    return Collections.emptyIterator();
  }

  @Override
  public O get(DbName name) {

    return null;
  }

  /**
   * @param <O> type of the contained {@link DbObject}s.
   * @return the singleton instance of {@link DbObjectContainerImplEmpty}.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <O extends DbObject> DbObjectContainerImplEmpty<O> get() {

    return (DbObjectContainerImplEmpty) INSTANCE;
  }

}
