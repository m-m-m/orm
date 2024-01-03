/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.repository;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.repository.EntityRepository;
import io.github.mmm.orm.spi.repository.impl.EntityRepositoryManagerImpl;

/**
 * Manager of all {@link EntityRepository repositories}.
 *
 * @see EntityRepository
 * @since 1.0.0
 */
public interface EntityRepositoryManager extends Iterable<EntityRepository<?>> {

  /**
   * @param <E> type of the managed {@link EntityBean}.
   * @param entityClass the {@link Class} reflecting the {@link EntityBean} to get the {@link EntityRepository} for.
   * @return the requested {@link EntityRepository} or {@code null} if not found.
   */
  <E extends EntityBean> EntityRepository<E> getRepository(Class<E> entityClass);

  /**
   * @param <E> type of the managed {@link EntityBean}.
   * @param entity the {@link EntityBean} (prototype) to get the {@link EntityRepository} for.
   * @return the requested {@link EntityRepository} or {@code null} if not found.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  default <E extends EntityBean> EntityRepository<E> getRepository(E entity) {

    Class<E> entityClass = (Class) entity.getJavaClass();
    return getRepository(entityClass);
  }

  /**
   * @param <E> type of the managed {@link EntityBean}.
   * @param entity the {@link EntityBean} to get the {@link EntityRepository} for.
   * @return the requested {@link EntityRepository}.
   * @throws ObjectNotFoundException if the requested {@link EntityRepository} does not exist.
   */
  default <E extends EntityBean> EntityRepository<E> getRepositoryRequired(E entity) throws ObjectNotFoundException {

    EntityRepository<E> repository = getRepository(entity);
    if (repository == null) {
      throw new ObjectNotFoundException(EntityRepository.class.getSimpleName(), entity.getJavaClass());
    }
    return repository;
  }

  /**
   * @return the {@link EntityRepositoryManager} instance.
   */
  static EntityRepositoryManager get() {

    return EntityRepositoryManagerImpl.INSTANCE;
  }

}
