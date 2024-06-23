/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.repository;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.bean.SoftDeletableEntity;
import io.github.mmm.orm.repository.operation.EntityDeleteOperations;
import io.github.mmm.orm.repository.operation.EntityFindOperations;

/**
 * {@link EntityRepository} for {@link SoftDeletableEntity}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public interface SoftDeletableEntityRepository<E extends SoftDeletableEntity> extends EntityRepository<E> {

  /**
   * By default operations like {@link #delete(EntityBean)} will only perform logical deletion for a
   * {@link SoftDeletableEntity}. This method gives access to the physical delete operations.
   *
   * @return {@link EntityDeleteOperations} for physical deletion operations.
   */
  EntityDeleteOperations<E> deletePhysical();

  /**
   * By default operations like {@link #findById(io.github.mmm.entity.id.Id)} will not find {@link SoftDeletableEntity}
   * that are {@link SoftDeletableEntity#Deleted() logically deleted}. This method gives access to find operations that
   * also include logically deleted entities.
   *
   * @return the {@link EntityFindOperations} that include {@link SoftDeletableEntity#Deleted() logically deleted}
   *         entities.
   */
  EntityFindOperations<E> findIncludingDeleted();

}
