/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.operation;

/**
 * Enum with the available kinds of a {@link TableOperation}.
 *
 * @see TableOperation#getKind()
 */
public enum TableOperationKind {

  /** Operation kind for {@link io.github.mmm.orm.ddl.DbColumnSpec column}. */
  COLUMN,

  /** Operation kind for {@link io.github.mmm.orm.ddl.constraint.DbConstraint constraint}. */
  CONSTRAINT;

}
