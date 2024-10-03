package io.github.mmm.orm.spi.session;

import io.github.mmm.entity.bean.EntityBean;

/**
 * Interface for a holder of the {@link #getInternal() internal entity} from the first-level cache together with its
 * {@link #getExternal() external reference}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public interface DbEntityHolder<E extends EntityBean> {

  /**
   * @return the internal {@link EntityBean} from the first-level cache. It is mutable and must strictly be kept
   *         internal and never passed to the outside world (via public API). Implementors have to protected it with
   *         their life.
   */
  E getInternal();

  /**
   * @return the external {@link EntityBean#getReadOnly() read-only view} of the {@link #getInternal() internal} entity.
   */
  E getExternal();

  /**
   * Updates the entity/entities in this holder.
   *
   * @param entity the new entity that has been updated.
   */
  void update(E entity);

}
