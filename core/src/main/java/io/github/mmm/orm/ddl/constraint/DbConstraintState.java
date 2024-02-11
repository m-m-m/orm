/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

/**
 * State of a {@link DbConstraint} as the combination of {@link DbConstraintDeferrable}, {@link DbConstraintInitially},
 * and {@link DbConstraintRely}.
 *
 * @since 1.0.0
 */
public final class DbConstraintState {

  private static final DbConstraintState[] STATES = new DbConstraintState[27];

  /** The default state. */
  public static final DbConstraintState DEFAULT = of(DbConstraintDeferrable.DEFAULT, DbConstraintInitially.DEFAULT,
      DbConstraintRely.DEFAULT);

  private final DbConstraintDeferrable deferrable;

  private final DbConstraintInitially initially;

  private final DbConstraintRely rely;

  private final String syntax;

  private DbConstraintState(DbConstraintDeferrable deferrable, DbConstraintInitially initially, DbConstraintRely rely) {

    super();
    this.deferrable = deferrable;
    this.initially = initially;
    this.rely = rely;
    StringBuilder sb = new StringBuilder();
    sb.append(deferrable.toString());
    if (initially != DbConstraintInitially.DEFAULT) {
      if (sb.length() > 0) {
        sb.append(' ');
      }
      sb.append(initially.toString());
    }
    if (rely != DbConstraintRely.DEFAULT) {
      if (sb.length() > 0) {
        sb.append(' ');
      }
      sb.append(rely.toString());
    }
    this.syntax = sb.toString();
  }

  /**
   * @return the {@link DbConstraintDeferrable deferrable} status.
   */
  public DbConstraintDeferrable getDeferrable() {

    return this.deferrable;
  }

  /**
   * @return the {@link DbConstraintInitially initially} status.
   */
  public DbConstraintInitially getInitially() {

    return this.initially;
  }

  /**
   * @return the {@link DbConstraintRely rely} status.
   */
  public DbConstraintRely getRely() {

    return this.rely;
  }

  /**
   * @param deferrable the {@link #getDeferrable() deferrable} state.
   * @param initially the {@link #getInitially() initially} state.
   * @param rely the {@link #getRely() rely} state.
   * @return the {@link DbConstraintState} composed out of the given parameters.
   */
  public static DbConstraintState of(DbConstraintDeferrable deferrable, DbConstraintInitially initially,
      DbConstraintRely rely) {

    if (deferrable == null) {
      deferrable = DbConstraintDeferrable.DEFAULT;
    }
    if (initially == null) {
      initially = DbConstraintInitially.DEFAULT;
    }
    if (rely == null) {
      rely = DbConstraintRely.DEFAULT;
    }
    int index = deferrable.ordinal() * 9 + initially.ordinal() * 3 + rely.ordinal();
    DbConstraintState result = STATES[index];
    if (result == null) {
      result = new DbConstraintState(deferrable, initially, rely);
      STATES[index] = result;
    }
    return result;
  }

  @Override
  public String toString() {

    return this.syntax;
  }

}
