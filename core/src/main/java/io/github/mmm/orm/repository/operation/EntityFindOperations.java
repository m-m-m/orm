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
 * Interface for the find operations of an {@link EntityRepository}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public interface EntityFindOperations<E extends EntityBean> {

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
