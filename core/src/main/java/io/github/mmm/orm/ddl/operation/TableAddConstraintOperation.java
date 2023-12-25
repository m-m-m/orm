package io.github.mmm.orm.ddl.operation;

import io.github.mmm.orm.ddl.constraint.DbConstraint;

/**
 * {@link TableConstraintOperation} to add a constraint.
 *
 * @since 1.0.0
 */
public class TableAddConstraintOperation extends TableConstraintOperation {

  /**
   * The constructor.
   *
   * @param constraint the {@link #getConstraint() constraint} to add.
   */
  public TableAddConstraintOperation(DbConstraint constraint) {

    super(constraint);
  }

  @Override
  public TableOperationType getType() {

    return TableOperationType.ADD;
  }

}
