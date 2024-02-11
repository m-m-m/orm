/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

import io.github.mmm.orm.ddl.DbColumnSpec;

/**
 * Foreign key {@link DbConstraint} uniquely identifying a different {@link io.github.mmm.entity.bean.EntityBean entity}
 * (row from another table).
 *
 * @since 1.0.0
 */
public final class PrimaryKeyConstraint extends DbConstraint {

  /** {@link #getType() Type} {@value}. */
  public static final String TYPE = "PRIMARY KEY";

  /** Suggested prefix for the {@link #getName() name}: {@value}. */
  public static final String PREFIX = "PK_";

  /**
   * The constructor.
   *
   * @param column the {@link DbColumnSpec column}.
   */
  public PrimaryKeyConstraint(DbColumnSpec column) {

    this(null, column);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param column the {@link DbColumnSpec column}.
   */
  public PrimaryKeyConstraint(String name, DbColumnSpec column) {

    this(name, column, DbConstraintState.DEFAULT);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param column the {@link DbColumnSpec column}.
   * @param state the {@link #getState() state}.
   */
  public PrimaryKeyConstraint(String name, DbColumnSpec column, DbConstraintState state) {

    this(name, state, column);
  }

  private PrimaryKeyConstraint(String name, DbConstraintState state, DbColumnSpec... columns) {

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
  protected boolean isAddColumnToName() {

    return false;
  }

  @Override
  public PrimaryKeyConstraint withState(DbConstraintState newState) {

    if (this.state == newState) {
      return this;
    }
    return new PrimaryKeyConstraint(this.name, newState, this.columns);
  }

}
