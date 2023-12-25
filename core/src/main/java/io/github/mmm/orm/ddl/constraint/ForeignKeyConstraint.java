/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl.constraint;

import io.github.mmm.entity.property.id.IdProperty;
import io.github.mmm.orm.ddl.DbColumnSpec;

/**
 * Foreign key {@link DbConstraint} uniquely identifying a different {@link io.github.mmm.entity.bean.EntityBean entity}
 * (row from another table).
 *
 * @since 1.0.0
 */
public final class ForeignKeyConstraint extends DbConstraint {

  /** {@link #getType() Type} {@value}. */
  public static final String TYPE = "FOREIGN KEY";

  /** Suggested prefix for the {@link #getName() name}: {@value}. */
  public static final String PREFIX = "FK_";

  private final DbColumnSpec referenceColumn;

  /**
   * The constructor.
   *
   * @param column the {@link DbColumnSpec column}.
   * @param referenceColumn the {@link #getReferenceColumn() referenced column}.
   */
  public ForeignKeyConstraint(DbColumnSpec column, DbColumnSpec referenceColumn) {

    this(null, column, referenceColumn);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param column the {@link DbColumnSpec column}.
   * @param referenceColumn the {@link #getReferenceColumn() referenced column}.
   */
  public ForeignKeyConstraint(String name, DbColumnSpec column, DbColumnSpec referenceColumn) {

    super(name, column);
    this.referenceColumn = referenceColumn;
  }

  /**
   * @return the name of the referenced {@link IdProperty primary key column}.
   */
  public DbColumnSpec getReferenceColumn() {

    return this.referenceColumn;
  }

  @Override
  public String getNamePrefix() {

    return PREFIX;
  }

  @Override
  public String getType() {

    return TYPE;
  }

  @Override
  protected void toString(StringBuilder sb) {

    super.toString(sb);
    sb.append(" REFERENCES ");
    sb.append(this.referenceColumn.getTable().getName());
    sb.append('(');
    sb.append(this.referenceColumn.getName());
    sb.append(')');
  }

}
