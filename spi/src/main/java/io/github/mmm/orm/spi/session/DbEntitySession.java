/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.session;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;

/**
 * Session data for a specific {@link EntityBean#getType() type} of {@link EntityBean}. Represents the internal
 * first-level cache and shall not be exposed externally.
 *
 * @param <E> type of the managed {@link EntityBean}.
 *
 * @see DbSession#get(EntityBean)
 */
public class DbEntitySession<E extends EntityBean> {

  private final Map<Object, E> entityMap;

  /**
   * The constructor.
   */
  public DbEntitySession() {

    super();
    // TODO with project loom will we have sub-threads accessing repos and need ConcurrentMap?
    this.entityMap = new HashMap<>();
  }

  /**
   * @param id the {@link Id} of the requested {@link EntityBean}.
   * @return the {@link EntityBean} with the given {@link Id} from the first-level cache or {@code null} if not present.
   */
  public E get(Id<E> id) {

    if (id == null) {
      return null;
    }
    Object pk = id.get();
    E entity = this.entityMap.get(pk);
    return entity;
  }

  /**
   * @param id the {@link Id} of the requested {@link EntityBean}.
   * @param loader the {@link Function} to load the {@link EntityBean} by {@link Id} from the database. May return
   *        {@code null} for {@link Id}s that do not exist in the database.
   * @return the {@link EntityBean} with the given {@link Id}. Will be taken from first-level cache if present and
   *         otherwise loaded from DB and put into first-level cache. Result may still be {@code null} if no entity
   *         exists for the given {@link Id}.
   */
  public E getOrLoad(Id<E> id, Function<Id<E>, E> loader) {

    if (id == null) {
      return null;
    }
    Object pk = id.get();
    E entity = this.entityMap.computeIfAbsent(pk, k -> loader.apply(id));
    return entity;
  }

  /**
   * @param entity the {@link EntityBean} to put into the first-level cache.
   */
  public void put(E entity) {

    put(entity, Id.from(entity));
  }

  /**
   * @param entity the {@link EntityBean} to put into the first-level cache.
   * @param id the explicit new {@link Id} for the {@link EntityBean} (e.g. to ensure it is assigned only at the very
   *        end on success)
   */
  public void put(E entity, Id<E> id) {

    Object pk = id.get();
    if (pk == null) {

    }
    E duplicate = this.entityMap.put(pk, entity);
    assert (duplicate == null);
  }

  /**
   * @param id the {@link Id} of the requested {@link EntityBean}.
   * @return a copy of the {@link EntityBean} from the first-level cache that would be returned by {@link #get(Id)}.
   */
  public E getCopy(Id<E> id) {

    E entity = get(id);
    if (entity == null) {
      return null;
    }
    return ReadableBean.copy(entity);
  }

  /**
   * Closes this session.
   */
  protected void close() {

    this.entityMap.clear();
    ;
  }

}
