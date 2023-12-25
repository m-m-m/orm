package io.github.mmm.orm.ddl.operation;

import io.github.mmm.orm.ddl.DbColumnSpec;

/**
 * {@link TableColumnOperation} to drop a column.
 *
 * @since 1.0.0
 */
public class TableDropColumnOperation extends TableColumnOperation {

  /**
   * The constructor.
   *
   * @param column the {@link #getColumn() column} to drop.
   */
  public TableDropColumnOperation(DbColumnSpec column) {

    super(column);
  }

  @Override
  public TableOperationType getType() {

    return TableOperationType.DROP;
  }

}
