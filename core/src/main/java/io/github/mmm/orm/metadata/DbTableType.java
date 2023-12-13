/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

import java.util.Objects;

/**
 * Name of a database object (schema, table, column, etc.).
 */
public final class DbTableType {

  /** {@link DbTableType} for a regular {@link DbTable database table}. */
  public static final DbTableType TABLE = new DbTableType("TABLE");

  /** {@link DbTableType} for a view that selects from other {@link DbTable}(s). */
  public static final DbTableType VIEW = new DbTableType("VIEW");

  /** {@link DbTableType} for a synonym that imports a {@link DbTable} from a different catalog/schema. */
  public static final DbTableType SYNONYM = new DbTableType("SYNONYM");

  private static final DbTableType[] STANDARD_TYPES = { TABLE, VIEW, SYNONYM };

  private final String type;

  private DbTableType(String type) {

    super();
    Objects.requireNonNull(type);
    this.type = type;
  }

  /**
   * @return the type as {@link String}.
   */
  public String get() {

    return this.type;
  }

  /**
   * @return {@code true} for a regular {@link #TABLE}, {@code false} otherwise.
   */
  public boolean isTable() {

    return (this == TABLE);
  }

  /**
   * @return {@code true} for a {@link #VIEW}, {@code false} otherwise.
   */
  public boolean isView() {

    return (this == VIEW);
  }

  /**
   * @return {@code true} for a {@link #SYNONYM}, {@code false} otherwise.
   */
  public boolean isSynonym() {

    return (this == TABLE);
  }

  @Override
  public int hashCode() {

    return this.type.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    } else if ((obj == null) || (obj.getClass() != DbTableType.class)) {
      return false;
    }
    DbTableType other = (DbTableType) obj;
    return this.type.equals(other.type);
  }

  @Override
  public String toString() {

    return this.type;
  }

  /**
   * @param type the {@link #get() type} as {@link String}.
   * @return the new {@link DbTableType} or {@code null} if the given {@link String} was {@code null} or
   *         {@link String#isBlank() blank}.
   */
  public static DbTableType of(String type) {

    if (type == null) {
      return null;
    }
    if (type.isEmpty()) {
      return null;
    }
    for (DbTableType standardType : STANDARD_TYPES) {
      if (type.equalsIgnoreCase(standardType.type)) {
        return standardType;
      }
    }
    return new DbTableType(type);
  }

}
