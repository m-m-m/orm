package io.github.mmm.orm.spi.session;

import io.github.mmm.bean.BeanHelper;
import io.github.mmm.entity.bean.EntityBean;

/**
 * Abstract base implementation of {@link DbEntityHolder}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public abstract class AbstractDbEntityHolder<E extends EntityBean> implements DbEntityHolder<E> {

  /** @see #getInternal() */
  protected final E internal;

  /**
   * The constructor.
   *
   * @param internal the {@link #getInternal() internal entity}.
   */
  public AbstractDbEntityHolder(E internal) {

    super();
    this.internal = internal;
  }

  @Override
  public E getInternal() {

    return this.internal;
  }

  @Override
  public void update(E entity) {

    // TODO only copy non-transient properties...
    BeanHelper.copy(entity, this.internal);
  }

}
