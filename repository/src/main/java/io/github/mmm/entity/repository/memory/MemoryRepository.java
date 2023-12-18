package io.github.mmm.entity.repository.memory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import io.github.mmm.bean.BeanHelper;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.bean.repository.AbstractEntityRepository;
import io.github.mmm.entity.id.GenericId;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.IdGenerator;
import io.github.mmm.entity.id.OptimisicLockException;
import io.github.mmm.entity.link.IdLink;
import io.github.mmm.entity.link.Link;
import io.github.mmm.entity.property.link.LinkProperty;
import io.github.mmm.entity.repository.EntityRepository;
import io.github.mmm.marshall.StructuredState;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.container.collection.CollectionProperty;

/**
 * Abstract base class for an in-memory {@link EntityRepository}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 */
public abstract class MemoryRepository<E extends EntityBean> extends AbstractEntityRepository<E> {

  private final Map<Object, EntityHolder<E>> entityMap;

  private final Function<Id<?>, EntityBean> resolver;

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param resolver
   * @param idGenerator the {@link IdGenerator} used to {@link IdGenerator#generate(Id) generate} new unique
   *        {@link Id}s.
   */
  public MemoryRepository(E prototype, Function<Id<?>, EntityBean> resolver, IdGenerator idGenerator) {

    this(prototype, resolver, idGenerator, null);
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param resolver
   * @param idGenerator the {@link IdGenerator} used to {@link IdGenerator#generate(Id) generate} new unique
   *        {@link Id}s.
   * @param entityMap the {@link Map} implementation used to store the entities.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public MemoryRepository(E prototype, Function<Id<?>, EntityBean> resolver, IdGenerator idGenerator, Map entityMap) {

    super(prototype, idGenerator);
    if (resolver == null) {
      this.resolver = new RepoResolver();
    } else {
      this.resolver = resolver;
    }
    if (entityMap == null) {
      this.entityMap = new ConcurrentHashMap<>();
    } else {
      this.entityMap = entityMap;
    }
  }

  @Override
  protected boolean isUseRevision() {

    // pragmatic default for in memory
    return false;
  }

  @Override
  public E findById(Id<E> id) {

    if (id == null) {
      return null;
    }
    Object pk = id.get();
    if (pk == null) {
      return null;
    }
    EntityHolder<E> holder = this.entityMap.get(pk);
    if (holder == null) {
      return null;
    }
    return holder.getExternalEntity();
  }

  @Override
  public boolean deleteById(Id<E> id) {

    if (id == null) {
      return false;
    }
    Object pk = id.get();
    if (pk == null) {
      return false;
    }
    EntityHolder<E> holder = this.entityMap.remove(pk);
    return (holder != null);
  }

  @Override
  protected void insert(E entity) {

    insert(entity, false);
  }

  private void insert(E entity, boolean internal) {

    Id<?> id = entity.getId();
    Object pk = id.get();
    EntityHolder<E> holder = EntityHolder.of(entity, internal);
    connectResolver(holder.externalEntity);
    EntityHolder<E> duplicate = this.entityMap.putIfAbsent(pk, holder);
    if (duplicate != null) {
      throw new IllegalStateException();
    }
  }

  @SuppressWarnings("unchecked")
  private void connectResolver(E entity) {

    for (WritableProperty<?> property : entity.getProperties()) {
      if (!property.isReadOnly()) {
        if (property instanceof LinkProperty linkProperty) {
          linkProperty.setResolver(this.resolver);
        } else if (property instanceof CollectionProperty<?, ?> collectionProperty) {
          WritableProperty<?> valueProperty = collectionProperty.getValueProperty();
          if (valueProperty instanceof LinkProperty linkProperty) {
            linkProperty.setResolver(this.resolver);
            Collection<Link<?>> collection = (Collection<Link<?>>) collectionProperty.get();
            if (collection != null) {
              for (Link<?> link : collection) {
                if (link instanceof IdLink idLink) {
                  idLink.setResolver(this.resolver);
                }
              }
            }
          }
        }
      }
    }
  }

  @Override
  protected void update(E entity) {

    Id<?> id = entity.getId();
    Object pk = id.get();
    EntityHolder<E> holder = this.entityMap.get(pk);
    if (holder == null) {
      throw new IllegalArgumentException("Cannot save transient entity with persistent id " + id);
    }
    holder.verifyExternalEntity(entity);
    E internal = holder.internalEntity;
    if (isUseRevision()) {
      Id<?> internalId = internal.getId();
      if (!internalId.equals(id)) {
        throw new OptimisicLockException(id, internal.getType().getStableName());
      }
      internalId = GenericId.updateRevision(internalId);
      entity.setId(internalId);
    }
    BeanHelper.copy(entity, internal);
  }

  /**
   * Loads an entire array of {@link EntityBean entities} from the given {@link StructuredReader} and insert them into
   * this repository.
   *
   * @param reader the {@link StructuredReader} to read the array of entities from.
   */
  public void load(StructuredReader reader) {

    reader.require(StructuredState.START_ARRAY, true);
    while (!reader.readEndArray()) {
      E entity = ReadableBean.newInstance(this.prototype);
      entity.read(reader);
      insert(entity, true);
    }
  }

  /**
   * @param writer the {@link StructuredWriter} to write all entities of this repository to.
   */
  public void save(StructuredWriter writer) {

    writer.writeStartArray();
    for (EntityHolder<E> holder : this.entityMap.values()) {
      holder.internalEntity.write(writer);
    }
    writer.writeEnd();
  }

  private class RepoResolver implements Function<Id<?>, EntityBean> {

    @SuppressWarnings("unchecked")
    @Override
    public EntityBean apply(Id<?> id) {

      if (id == null) {
        return null;
      }
      if (id.getEntityClass() == getEntityClass()) {
        return findById((Id<E>) id);
      }
      return null;
    }
  }

}
