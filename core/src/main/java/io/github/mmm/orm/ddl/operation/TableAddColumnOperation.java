package io.github.mmm.orm.ddl.operation;

import io.github.mmm.orm.ddl.DbColumnSpec;

/**
 * {@link TableColumnOperation} to add a column.
 *
 * @since 1.0.0
 */
public class TableAddColumnOperation extends TableColumnOperation {

  /**
   * The constructor.
   *
   * @param column the {@link #getColumn() column} to add.
   */
  public TableAddColumnOperation(DbColumnSpec column) {

    super(column);
  }

  @Override
  public TableOperationType getType() {

    return TableOperationType.ADD;
  }

}
