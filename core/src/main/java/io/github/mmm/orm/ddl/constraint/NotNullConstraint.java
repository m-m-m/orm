/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

import io.github.mmm.orm.ddl.DbColumnSpec;

/**
 * Not null {@link DbConstraint} ensuring that the {@link #iterator() contained} {@link DbColumnSpec columns} cannot have
 * a {@code null} value.
 *
 * @since 1.0.0
 */
public final class NotNullConstraint extends DbConstraint {

  /** {@link #getType() Type} {@value}. */
  public static final String TYPE = "NOT NULL";

  /** Suggested prefix for the {@link #getName() name}: {@value}. */
  public static final String PREFIX = "NN_";

  /**
   * The constructor.
   *
   * @param column the {@link DbColumnSpec column}.
   */
  public NotNullConstraint(DbColumnSpec column) {

    this(null, column);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param column the {@link DbColumnSpec column}.
   */
  public NotNullConstraint(String name, DbColumnSpec column) {

    super(name, column);
  }

  @Override
  public String getNamePrefix() {

    return PREFIX;
  }

  @Override
  public String getType() {

    return TYPE;
  }

}
