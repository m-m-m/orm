/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.link.Link;

/**
 * Interface for a repository that gives access to {@link #save(EntityBean) save}, {@link #delete(EntityBean) delete} an
 * {@link EntityBean}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public interface EntityRepository<E extends EntityBean> {

  /**
   * @return the {@link Class} reflecting the managed {@link EntityBean}.
   */
  Class<E> getEntityClass();

  /**
   * @param id the {@link Id} of the requested {@link EntityBean entity}.
   * @return the requested {@link EntityBean entity} or {@code null} if no such entity exists.
   */
  E findById(Id<E> id);

  /**
   * @param link the {@link Link} to the requested {@link EntityBean entity}.
   * @return the requested {@link EntityBean entity} or {@code null} if no such entity exists.
   */
  default E findByLink(Link<E> link) {

    if (link == null) {
      return null;
    }
    // even if link is resolved, we load the entity by id
    // check link.isResolved() yourself before calling this method.
    return findById(link.getId());
  }

  /**
   * @param entity the {@link EntityBean} to save. If transient, it will be inserted, otherwise it will be updated.
   * @return the new or updated {@link Id} of the saved {@link EntityBean}.
   */
  Id<E> save(E entity);

  /**
   * @param entities the array of {@link EntityBean entities} to {@link #save(EntityBean) save}.
   */
  @SuppressWarnings("unchecked")
  default void saveAll(E... entities) {

    // this is a naive default implementation. Real database backed implementations shall override with bulk operation.
    for (E entity : entities) {
      save(entity);
    }
  }

  /**
   * @param entities the {@link Iterable} of {@link EntityBean entities} to {@link #save(EntityBean) save}.
   */
  default void saveAll(Iterable<E> entities) {

    // this is a naive default implementation. Real database backed implementations shall override with bulk operation.
    for (E entity : entities) {
      save(entity);
    }
  }

  /**
   * @param entity the {@link EntityBean} to remove from this repository.
   * @return {@code true} if the given {@link EntityBean entity} has been successfully deleted from this repository,
   *         {@code false} otherwise (was not {@link #save(EntityBean) saved} before).
   */
  default boolean delete(E entity) {

    return deleteById(Id.from(entity));
  }

  /**
   * @param id the {@link Id} (primary key) of the {@link EntityBean entity} to delete.
   * @return {@code true} if the entity with the given {@link Id} has been successfully deleted from this repository,
   *         {@code false} otherwise (no entity is persistent with this {@link Id}).
   */
  boolean deleteById(Id<E> id);

  /**
   * @param link the {@link Link} to the {@link EntityBean entity} to delete.
   * @return {@code true} if the entity for the given {@link Link} has been successfully deleted from this repository,
   *         {@code false} otherwise (no entity is persistent with this {@link Id}).
   */
  default boolean deleteByLink(Link<E> link) {

    if (link == null) {
      return false;
    }
    if (link.isResolved()) {
      return delete(link.getTarget());
    } else {
      return deleteById(link.getId());
    }
  }

  /**
   * @param entities the {@link Iterable} of {@link EntityBean entities} to {@link #delete(EntityBean) delete}.
   * @return the number of entities that have been physically deleted.
   */
  default int deleteAll(Iterable<E> entities) {

    // this is a naive default implementation. Real database backed implementations shall override with bulk operation.
    int deletionCount = 0;
    for (E entity : entities) {
      boolean deleted = delete(entity);
      if (deleted) {
        deletionCount++;
      }
    }
    return deletionCount;
  }

  /**
   * @param ids the {@link Iterable} of {@link Id}s to {@link #deleteById(Id) delete}.
   * @return the number of entities that have been physically deleted.
   */
  default int deleteAllById(Iterable<Id<E>> ids) {

    // this is a naive default implementation. Real database backed implementations shall override with bulk operation.
    int deletionCount = 0;
    for (Id<E> id : ids) {
      boolean deleted = deleteById(id);
      if (deleted) {
        deletionCount++;
      }
    }
    return deletionCount;
  }

  /**
   * @param links the {@link Iterable} of {@link Link}s to {@link #deleteByLink(Link) delete by}.
   * @return the number of entities that have been physically deleted.
   */
  default int deleteAllByLink(Iterable<Link<E>> links) {

    // this is a naive default implementation. Real database backed implementations shall override with bulk operation.
    int deletionCount = 0;
    for (Link<E> link : links) {
      boolean deleted = deleteByLink(link);
      if (deleted) {
        deletionCount++;
      }
    }
    return deletionCount;
  }

  /**
   * @param ids the {@link Iterable} of {@link Id}s to {@link #findById(Id) retrieve}.
   * @return an {@link List} with the {@link #findById(Id) retrieved} {@link EntityBean entities}.
   */
  default List<E> findAllById(Iterable<Id<E>> ids) {

    // this is a naive default implementation. Real database backed implementations shall override with bulk operation.
    int capacity = 16;
    if (ids instanceof Collection) {
      capacity = ((Collection<?>) ids).size();
    }
    List<E> entities = new ArrayList<>(capacity);
    for (Id<E> id : ids) {
      E entity = findById(id);
      if (entity != null) {
        entities.add(entity);
      }
    }
    return entities;
  }

}
