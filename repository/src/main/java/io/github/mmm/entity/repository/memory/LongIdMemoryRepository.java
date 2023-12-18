package io.github.mmm.entity.repository.memory;

import java.util.Map;
import java.util.function.Function;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.LongIdGenerator;
import io.github.mmm.entity.id.LongIdMemorySequence;

/**
 * TODO hohwille This type ...
 *
 */
public class LongIdMemoryRepository<E extends EntityBean> extends MemoryRepository<E> {

  private final LongIdMemorySequence sequence;

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   */
  public LongIdMemoryRepository(E prototype) {

    this(prototype, null, null);
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   */
  public LongIdMemoryRepository(E prototype, Function<Id<?>, EntityBean> resolver) {

    this(prototype, resolver, null);
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param entityMap the {@link Map} implementation used to store the entities.
   */
  public LongIdMemoryRepository(E prototype, Function<Id<?>, EntityBean> resolver, Map entityMap) {

    this(prototype, resolver, new LongIdMemorySequence(), entityMap);
  }

  private LongIdMemoryRepository(E prototype, Function<Id<?>, EntityBean> resolver, LongIdMemorySequence sequence,
      Map<Object, E> entityMap) {

    super(prototype, resolver, new LongIdGenerator(sequence), entityMap);
    this.sequence = sequence;
  }

}
