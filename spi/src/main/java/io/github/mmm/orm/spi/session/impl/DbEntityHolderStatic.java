package io.github.mmm.orm.spi.session.impl;

import io.github.mmm.bean.BeanHelper;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.spi.session.AbstractDbEntityHolder;
import io.github.mmm.orm.spi.session.DbEntityHolder;

/**
 * Implementation of {@link DbEntityHolder} with static {@link #getExternal() external entity}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public class DbEntityHolderStatic<E extends EntityBean> extends AbstractDbEntityHolder<E> {

  private final E external;

  /**
   * The constructor.
   *
   * @param managed the {@link #getManaged() managed entity}.
   * @param external the {@link #getExternal() external entity}.
   */
  public DbEntityHolderStatic(E managed, E external) {

    super(managed);
    this.external = external;
  }

  @Override
  public E getExternal() {

    return this.external;
  }

  @Override
  public void update(E entity) {

    super.update(entity);
    if (!this.external.isReadOnly()) {
      // TODO only copy non-transient properties...
      BeanHelper.copy(entity, this.external);
    }
  }

}
