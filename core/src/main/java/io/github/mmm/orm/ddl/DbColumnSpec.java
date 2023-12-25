/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.naming.DbNamingStrategy;
import io.github.mmm.property.ReadableProperty;

/**
 * {@link DbElement} for a database column.
 */
public class DbColumnSpec extends DbElement {

  /** Empty {@link DbColumnSpec} array. */
  public static final DbColumnSpec[] NO_COLUMNS = {};

  private final ReadableProperty<?> property;

  private final DbTableSpec table;

  private final String declaration;

  /**
   * The constructor.
   *
   * @param property the {@link #getProperty() property}.
   */
  public DbColumnSpec(ReadableProperty<?> property) {

    this(null, property, null, null);
  }

  /**
   * The constructor.
   *
   * @param property the {@link #getProperty() property}.
   * @param table the {@link #getTable() table}.
   */
  public DbColumnSpec(ReadableProperty<?> property, DbTableSpec table) {

    this(null, property, table, null);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param property the {@link #getProperty() property}.
   * @param table the {@link #getTable() table}.
   */
  public DbColumnSpec(String name, ReadableProperty<?> property, DbTableSpec table) {

    this(name, property, table, null);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param property the {@link #getProperty() property}.
   * @param table the {@link #getTable() table}.
   * @param declaration the explicit {@link #getDeclaration() column declaration} (database specific).
   */
  public DbColumnSpec(String name, ReadableProperty<?> property, DbTableSpec table, String declaration) {

    super(name);
    this.property = property;
    if ((table == null) && (property != null)) {
      this.table = DbTableSpec.of((EntityBean) property.getMetadata().getLock());
    } else {
      this.table = table;
    }
    this.declaration = declaration;
  }

  /**
   * @return the associated {@link ReadableProperty property} represented as column.
   */
  public ReadableProperty<?> getProperty() {

    return this.property;
  }

  /**
   * @return the {@link DbTableSpec} owning this column.
   */
  public DbTableSpec getTable() {

    return this.table;
  }

  /**
   * @return the column declaration (type). Typically {@code null} to derive it automatically from the
   *         {@link #getProperty() property} {@link ReadableProperty#getValueClass() type}.
   */
  public String getDeclaration() {

    return this.declaration;
  }

  @Override
  public String createName(DbNamingStrategy namingStrategy) {

    return namingStrategy.getColumnName(this.property);
  }

}
