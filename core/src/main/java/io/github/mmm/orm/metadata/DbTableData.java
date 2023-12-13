/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

/**
 * Meta-information about a database table (or view, materialized view, etc.).
 *
 * @since 1.0.0
 */
public interface DbTableData extends DbObjectWithQualifiedName, DbObjectWithComment {

  /**
   * @return the {@link DbTableType} such as {@link DbTableType#TABLE}, {@link DbTableType#VIEW}, etc.
   */
  DbTableType getType();

}
