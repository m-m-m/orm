/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.dialect;

/**
 * Interface for database contextual data and operations.
 */
public interface DbContext {

  /**
   * @return the prefix to qualify database objects like tables, views, sequences, etc.
   */
  default String getQualifierPrefix() {

    return "";
  }
}
