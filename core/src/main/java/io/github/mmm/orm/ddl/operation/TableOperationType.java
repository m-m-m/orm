/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.operation;

/**
 * Enum with the available types of a {@link TableOperation}.
 *
 * @see TableOperation#getType()
 */
public enum TableOperationType {

  /** Type to add an object (column or constraint). */
  ADD,

  /** Type to drop (remove) an object (column or constraint). */
  DROP,

  /** Type to rename an object (column or constraint). */
  RENAME,

  /** Type to modify an object (e.g. change column type). */
  MODIFY;

}
