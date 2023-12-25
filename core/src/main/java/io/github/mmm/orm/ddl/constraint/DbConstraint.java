/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

import java.util.Iterator;

import io.github.mmm.base.collection.ArrayIterator;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.ddl.DbElement;
import io.github.mmm.orm.ddl.DbTableSpec;
import io.github.mmm.orm.naming.DbNamingStrategy;

/**
 * Represents a database constraint on one or multiple columns.
 *
 * @since 1.0.0
 */
public abstract class DbConstraint extends DbElement implements Iterable<DbColumnSpec> {

  private final DbColumnSpec[] columns;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param columns the {@link DbColumnSpec columns}.
   */
  public DbConstraint(String name, DbColumnSpec... columns) {

    super(name);
    assert (columns.length > 0);
    this.columns = columns;
  }

  @Override
  public String createName(DbNamingStrategy namingStrategy) {

    StringBuilder sb = new StringBuilder();
    sb.append(getNamePrefix());
    DbColumnSpec column = this.columns[0];
    DbTableSpec table = column.getTable();
    sb.append(table.createName(namingStrategy));
    if (isAddColumnToName()) {
      sb.append('_');
      sb.append(column.createName(namingStrategy));
    }
    return sb.toString();
  }

  /**
   * @return {@code true} if the name of the first column shall be added to the {@link #createName(DbNamingStrategy)
   *         constraint name}.
   */
  protected boolean isAddColumnToName() {

    return true;
  }

  /**
   * @return the name prefix (e.g. "FK_" for foreign key).
   */
  public abstract String getNamePrefix();

  /**
   * @return the type of this {@link DbConstraint} (e.g. "CHECK" or "FOREIGN KEY").
   */
  public abstract String getType();

  /**
   * @return the first {@link DbColumnSpec column}. To get all columns {@link #iterator() iterate} over the
   *         {@link DbConstraint}.
   */
  public DbColumnSpec getFirstColumn() {

    return this.columns[0];
  }

  @Override
  public Iterator<DbColumnSpec> iterator() {

    return new ArrayIterator<>(this.columns);
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder(32);
    toString(sb);
    return sb.toString();
  }

  /**
   * @param sb the {@link StringBuilder} to append to.
   */
  protected void toString(StringBuilder sb) {

    sb.append("CONSTRAINT ");
    sb.append(getName());
    sb.append(' ');
    sb.append(getType());
    toStringColumns(sb);
  }

  /**
   * @param sb the {@link StringBuilder} to append to.
   */
  protected void toStringColumns(StringBuilder sb) {

    sb.append(" (");
    String s = "";
    for (DbColumnSpec column : this.columns) {
      sb.append(s);
      sb.append(column.getName());
      s = ", ";
    }
    sb.append(')');
  }

}
