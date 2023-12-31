/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.typemapping;

import io.github.mmm.base.range.Range;
import io.github.mmm.entity.bean.typemapping.SingleTypeMapping;
import io.github.mmm.orm.type.DbTypeString;
import io.github.mmm.property.ReadableProperty;

/**
 * Implementation of {@link SingleTypeMapping} for a {@link String}.
 *
 * @since 1.0.0
 */
public class SingleTypeMappingString extends SingleTypeMapping<String> {

  private final DbTypeString typeMapper;

  private final String sqlTypeVariable;

  private final String sqlTypeFixed;

  /**
   * The constructor.
   *
   * @param declarationAny the SQL type for {@link String}s of any length (e.g. "TEXT" or "VARCHAR(MAX)").
   * @param declarationVariable the SQL type for length limited {@link String}s (e.g. "VARCHAR(%s)" or "VARCHAR2(%s
   *        CHAR)").
   * @param declarationFixed the SQL type for fixed length {@link String}s (e.g. "CHAR(%s)").
   */
  public SingleTypeMappingString(String declarationAny, String declarationVariable, String declarationFixed) {

    super();
    this.typeMapper = new DbTypeString(declarationAny);
    this.sqlTypeVariable = declarationVariable;
    this.sqlTypeFixed = declarationFixed;
  }

  @Override
  public DbTypeString getTypeMapper() {

    return this.typeMapper;
  }

  @Override
  public DbTypeString getTypeMapper(ReadableProperty<?> property) {

    Range<Integer> range = getRange(property);
    Integer max = range.getMax();
    if (max == null) {
      return this.typeMapper;
    } else {
      Integer min = range.getMin();
      if (max.equals(min)) {
        return new DbTypeString(String.format(this.sqlTypeFixed, max));
      }
      return new DbTypeString(String.format(this.sqlTypeVariable, max));
    }
  }

}
