/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.orm.mapping.DbBeanMapper;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;
import io.github.mmm.orm.result.impl.DbResultPojo;

/**
 * Implementation of {@link DbBeanMapper}.
 *
 * @param <B> type of the {@link WritableBean} to map.
 * @since 1.0.0
 */
public class DbBeanMapperImpl<B extends WritableBean> extends ComposableDbMapper<B> implements DbBeanMapper<B> {

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
  public DbResult java2db(B source) {

    // TODO compute size as capacity
    DbResultPojo dbResult = new DbResultPojo();
    for (DbPropertyMapper<?> mapper : this.propertyMappers) {
      mapper.java2db(source, dbResult);
    }
    return dbResult;
  }

  @Override
  public B db2java(Iterator<DbResultValue<?>> dbIterator) {

    B resultBean = ReadableBean.copy(this.bean);
    for (DbPropertyMapper<?> mapper : this.propertyMappers) {
      mapper.db2java(dbIterator, resultBean);
    }
    return resultBean;
  }

}
