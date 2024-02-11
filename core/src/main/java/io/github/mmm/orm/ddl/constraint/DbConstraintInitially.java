/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

/**
 * {@link DbConstraint} state for initially status.
 *
 * @since 1.0.0
 */
public enum DbConstraintInitially {

  /** Unspecified, let the database decide (typically {@link #INITIALLY_IMMEDIATE initially immediate}). */
  DEFAULT(""),

  /**
   * Make the {@link DbConstraint} initially deferred. For database support see
   * {@link DbConstraintDeferrable#DEFERRABLE}.
   */
  INITIALLY_DEFERRED("INITIALLY DEFERRED"),

  /** Explicitly make the {@link DbConstraint} initially immediate. */
  INITIALLY_IMMEDIATE("INITIALLY IMMEDIATE");

  private final String syntax;

  private DbConstraintInitially(String syntax) {

    this.syntax = syntax;
  }

  @Override
  public String toString() {

    return this.syntax;
  }

}
