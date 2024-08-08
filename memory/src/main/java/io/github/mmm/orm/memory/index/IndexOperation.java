/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.memory.index;

/**
 * Enum with the available operations on a {@link MemoryIndex}.
 */
public enum IndexOperation {

  /**
   * Add to index.
   *
   * @see MemoryIndex#add(Object, io.github.mmm.entity.bean.EntityBean)
   */
  ADD,

  /**
   * Update index.
   *
   * @see MemoryIndex#update(Object, io.github.mmm.entity.bean.EntityBean, Object)
   */
  UPDATE,

  /**
   * Remove from index.
   *
   * @see MemoryIndex#remove(Object)
   */
  REMOVE

}
