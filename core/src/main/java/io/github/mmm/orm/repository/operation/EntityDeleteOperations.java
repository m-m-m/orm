/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.repository.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.link.Link;
import io.github.mmm.orm.repository.EntityRepository;

/**
 * Interface for the delete operations of an {@link EntityRepository}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public interface EntityDeleteOperations<E extends EntityBean> {

  /**
   * @param entity the {@link EntityBean} to remove from this repository.
   * @return {@code true} if the given {@link EntityBean entity} has been successfully deleted from this repository,
   *         {@code false} otherwise (was not {@link EntityRepository#save(EntityBean) saved} before).
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
      return delete(link.getEntity());
    } else {
      return deleteById(link.getId());
    }
  }

  /**
   * @param entities the {@link Iterable} of {@link EntityBean entities} to {@link #delete(EntityBean) delete}.
   * @return the number of entities that have been physically deleted.
   */
  default int deleteAll(Iterable<E> entities) {

    int capacity = 12;
    if (entities instanceof Collection<E> c) {
      capacity = c.size();
    }
    List<Id<E>> ids = new ArrayList<>(capacity);
    for (E entity : entities) {
      if (entity != null) { // robustness, should not happen
        Id<E> id = Id.from(entity);
        if (id != null) { // robustness, should not happen
          ids.add(id);
        }
      }
    }
    return deleteAllById(ids);
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

    int capacity = 12;
    if (links instanceof Collection<Link<E>> c) {
      capacity = c.size();
    }
    List<Id<E>> ids = new ArrayList<>(capacity);
    for (Link<E> link : links) {
      if (link != null) { // robustness, should not happen
        Id<E> id = link.getId();
        if (id != null) { // robustness, should not happen
          ids.add(id);
        }
      }
    }
    return deleteAllById(ids);
  }

}
