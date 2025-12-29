/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.memory.index;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class MemoryUniqueIndex<K, E extends EntityBean> extends MemoryIndex<K, E> {

  private final Map<K, Id<E>> map;

  /**
   * The constructor.
   *
   * @param repository the owning {@link MemoryRepository}.
   * @param properties the {@link #getProperty(int) property names} to index.
   */
  public MemoryUniqueIndex(MemoryRepository<E> repository, String... properties) {

    this(repository, Function.identity(), properties);
  }

  /**
   * The constructor.
   *
   * @param repository the owning {@link MemoryRepository}.
   * @param normalizer the {@link #getNormalizer() normalizer}.
   * @param properties the {@link #getProperty(int) property names} to index.
   */
  public MemoryUniqueIndex(MemoryRepository<E> repository, Function<K, K> normalizer, String... properties) {

    super(repository, normalizer, properties);
    this.map = new HashMap<>(256);
  }

  @Override
  protected void addNormalized(K key, E entity) {

    if (key == null) {
      return;
    }
    Object duplicate = this.map.put(key, Id.from(entity).withoutRevision());
    assert (duplicate == null || (duplicate != entity)) : "Unique index violation for key '" + key + "' for " + entity
        + " with " + duplicate;
  }

  @Override
  protected void removeNormalized(K key, E entity) {

    if (key != null) {
      Id<E> old = this.map.remove(key);
      assert ((entity == null) || (old.getPk() == entity.getId().getPk()));
    }
  }

  /**
   * @param key the key of the {@link EntityBean} to find.
   * @return the requested {@link EntityBean} or {@code null} if not indexed.
   */
  public E find(K key) {

    if (key == null) {
      return null;
    }
    key = this.normalizer.apply(key);
    if (key == null) {
      return null;
    }
    Id<E> id = this.map.get(key);
    if (id == null) {
      return null;
    }
    return this.repository.findById(id);
  }

  @Override
  public Iterable<E> findAll(K key) {

    E entity = find(key);
    if (entity == null) {
      return List.of();
    }
    return List.of(entity);
  }

}
