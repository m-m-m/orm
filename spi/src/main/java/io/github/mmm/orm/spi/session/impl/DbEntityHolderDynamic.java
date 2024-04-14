package io.github.mmm.orm.spi.session.impl;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.spi.session.AbstractDbEntityHolder;
import io.github.mmm.orm.spi.session.DbEntityHolder;

/**
 * Implementation of {@link DbEntityHolder} with static {@link #getExternal() external entity}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public class DbEntityHolderDynamic<E extends EntityBean> extends AbstractDbEntityHolder<E> {

  private final boolean readOnly;

  /**
   * The constructor.
   *
   * @param managed the {@link #getManaged() managed entity}.
   * @param readOnly to create read-only copies for {@link #getExternal() external entities}, {@code false} otherwise
   *        (mutable copies).
   */
  public DbEntityHolderDynamic(E managed, boolean readOnly) {

    super(managed);
    this.readOnly = readOnly;
  }

  @Override
  public E getExternal() {

    return ReadableBean.copy(this.managed, this.readOnly);
  }

}
