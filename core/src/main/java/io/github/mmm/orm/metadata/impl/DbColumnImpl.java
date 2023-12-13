/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import io.github.mmm.orm.metadata.DbColumn;
import io.github.mmm.orm.metadata.DbName;

/**
 * Implementation of {@link DbColumn}.
 *
 * @since 1.0.0
 */
public class DbColumnImpl extends DbObjectWithNameImpl implements DbColumn {

  private final String comment;

  private final Boolean nullable;

  private final int typeCode;

  private final String typeName;

  private final int size;

  private final int decimalDigits;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param comment the {@link #getComment() comment}.
   * @param nullable the {@link #getNullable() nullable} flag.
   * @param typeCode the {@link #getTypeCode() type code}.
   * @param typeName the {@link #getTypeName() type name}.
   * @param size the {@link #getSize() size}.
   * @param decimalDigits the {@link #getDecimalDigits() decimal digits}.
   */
  public DbColumnImpl(DbName name, String comment, Boolean nullable, int typeCode, String typeName, int size,
      int decimalDigits) {

    super(name);
    this.comment = comment;
    this.nullable = nullable;
    this.typeCode = typeCode;
    this.typeName = typeName;
    this.size = size;
    this.decimalDigits = decimalDigits;
  }

  @Override
  public String getComment() {

    return this.comment;
  }

  @Override
  public Boolean getNullable() {

    return this.nullable;
  }

  @Override
  public int getTypeCode() {

    return this.typeCode;
  }

  @Override
  public String getTypeName() {

    return this.typeName;
  }

  @Override
  public int getSize() {

    return this.size;
  }

  @Override
  public int getDecimalDigits() {

    return this.decimalDigits;
  }

  @Override
  public void toString(StringBuilder sb, boolean detailed) {

    if (detailed) {
      sb.append("Column:");
    }
    sb.append(this.name);
    if (detailed) {
      sb.append(' ');
      sb.append(this.typeName);
      sb.append('(');
      sb.append(this.size);
      if (this.decimalDigits > 0) {
        sb.append(',');
        sb.append(this.decimalDigits);
      }
      sb.append(')');

      if (Boolean.TRUE.equals(this.nullable)) {
        sb.append(" nullable");
      } else if (Boolean.FALSE.equals(this.nullable)) {
        sb.append(" NOT null");
      }
    }
  }

}
