/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.session.impl;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.spi.session.AbstractDbEntitySession;
import io.github.mmm.orm.spi.session.DbEntityHolder;
import io.github.mmm.orm.spi.session.DbSession;

/**
 * Default implementation of {@link io.github.mmm.orm.spi.session.DbEntitySession}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 *
 * @see DbSession#get(EntityBean)
 * @since 1.0.0
 */
public class DbEntitySessionDefault<E extends EntityBean> extends AbstractDbEntitySession<E> {

  /**
   * The constructor.
   */
  public DbEntitySessionDefault() {

    super();
  }

  @Override
  protected DbEntityHolder<E> createHolder(E managed) {

    return new DbEntityHolderStatic<>(managed);
  }

}
