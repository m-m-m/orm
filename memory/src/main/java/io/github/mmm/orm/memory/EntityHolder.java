package io.github.mmm.orm.memory;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.entity.bean.EntityBean;

/**
 * Holder for internal and external {@link EntityBean}.
 */
final class EntityHolder<E extends EntityBean> {

  final E internalEntity;

  final E externalEntity;

  /**
   * The constructor.
   *
   * @param internalEntity the {@link #getInternalEntity() internal entity}.
   * @param externalEntity the {@link #getExternalEntity() external entity}.
   */
  EntityHolder(E internalEntity, E externalEntity) {

    super();
    this.internalEntity = internalEntity;
    this.externalEntity = externalEntity;
  }

  /**
   * @return internalEntity the {@link EntityBean} that is kept internal and never passed to the outside world.
   *         Therefore only the {@link #getExternalEntity() external entity} can be modified externally. Changes are
   *         only applied when the external entity gets saved.
   */
  E getInternalEntity() {

    return this.internalEntity;
  }

  /**
   * @return externalEntity the {@link EntityBean} that passed to the outside world.
   */
  E getExternalEntity() {

    return this.externalEntity;
  }

  void verifyExternalEntity(E entity) {

    assert (entity == this.externalEntity);
  }

  static <E extends EntityBean> EntityHolder<E> of(E entity, boolean internal) {

    E copy = ReadableBean.copy(entity);
    return new EntityHolder<>(copy, entity);
  }

}
