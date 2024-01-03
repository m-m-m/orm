/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.orm.result.DbResultCell;
import io.github.mmm.orm.result.DbResultRow;
import io.github.mmm.orm.result.DbResultRowPojo;
import io.github.mmm.value.converter.TypeMapper;

/**
 * Implementation of {@link TypeMapper} to map from {@link WritableBean} to {@link DbResultRow} and vice-versa.
 *
 * @param <B> type of the {@link WritableBean} to map.
 * @since 1.0.0
 */
public class DbBeanMapperImpl<B extends WritableBean> implements DbBeanMapper<B> {

  private final B bean;

  private final List<DbPropertyMapper<?>> propertyMappers;

  /**
   * The constructor.
   *
   * @param bean the {@link WritableBean} template.
   */
  public DbBeanMapperImpl(B bean) {

    super();
    this.bean = bean;
    this.propertyMappers = new ArrayList<>(bean.getProperties().size());
  }

  /**
   * @param propertyMapper the {@link DbPropertyMapper} to add.
   */
  public void add(DbPropertyMapper<?> propertyMapper) {

    assert (this.bean.getProperty(propertyMapper.getPropertyName()) != null);
    this.propertyMappers.add(propertyMapper);
  }

  @Override
  public DbResultRow java2db(B source) {

    DbResultRowPojo dbResult = new DbResultRowPojo();
    for (DbPropertyMapper<?> mapper : this.propertyMappers) {
      mapper.java2db(source, dbResult);
    }
    return dbResult;
  }

  @Override
  public B db2java(DbResultRow dbResult) {

    B resultBean = ReadableBean.copy(this.bean);
    Iterator<DbResultCell<?>> cellIterator = dbResult.getCells().iterator();
    for (DbPropertyMapper<?> mapper : this.propertyMappers) {
      mapper.db2java(cellIterator, resultBean);
    }
    return resultBean;
  }

}
