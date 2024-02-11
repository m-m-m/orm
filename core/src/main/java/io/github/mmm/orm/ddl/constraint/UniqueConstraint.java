/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

import io.github.mmm.orm.ddl.DbColumnSpec;

/**
 * Unique {@link DbConstraint} ensuring that all values in the {@link #iterator() contained} {@link DbColumnSpec
 * columns} are different (no duplicates).
 *
 * @since 1.0.0
 */
public final class UniqueConstraint extends DbConstraint {

  /** {@link #getType() Type} {@value}. */
  public static final String TYPE = "FOREIGN KEY";

  /** Suggested prefix for the {@link #getName() name}: {@value}. */
  public static final String PREFIX = "UQ_";

  /**
   * The constructor.
   *
   * @param columns the {@link DbColumnSpec columns}.
   */
  public UniqueConstraint(DbColumnSpec... columns) {

    this(null, columns);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param columns the {@link DbColumnSpec columns}.
   */
  public UniqueConstraint(String name, DbColumnSpec... columns) {

    this(name, DbConstraintState.DEFAULT, columns);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param state the {@link #getState() state}.
   * @param columns the {@link DbColumnSpec columns}.
   */
  public UniqueConstraint(String name, DbConstraintState state, DbColumnSpec... columns) {

    super(name, state, columns);
  }

  @Override
  public String getNamePrefix() {

    return PREFIX;
  }

  @Override
  public String getType() {

    return TYPE;
  }

  @Override
  public UniqueConstraint withState(DbConstraintState newState) {

    if (this.state == newState) {
      return this;
    }
    return new UniqueConstraint(this.name, newState, this.columns);
  }

}
