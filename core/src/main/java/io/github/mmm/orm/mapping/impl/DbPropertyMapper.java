/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping.impl;

import java.util.Iterator;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.orm.result.DbResultValue;
import io.github.mmm.orm.result.impl.DbResultPojo;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;

/**
 * Interface to map a {@link WritableProperty property} to the database and vice versa.
 *
 * @param <V> type of the {@link WritableProperty#get() value} to map.
 * @since 1.0.0
 */
public interface DbPropertyMapper<V> {

  /**
   * @return the {@link ReadableProperty#getName() property name}.
   */
  String getPropertyName();

  /**
   * @param javaBean the {@link ReadableBean} to map.
   * @param dbResult the {@link DbResultPojo} where to {@link DbResultPojo#add(io.github.mmm.orm.result.DbResultValue)
   *        add} the collected {@link io.github.mmm.orm.result.DbResultValue database values}.
   */
  void java2db(ReadableBean javaBean, DbResultPojo dbResult);

  /**
   * @param javaProperty the {@link ReadableProperty} to map.
   * @param dbResult the {@link DbResultPojo} where to {@link DbResultPojo#add(io.github.mmm.orm.result.DbResultValue)
   *        add} the collected {@link io.github.mmm.orm.result.DbResultValue database values}.
   */
  default void java2db(ReadableProperty<V> javaProperty, DbResultPojo dbResult) {

    V value = null;
    if (javaProperty != null) {
      javaProperty.get();
    }
    java2dbValue(value, dbResult);
  }

  /**
   * @param javaValue the Java value to map.
   * @param dbResult the {@link DbResultPojo} where to {@link DbResultPojo#add(io.github.mmm.orm.result.DbResultValue)
   *        add} the collected {@link io.github.mmm.orm.result.DbResultValue database values}.
   */
  void java2dbValue(V javaValue, DbResultPojo dbResult);

  /**
   * @param dbIterator the {@link Iterator} of the {@link DbResultValue database values} to convert to Java.
   * @param javaBean the {@link WritableBean} where to map the {@link DbResultValue}(s) to.
   */
  void db2java(Iterator<DbResultValue<?>> dbIterator, WritableBean javaBean);

}
