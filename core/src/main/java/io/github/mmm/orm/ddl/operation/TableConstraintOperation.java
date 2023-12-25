package io.github.mmm.orm.ddl.operation;

import java.util.Objects;

import io.github.mmm.orm.ddl.constraint.DbConstraint;

/**
 * {@link TableOperation} on a {@link DbConstraint constraint}.
 *
 * @since 1.0.0
 */
public abstract class TableConstraintOperation extends TableOperation {

  private final String name;

  private final DbConstraint constraint;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() constraint name}.
   */
  public TableConstraintOperation(String name) {

    super();
    Objects.requireNonNull(name);
    this.name = name;
    this.constraint = null;
  }

  /**
   * The constructor.
   *
   * @param constraint the #getConstraint()
   */
  public TableConstraintOperation(DbConstraint constraint) {

    super();
    this.name = constraint.getName();
    this.constraint = constraint;
  }

  /**
   * @return the name of the constraint to operate on.
   */
  public String getName() {

    return this.name;
  }

  /**
   * @return the {@link DbConstraint} to operate on. May be {@code null} if only the name is given (e.g. in case of a
   *         drop constraint operation).
   */
  public DbConstraint getConstraint() {

    return this.constraint;
  }

  @Override
  public TableOperationKind getKind() {

    return TableOperationKind.CONSTRAINT;
  }

  @Override
  public String toString() {

    return getType().name() + " CONSTRAINT " + this.name;
  }

}
