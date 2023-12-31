/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.typemapping;

import io.github.mmm.base.range.Range;
import io.github.mmm.binary.Streamable;
import io.github.mmm.entity.bean.typemapping.SingleTypeMapping;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.value.converter.IdentityTypeMapper;
import io.github.mmm.value.converter.TypeMapper;

/**
 * Implementation of {@link SingleTypeMapping} for a {@link Streamable}.
 *
 * @since 1.0.0
 */
public class SingleTypeMappingBinary extends SingleTypeMapping<Streamable> {

  private final IdentityTypeMapper<Streamable> typeMapper;

  private final String declarationVariable;

  /**
   * The constructor.
   *
   * @param declaration the SQL type for {@link Streamable}s of any length (e.g. "BLOB").
   * @param declarationVariable the SQL type for length limited {@link Streamable}s (e.g. "BLOB(%s)").
   */
  public SingleTypeMappingBinary(String declaration, String declarationVariable) {

    super();
    this.typeMapper = new IdentityTypeMapper<>(Streamable.class, declaration);
    this.declarationVariable = declarationVariable;
  }

  @Override
  public TypeMapper<Streamable, ?> getTypeMapper() {

    return this.typeMapper;
  }

  @Override
  public TypeMapper<Streamable, ?> getTypeMapper(ReadableProperty<?> property) {

    Range<? extends Number> range = getRange(property);
    Number max = range.getMax();
    if (max == null) {
      return this.typeMapper;
    } else {
      return new IdentityTypeMapper<>(Streamable.class, String.format(this.declarationVariable, max));
    }
  }

}
