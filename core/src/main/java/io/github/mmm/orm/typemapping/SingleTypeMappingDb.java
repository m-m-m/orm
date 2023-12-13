/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.typemapping;

import io.github.mmm.entity.bean.typemapping.SingleTypeMapping;
import io.github.mmm.orm.type.DbType;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.value.converter.TypeMapper;

/**
 * Abstract base implementation of {@link SingleTypeMapping} based on {@link DbType}.
 *
 * @param <V> type of the {@link TypeMapper#getSourceType() source type}.
 * @since 1.0.0
 */
public abstract class SingleTypeMappingDb<V> extends SingleTypeMapping<V> {

  @Override
  public abstract DbType<V, ?> getTypeMapper();

  @Override
  public abstract DbType<V, ?> getTypeMapper(ReadableProperty<?> property);

}
