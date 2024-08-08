package io.github.mmm.orm.memory;

import java.util.Map;
import java.util.function.Function;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.generator.IdGenerator;
import io.github.mmm.entity.id.generator.SequenceIdGenerator;
import io.github.mmm.entity.id.sequence.IdSequenceMemory;

/**
 * Implementation of {@link MemoryRepository} using {@link IdSequenceMemory}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 */
public class SequenceMemoryRepository<E extends EntityBean> extends MemoryRepository<E> {

  private final IdSequenceMemory sequence;

  private final IdGenerator idGenerator;

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   */
  public SequenceMemoryRepository(E prototype) {

    this(prototype, null, null);
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param resolver the custom resolver {@link Function} for lazy loading or {@code null} to build default.
   */
  public SequenceMemoryRepository(E prototype, Function<Id<?>, EntityBean> resolver) {

    this(prototype, resolver, null);
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param resolver the custom resolver {@link Function} for lazy loading or {@code null} to build default.
   * @param entityMap the {@link Map} implementation used to store the entities.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public SequenceMemoryRepository(E prototype, Function<Id<?>, EntityBean> resolver, Map entityMap) {

    super(prototype, resolver, entityMap);
    this.sequence = new IdSequenceMemory();
    this.idGenerator = new SequenceIdGenerator(this.sequence);
  }

  @Override
  protected IdGenerator getIdGenerator() {

    return this.idGenerator;
  }

  /**
   * @return the {@link IdSequenceMemory}.
   */
  protected IdSequenceMemory getSequence() {

    return this.sequence;
  }

}
