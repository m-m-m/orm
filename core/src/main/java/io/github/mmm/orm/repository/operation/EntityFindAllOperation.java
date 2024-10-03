/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.repository.operation;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.repository.EntityRepository;

/**
 * Interface for the {@link #findAll()} operation of an {@link EntityRepository}. By default this operation is not
 * supported or visible since regular repositories are backed by very large tables and loading all data leads to
 * performance disasters.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public interface EntityFindAllOperation<E extends EntityBean> {

  /**
   * @return an {@link Iterable} with all entities of this repository.
   */
  Iterable<E> findAll();

}
