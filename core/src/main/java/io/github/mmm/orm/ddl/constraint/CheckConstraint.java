/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.criteria.CriteriaPredicate;
import io.github.mmm.value.CriteriaObject;

/**
 * Foreign key {@link DbConstraint} uniquely identifying a different {@link io.github.mmm.entity.bean.EntityBean entity}
 * (row from another table).
 *
 * @since 1.0.0
 */
public final class CheckConstraint extends DbConstraint {

  /** {@link #getType() Type} {@value}. */
  public static final String TYPE = "CHECK";

  /** Suggested prefix for the {@link #getName() name}: {@value}. */
  public static final String PREFIX = "CK_";

  private final CriteriaPredicate predicate;

  /**
   * The constructor.
   *
   * @param predicate the {@link #getPredicate() predicate} to check.
   */
  public CheckConstraint(CriteriaPredicate predicate) {

    this(null, getColumn(predicate), predicate, DbConstraintState.DEFAULT);
  }

  /**
   * The constructor.
   *
   * @param predicate the {@link #getPredicate() predicate} to check.
   * @param state the {@link #getState() state}.
   */
  public CheckConstraint(CriteriaPredicate predicate, DbConstraintState state) {

    this(null, getColumn(predicate), predicate, state);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param predicate the {@link #getPredicate() predicate} to check.
   */
  public CheckConstraint(String name, CriteriaPredicate predicate) {

    this(name, predicate, DbConstraintState.DEFAULT);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param predicate the {@link #getPredicate() predicate} to check.
   * @param state the {@link #getState() state}.
   */
  public CheckConstraint(String name, CriteriaPredicate predicate, DbConstraintState state) {

    this(name, getColumn(predicate), predicate, state);
  }

  private CheckConstraint(String name, DbColumnSpec column, CriteriaPredicate predicate, DbConstraintState state) {

    this(name, state, predicate, column);
  }

  private CheckConstraint(String name, DbConstraintState state, CriteriaPredicate predicate, DbColumnSpec... columns) {

    super(name, state, columns);
    this.predicate = predicate;
  }

  private static DbColumnSpec getColumn(CriteriaPredicate predicate) {

    return new DbColumnSpec(getColumnProperty(predicate));
  }

  private static ReadableProperty<?> getColumnProperty(CriteriaPredicate predicate) {

    CriteriaObject<?> arg = predicate.getFirstArg();
    if (arg instanceof ReadableProperty<?> property) {
      return property;
    } else if (arg instanceof CriteriaPredicate cp) {
      return getColumnProperty(cp);
    } else {
      arg = predicate.getSecondArg();
      if (arg instanceof ReadableProperty<?> property) {
        return property;
      }
    }
    throw new IllegalStateException("Could not determine column property from predicate: " + predicate);
  }

  /**
   * @return the {@link CriteriaPredicate} to check.
   */
  public CriteriaPredicate getPredicate() {

    return this.predicate;
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
  public CheckConstraint withState(DbConstraintState newState) {

    if (newState == this.state) {
      return this;
    }
    return new CheckConstraint(this.name, newState, this.predicate, this.columns);
  }

  @Override
  protected void toStringColumns(StringBuilder sb) {

    sb.append(this.predicate.toString());
  }

}
