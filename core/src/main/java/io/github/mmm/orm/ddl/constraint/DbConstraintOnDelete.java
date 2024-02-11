/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

/**
 * {@link DbConstraint} state for behavior of {@link ForeignKeyConstraint} in case of deletion.
 *
 * @since 1.0.0
 */
public enum DbConstraintOnDelete {

  /** Unspecified, let the database decide (typically fail on delete if still referenced). */
  DEFAULT(""),

  /**
   * Follow the {@link ForeignKeyConstraint foreign key} and delete the referenced row automatically. This may be useful
   * for relations to child objects that cannot live without their parent but it can also be dangerous to use.
   */
  ON_DELETE_CASCASE("ON DELETE CASCADE"),

  /**
   * Set the {@link ForeignKeyConstraint foreign key} to {@code null} automatically to be able to delete the referenced
   * row.
   */
  ON_DELETE_SET_NULL("ON DELETE SET NULL");

  private final String syntax;

  private DbConstraintOnDelete(String syntax) {

    this.syntax = syntax;
  }

  @Override
  public String toString() {

    return this.syntax;
  }

}
