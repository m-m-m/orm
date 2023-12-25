package io.github.mmm.orm.ddl.operation;

import io.github.mmm.orm.ddl.DbColumnSpec;

/**
 * {@link TableColumnOperation} to modify a column (change its type).
 *
 * @since 1.0.0
 */
public class TableModifyColumnOperation extends TableColumnOperation {

  /**
   * The constructor.
   *
   * @param column the {@link #getColumn() column} to modify.
   */
  public TableModifyColumnOperation(DbColumnSpec column) {

    super(column);
  }

  @Override
  public TableOperationType getType() {

    return TableOperationType.MODIFY;
  }

}
