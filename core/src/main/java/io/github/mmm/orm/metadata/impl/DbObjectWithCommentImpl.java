/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import io.github.mmm.orm.metadata.DbObjectWithComment;
import io.github.mmm.orm.metadata.DbObjectWithQualifiedName;
import io.github.mmm.orm.metadata.DbQualifiedName;

/**
 * Implementation of {@link DbObjectWithComment} and {@link DbObjectWithQualifiedName}.
 *
 * @since 1.0.0
 */
public abstract class DbObjectWithCommentImpl extends DbObjectWithQualifiedNameImpl implements DbObjectWithComment {

  /** @see #getComment() */
  protected final String comment;

  /**
   * The constructor.
   *
   * @param qName the {@link #getQualifiedName() qualified name}.
   * @param comment the {@link #getComment() comment}.
   */
  public DbObjectWithCommentImpl(DbQualifiedName qName, String comment) {

    super(qName);
    if (comment == null) {
      this.comment = "";
    } else {
      this.comment = comment;
    }
  }

  @Override
  public String getComment() {

    return this.comment;
  }

}
