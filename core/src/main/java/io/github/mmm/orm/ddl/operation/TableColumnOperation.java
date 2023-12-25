package io.github.mmm.orm.ddl.operation;

import io.github.mmm.orm.ddl.DbColumnSpec;

/**
 * {@link TableOperation} on a {@link DbColumnSpec column}.
 *
 * @since 1.0.0
 */
public abstract class TableColumnOperation extends TableOperation {

  private final DbColumnSpec column;

  /**
   * The constructor.
   *
   * @param column the {@link #getColumn() column} to alter.
   */
  public TableColumnOperation(DbColumnSpec column) {

    super();
    this.column = column;
  }

  /**
   * @return the {@link DbColumnSpec column} to alter according to the {@link #getType() type}.
   */
  public DbColumnSpec getColumn() {

    return this.column;
  }

  @Override
  public TableOperationKind getKind() {

    return TableOperationKind.COLUMN;
  }

  @Override
  public String toString() {

    return getType().name() + " COLUMN " + this.column.getName();
  }

}
