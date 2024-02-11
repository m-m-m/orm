/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

/**
 * {@link DbConstraint} state for deferrable status.
 *
 * @since 1.0.0
 */
public enum DbConstraintDeferrable {

  /** Unspecified, let the database decide (typically {@link #NOT_DEFERRABLE not deferrable}). */
  DEFAULT(""),

  /**
   * Make the {@link DbConstraint} deferrable. Instead of checking the constraint with every statement (e.g.
   * INSERT/UPDATE), deferred constraints are validated as batch operation at the end of the commit. This may also be
   * needed for {@link ForeignKeyConstraint} in case of cyclic relations. Please note that this is not supported by all
   * databases for all kind of {@link DbConstraint constraints}. E.g. MS SQL server does not support it at all and
   * PostGreSQL does not support it for {@link NotNullConstraint} and {@link CheckConstraint}. As the latter has some
   * severe impact on indexes also in case they are supported (e.g. in Oracle), it is generally a good idea to avoid
   * deferring for these kind of constraints.
   */
  DEFERRABLE("DEFERRABLE"),

  /** Explicitly make the {@link DbConstraint} not deferrable. */
  NOT_DEFERRABLE("NOT DEFERRABLE");

  private final String syntax;

  private DbConstraintDeferrable(String syntax) {

    this.syntax = syntax;
  }

  @Override
  public String toString() {

    return this.syntax;
  }

}
