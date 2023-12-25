package io.github.mmm.orm.ddl.operation;

import io.github.mmm.orm.ddl.DbColumnSpec;

/**
 * {@link TableColumnOperation} to rename a column.
 *
 * @since 1.0.0
 */
public class TableRenameColumnOperation extends TableColumnOperation {

  private final DbColumnSpec newColumn;

  /**
   * The constructor.
   *
   * @param column the {@link #getColumn() column} to rename.
   * @param newColumn the {@link #getNewColumn() new column} to rename to.
   */
  public TableRenameColumnOperation(DbColumnSpec column, DbColumnSpec newColumn) {

    super(column);
    this.newColumn = newColumn;
  }

  /**
   * @return the new {@link DbColumnSpec column} to rename to.
   */
  public DbColumnSpec getNewColumn() {

    return this.newColumn;
  }

  @Override
  public TableOperationType getType() {

    return TableOperationType.RENAME;
  }

  @Override
  public String toString() {

    return super.toString() + " TO " + this.newColumn.getName();
  }

}
