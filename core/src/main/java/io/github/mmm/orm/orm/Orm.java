/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.orm;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.criteria.ProjectionProperty;
import io.github.mmm.value.converter.AtomicTypeMapper;

/**
 * Interface for ORM (object/relational mapping).
 *
 * @since 1.0.0
 */
public interface Orm {

  /**
   * @param <B> type of the {@link WritableBean} to map.
   * @param bean the prototype of the {@link WritableBean} to map.
   * @return the {@link AtomicTypeMapper} to map from the {@link WritableBean} to {@link DbResult} and vice-versa.
   */
  default <B extends WritableBean> DbBeanMapper<B> createBeanMapping(B bean) {

    return createBeanMapping(bean, bean.getProperties());
  }

  /**
   * @param <B> type of the {@link WritableBean} to map.
   * @param bean the prototype of the {@link WritableBean} to map.
   * @param properties the {@link Iterable} with the explicit {@link WritableProperty properties} to map.
   * @return the {@link AtomicTypeMapper} to map from the {@link WritableBean} to {@link DbResult} and vice-versa.
   */
  <B extends WritableBean> DbBeanMapper<B> createBeanMapping(B bean,
      Iterable<? extends WritableProperty<?>> properties);

  /**
   * @param <B> type of the {@link WritableBean} to map.
   * @param bean the prototype of the {@link WritableBean} to map.
   * @param properties the {@link Iterable} with the {@link ProjectionProperty projection properties} to map.
   * @return the {@link AtomicTypeMapper} to map from the {@link WritableBean} to {@link DbResult} and vice-versa.
   */
  <B extends WritableBean> DbBeanMapper<B> createBeanMappingProjection(B bean,
      Iterable<? extends ProjectionProperty<?>> properties);

}
