/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.constraint;

import io.github.mmm.property.ReadableProperty;
import io.github.mmm.value.PropertyPath;

/**
 * Not null {@link DbConstraint} ensuring that the {@link #getColumns() column} cannot have a {@code null} value.
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
   * @param name the {@link #getName() name}.
   * @param column the (first) {@link #getColumns() column}.
   */
  public NotNullConstraint(String name, PropertyPath<?> column) {

    super(name, column);
  }

  /**
   * The constructor.
   *
   * @param column the (first) {@link #getColumns() column}.
   */
  public NotNullConstraint(ReadableProperty<?> column) {

    super(createName(PREFIX, column), column);
  }

  @Override
  public String getType() {

    return TYPE;
  }

}
