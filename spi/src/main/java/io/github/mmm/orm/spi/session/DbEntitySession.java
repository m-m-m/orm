/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.session;

import java.util.function.Function;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;

/**
 * Session data for a specific {@link EntityBean#getType() type} of {@link EntityBean}. Represents the internal
 * first-level cache and shall not be exposed externally.
 *
 * @param <E> type of the managed {@link EntityBean}.
 *
 * @see DbSession#get(EntityBean)
 * @since 1.0.0
 */
public interface DbEntitySession<E extends EntityBean> {

  /**
   * @return the {@link Class} reflecting the managed {@link EntityBean}.
   */
  Class<E> getEntityClass();

  /**
   * @param id the {@link Id} of the requested {@link EntityBean}.
   * @return the {@link DbEntityHolder} containing the {@link EntityBean} with the given {@link Id} from the first-level
   *         cache or {@code null} if not present.
   */
  default DbEntityHolder<E> get(Id<E> id) {

    return getOrLoad(id, null);
  }

  /**
   * @param id the {@link Id} of the requested {@link EntityBean}.
   * @param loader the {@link Function} to load the {@link EntityBean} by {@link Id} from the database. May return
   *        {@code null} for {@link Id}s that do not exist in the database.
   * @return the {@link DbEntityHolder} containing the {@link EntityBean} with the given {@link Id}. Will be taken from
   *         first-level cache if present and otherwise loaded from DB and put into first-level cache. Result may still
   *         be {@code null} if no entity exists for the given {@link Id}.
   */
  DbEntityHolder<E> getOrLoad(Id<E> id, Function<Id<E>, E> loader);

  /**
   * @param entity the {@link EntityBean} to put into the first-level cache.
   * @return the {@link DbEntityHolder} created for the given {@link EntityBean}.
   */
  default DbEntityHolder<E> put(E entity) {

    return put(entity, Id.from(entity));
  }

  /**
   * @param managedEntity the managed {@link EntityBean} to put into the first-level cache.
   * @param id the explicit new {@link Id} for the {@link EntityBean} (e.g. to ensure it is assigned only at the very
   *        end on success)
   * @return the {@link DbEntityHolder} created for the given {@link EntityBean}.
   */
  DbEntityHolder<E> put(E managedEntity, Id<E> id);

}
