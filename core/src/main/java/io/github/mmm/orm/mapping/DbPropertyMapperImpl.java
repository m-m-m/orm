/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping;

import java.util.Iterator;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.orm.result.DbResultCell;
import io.github.mmm.orm.result.DbResultRowPojo;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;

/**
 * Implementation of {@link DbPropertyMapper}.
 *
 * @param <V> type of the {@link WritableProperty#get() value} to map.
 * @since 1.0.0
 */
public class DbPropertyMapperImpl<V> implements DbPropertyMapper<V> {

  private final String propertyName;

  private final DbSegmentMapper<V, ?> valueMapper;

  /**
   * The constructor.
   *
   * @param propertyName the {@link ReadableProperty#getName() property name} to map.
   * @param valueMapper the {@link DbSegmentMapper} with the underlying complexity of the mapping.
   */
  public DbPropertyMapperImpl(String propertyName, DbSegmentMapper<V, ?> valueMapper) {

    super();
    this.propertyName = propertyName;
    this.valueMapper = valueMapper;
  }

  @Override
  public String getPropertyName() {

    return this.propertyName;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void java2db(ReadableBean javaBean, DbResultRowPojo dbResult) {

    java2db((ReadableProperty<V>) javaBean.getProperty(this.propertyName), dbResult);
  }

  @Override
  public void java2db(ReadableProperty<V> javaProperty, DbResultRowPojo dbResult) {

    V value = null;
    if (javaProperty != null) {
      value = javaProperty.get();
    }
    java2dbValue(value, dbResult);
  }

  @Override
  public void java2dbValue(V javaValue, DbResultRowPojo dbResult) {

    this.valueMapper.java2db(javaValue, dbResult);
  }

  @Override
  public void db2java(Iterator<DbResultCell<?>> dbEntryIterator, WritableBean javaBean) {

    V value = this.valueMapper.db2java(dbEntryIterator, null);
    javaBean.setDynamic(this.propertyName, value);
  }

  @Override
  public String toString() {

    return this.propertyName;
  }

}
