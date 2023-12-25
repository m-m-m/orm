package io.github.mmm.orm.ddl.operation;

import io.github.mmm.orm.statement.alter.AlterTableOperations;

/**
 * A single {@link TableOperation} of {@link AlterTableOperations}.
 *
 * @since 1.0.0
 */
public abstract class TableOperation {

  /**
   * @return the {@link TableOperationType type} of this {@link TableOperation operation}.
   */
  public abstract TableOperationType getType();

  /**
   * @return the {@link TableOperationKind kind} of object this {@link TableOperation operation} is about.
   */
  public abstract TableOperationKind getKind();
}
