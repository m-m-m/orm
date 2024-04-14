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

  /** @see #getManaged() */
  protected final E managed;

  /**
   * The constructor.
   *
   * @param managed the {@link #getManaged() managed entity}.
   */
  public AbstractDbEntityHolder(E managed) {

    super();
    this.managed = managed;
  }

  @Override
  public E getManaged() {

    return this.managed;
  }

  @Override
  public void update(E entity) {

    // TODO only copy non-transient properties...
    BeanHelper.copy(entity, this.managed);
  }

}
