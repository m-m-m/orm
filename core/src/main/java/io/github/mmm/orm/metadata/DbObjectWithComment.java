/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

/**
 * {@link DbObject} having a {@link #getComment() comment}.
 *
 * @see DbTable
 * @see DbColumn
 */
public interface DbObjectWithComment extends DbObject {

  /**
   * @return the optional comment. Will be {@link String#isEmpty() empty} if undefined but never {@code null}.
   */
  String getComment();

}
