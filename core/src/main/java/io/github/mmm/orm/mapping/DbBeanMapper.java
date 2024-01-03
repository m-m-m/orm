/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping;

import io.github.mmm.bean.WritableBean;

/**
 * {@link DbMapper} to map a {@link WritableBean} to the database and vice-versa.
 *
 * @param <B> type of the {@link WritableBean} to map.
 * @since 1.0.0
 */
public interface DbBeanMapper<B extends WritableBean> extends DbMapper<B> {

}
