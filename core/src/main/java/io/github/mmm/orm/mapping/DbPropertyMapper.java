/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping;

import java.util.Iterator;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.orm.result.DbResultRow;
import io.github.mmm.orm.result.DbResultCell;
import io.github.mmm.orm.result.DbResultRowPojo;
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
   * @param dbResult the {@link DbResultRowPojo} where to
   *        {@link DbResultRowPojo#addCell(io.github.mmm.orm.result.DbResultCell) add} the collected
   *        {@link io.github.mmm.orm.result.DbResultCell database data}.
   */
  void java2db(ReadableBean javaBean, DbResultRowPojo dbResult);

  /**
   * @param javaProperty the {@link ReadableProperty} to map.
   * @param dbResult the {@link DbResultRowPojo} where to
   *        {@link DbResultRowPojo#addCell(io.github.mmm.orm.result.DbResultCell) add} the collected
   *        {@link io.github.mmm.orm.result.DbResultCell database data}.
   */
  default void java2db(ReadableProperty<V> javaProperty, DbResultRowPojo dbResult) {

    V value = null;
    if (javaProperty != null) {
      javaProperty.get();
    }
    java2dbValue(value, dbResult);
  }

  /**
   * @param javaValue the Java value to map.
   * @param dbResult the {@link DbResultRowPojo} where to
   *        {@link DbResultRowPojo#addCell(io.github.mmm.orm.result.DbResultCell) add} the collected
   *        {@link io.github.mmm.orm.result.DbResultCell database data}.
   */
  void java2dbValue(V javaValue, DbResultRowPojo dbResult);

  /**
   * @param dbCellIterator the {@link Iterator} of the {@link DbResultCell database entries} received from the
   *        database to convert to Java.
   * @param javaBean the {@link WritableBean} where to map the {@link DbResultRow} to.
   */
  void db2java(Iterator<DbResultCell<?>> dbCellIterator, WritableBean javaBean);

}
