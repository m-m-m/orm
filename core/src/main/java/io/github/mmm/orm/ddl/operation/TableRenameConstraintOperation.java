package io.github.mmm.orm.ddl.operation;

import io.github.mmm.orm.ddl.constraint.DbConstraint;

/**
 * {@link TableColumnOperation} to rename a constraint.
 *
 * @since 1.0.0
 */
public class TableRenameConstraintOperation extends TableConstraintOperation {

  private final String newName;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name of the constraint} to rename.
   * @param newName the {@link #getNewName() new name}.
   */
  public TableRenameConstraintOperation(String name, String newName) {

    super(name);
    this.newName = newName;
  }

  /**
   * The constructor.
   *
   * @param constraint the {@link #getConstraint() constraint} to rename.
   * @param newName the {@link #getNewName() new name}.
   */
  public TableRenameConstraintOperation(DbConstraint constraint, String newName) {

    super(constraint);
    this.newName = newName;
  }

  /**
   * @return the new name to rename the constraint to.
   */
  public String getNewName() {

    return this.newName;
  }

  @Override
  public TableOperationType getType() {

    return TableOperationType.RENAME;
  }

  @Override
  public String toString() {

    return super.toString() + " TO " + this.newName;
  }

}
