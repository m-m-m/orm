/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.memory.index;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.orm.memory.MemoryRepository;

/**
 * Simple in-memory unique index for {@link MemoryRepository}. Will store {@link EntityBean}s using a unique key (e.g. a
 * "title" or "name" column if unique). Will ignore {@code null} values as keys.
 *
 * @param <K> type of the index key (e.g. {@link String}).
 * @param <E> type of the {@link EntityBean}.
 */
public class MemoryNonUniqueIndex<K, E extends EntityBean> extends MemoryIndex<K, E> {

  private final Map<K, Set<Id<E>>> map;

  /**
   * The constructor.
   *
   * @param repository the owning {@link MemoryRepository}.
   * @param properties the {@link #getProperty(int) property names} to index.
   */
  public MemoryNonUniqueIndex(MemoryRepository<E> repository, String... properties) {

    this(repository, Function.identity(), properties);
  }

  /**
   * The constructor.
   *
   * @param repository the owning {@link MemoryRepository}.
   * @param normalizer the {@link #getNormalizer() normalizer}.
   * @param properties the {@link #getProperty(int) property names} to index.
   */
  public MemoryNonUniqueIndex(MemoryRepository<E> repository, Function<K, K> normalizer, String... properties) {

    super(repository, normalizer, properties);
    this.map = new HashMap<>(256);
  }

  @Override
  protected void addNormalized(K key, E entity) {

    if (key == null) {
      return;
    }
    Set<Id<E>> set = this.map.computeIfAbsent(key, k -> new HashSet<>());
    set.add(Id.from(entity).withoutRevision());
  }

  @Override
  protected void removeNormalized(K key, E entity) {

    if (key != null) {
      Set<Id<E>> set = this.map.get(key);
      if (set != null) {
        set.remove(Id.from(entity).withoutRevision());
      }
    }
  }

  /**
   * @param key the key of the {@link EntityBean} to find.
   * @return the {@link Iterable} of {@link EntityBean}s found for the given key.
   */
  public Iterable<E> find(K key) {

    Iterable<E> result = Collections.emptySet();
    if (key != null) {
      key = this.normalizer.apply(key);
      if (key != null) {
        Set<Id<E>> ids = this.map.get(key);
        if (ids != null) {
          result = this.repository.findAllById(ids);
        }
      }
    }
    return result;
  }

}
