/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

/**
 * Meta-information about a column of a {@link DbTable table}.
 *
 * @since 1.0.0
 */
public interface DbColumn extends DbObjectWithComment {

  /**
   * @return {@link Boolean#TRUE} if nullable, {@link Boolean#FALSE} if value cannot be {@code null}, and {@code null}
   *         if unknown.
   */
  Boolean getNullable();

  /**
   * @return the type of this column as {@link java.sql.Types SQL type constant}.
   */
  int getTypeCode();

  /**
   * @return the type of this column as {@link String}.
   */
  String getTypeName();

  /**
   * @return the size of this column (e.g. the maximum length of a string).
   */
  int getSize();

  /**
   * @return the number of decimal digits for numeric values. Will be {@code 0} for values that are not numeric.
   */
  int getDecimalDigits();
}
