/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.sync;

import io.github.mmm.entity.bean.EntityBean;

/**
 * Interface to synchronize the Java {@link EntityBean}-model with the DDL of your database.<br>
 * It will build the diff of your {@link EntityBean}-model and your DDL-models. All columns missing in one of the models
 * will be created in the other. So unlike hibernate hbm2dd auto feature it will synchronize bidirectional and also
 * creates properties in case of {@link EntityBean#isDynamic() dynamic} and {@link io.github.mmm.bean.VirtualBean
 * virtual} {@link EntityBean}s.<br>
 * <b>ATTENTION:</b><br>
 * This feature is designed only for development and should not be used in production systems.
 */
public interface ModelSynchronizer {

  /**
   * Synchronizes all {@link EntityBean}s managed by a {@link io.github.mmm.entity.repository.EntityRepository
   * repository} that currently have an open transaction.
   */
  void sync();

  /**
   * @param entityClass the {@link Class} reflection the {@link EntityBean} to synchronize with the database (DDL).
   * @throws RuntimeException if no transaction is open for the according {@link io.github.mmm.orm.source.DbSource
   *         database source}.
   */
  void sync(Class<? extends EntityBean> entityClass);

  /**
   * @param entity the {@link EntityBean} to synchronize with the database (DDL).
   * @throws RuntimeException if no transaction is open for the according {@link io.github.mmm.orm.source.DbSource
   *         database source}.
   */
  void sync(EntityBean entity);

}
