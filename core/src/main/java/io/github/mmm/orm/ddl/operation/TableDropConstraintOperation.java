package io.github.mmm.orm.ddl.operation;

import io.github.mmm.orm.ddl.constraint.DbConstraint;

/**
 * {@link TableConstraintOperation} to drop a constraint.
 *
 * @since 1.0.0
 */
public class TableDropConstraintOperation extends TableConstraintOperation {

  /**
   * The constructor.
   *
   * @param name the name of the {@link #getConstraint() constraint} to drop.
   */
  public TableDropConstraintOperation(String name) {

    super(name);
  }

  /**
   * The constructor.
   *
   * @param constraint the {@link #getConstraint() constraint} to drop.
   */
  public TableDropConstraintOperation(DbConstraint constraint) {

    super(constraint);
  }

  @Override
  public TableOperationType getType() {

    return TableOperationType.DROP;
  }

}
