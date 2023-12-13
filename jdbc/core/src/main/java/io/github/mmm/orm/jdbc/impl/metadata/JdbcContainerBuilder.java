/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.impl.metadata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.mmm.orm.metadata.DbObject;
import io.github.mmm.orm.metadata.DbObjectContainer;
import io.github.mmm.orm.metadata.impl.DbObjectContainerImplEmpty;
import io.github.mmm.orm.metadata.impl.DbObjectContainerImplList;
import io.github.mmm.orm.metadata.impl.DbObjectContainerImplSingle;

/**
 * Builder for {@link DbObjectContainer}.
 *
 * @param <O> type of the {@link DbObject}s contained in the {@link DbObjectContainer} to build.
 */
public class JdbcContainerBuilder<O extends DbObject> {

  private O child;

  private List<O> children;

  /**
   * @param newChild the new {@link DbObjectContainer#iterator() child element} to add.
   */
  public void add(O newChild) {

    Objects.requireNonNull(newChild);
    if (this.children == null) {
      if (this.child == null) {
        this.child = newChild;
        return;
      }
      this.children = new ArrayList<>();
      this.children.add(this.child);
    }
    this.children.add(newChild);
  }

  /**
   * @return {@code true} if this {@link #add(DbObject)} has never been called and the container is empty, {@code false}
   *         otherwise.
   */
  public boolean isEmpty() {

    return (this.child == null) && (this.children == null);
  }

  /**
   * @return the new {@link DbObjectContainer} with all the children that have been {@link #add(DbObject) added}.
   */
  @SuppressWarnings("unchecked")
  public DbObjectContainer<O> build() {

    if (this.children == null) {
      if (this.child == null) {
        return DbObjectContainerImplEmpty.get();
      }
      return new DbObjectContainerImplSingle<>(this.child);
    } else {
      O[] array = (O[]) Array.newInstance(this.child.getClass(), this.children.size());
      return new DbObjectContainerImplList<>(this.children.toArray(array));
    }
  }

}
