/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.session.impl;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.spi.session.AbstractDbEntitySession;
import io.github.mmm.orm.spi.session.DbEntityHolder;
import io.github.mmm.orm.spi.session.DbSession;

/**
 * Session data for a specific {@link EntityBean#getType() type} of {@link EntityBean}. Represents the internal
 * first-level cache and shall not be exposed externally.
 *
 * @param <E> type of the managed {@link EntityBean}.
 *
 * @see DbSession#get(EntityBean)
 * @since 1.0.0
 */
public class DbEntitySessionImplCopy<E extends EntityBean> extends AbstractDbEntitySession<E> {

  private final boolean readOnly;

  /**
   * The constructor.
   *
   * @param readOnly to create read-only copies for {@link DbEntityHolder#getExternal() external entities},
   *        {@code false} otherwise (mutable copies).
   */
  public DbEntitySessionImplCopy(boolean readOnly) {

    super();
    this.readOnly = readOnly;
  }

  @Override
  protected DbEntityHolder<E> createHolder(E managed) {

    return new DbEntityHolderDynamic<>(managed, this.readOnly);
  }

}
