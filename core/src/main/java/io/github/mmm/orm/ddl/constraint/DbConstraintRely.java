/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

/**
 * {@link DbConstraint} state for rely status. It specifies whether a constraint in NOVALIDATE mode is to be taken into
 * account for query rewrite.
 *
 * @since 1.0.0
 */
public enum DbConstraintRely {

  /** Unspecified, let the database decide (typically {@link #NORELY norely}). */
  DEFAULT(""),

  /**
   * Make the {@link DbConstraint constraint} rely activate it in NOVALIDATE mode for query rewrite in an unenforced
   * query rewrite integrity mode.
   */
  RELY("RELY"),

  /** Explicitly make the {@link DbConstraint} initially immediate. */
  NORELY("NORELY");

  private final String syntax;

  private DbConstraintRely(String syntax) {

    this.syntax = syntax;
  }

  @Override
  public String toString() {

    return this.syntax;
  }

}
