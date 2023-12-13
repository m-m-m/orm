/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

/**
 * Interface for a container with {@link DbObject}s of a specific kind.
 *
 * @param <O> type of the contained {@link DbObject}s.
 */
public interface DbObjectContainer<O extends DbObject> extends Iterable<O> {

  /**
   * @param name the {@link DbName} with the name of the requested object.
   * @return the {@link DbObject} from this container {@link DbObject#getName() having} the given {@code name} or
   *         {@code null} if no such object exists.
   */
  O get(DbName name);

}
