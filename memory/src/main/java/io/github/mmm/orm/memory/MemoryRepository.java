package io.github.mmm.orm.memory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.bean.BeanHelper;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.GenericId;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.OptimisicLockException;
import io.github.mmm.entity.link.IdLink;
import io.github.mmm.entity.link.Link;
import io.github.mmm.entity.property.link.LinkProperty;
import io.github.mmm.marshall.StructuredFormatHelper;
import io.github.mmm.marshall.StructuredFormatProvider;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredState;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.orm.memory.index.IndexOperation;
import io.github.mmm.orm.memory.index.MemoryIndex;
import io.github.mmm.orm.repository.AbstractEntityRepository;
import io.github.mmm.orm.repository.EntityRepository;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.container.collection.CollectionProperty;
import io.github.mmm.property.container.collection.ReadableCollectionProperty;
import io.github.mmm.property.string.StringCollectionProperty;

/**
 * Abstract base class for an in-memory {@link EntityRepository}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @see SequenceMemoryRepository
 */
public abstract class MemoryRepository<E extends EntityBean> extends AbstractEntityRepository<E> {

  private final Map<Id<E>, EntityHolder<E>> entityMap;

  private final Function<Id<?>, EntityBean> resolver;

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   */
  public MemoryRepository(E prototype) {

    this(prototype, null);
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param resolver the custom resolver {@link Function} for lazy loading or {@code null} to build default.
   */
  public MemoryRepository(E prototype, Function<Id<?>, EntityBean> resolver) {

    this(prototype, resolver, null);
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param resolver the custom resolver {@link Function} for lazy loading or {@code null} to build default.
   * @param entityMap the {@link Map} implementation used to store the entities.
   */
  public MemoryRepository(E prototype, Function<Id<?>, EntityBean> resolver, Map<Id<E>, EntityHolder<E>> entityMap) {

    super(prototype);
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

  /**
   * @return the {@link Collection} of {@link MemoryIndex indices} to manage.
   */
  protected Collection<MemoryIndex<?, ? super E>> getIndices() {

    return Collections.emptyList();
  }

  @Override
  protected boolean isUseRevision() {

    // pragmatic default for in memory
    return false;
  }

  @Override
  public E doFindById(Id<E> id) {

    if (id == null) {
      return null;
    }
    EntityHolder<E> holder = this.entityMap.get(id.withoutRevision());
    if (holder == null) {
      return null;
    }
    return holder.getExternalEntity();
  }

  @Override
  public boolean doDeleteById(Id<E> id) {

    if (id == null) {
      return false;
    }
    EntityHolder<E> holder = this.entityMap.remove(id.withoutRevision());
    if (holder == null) {
      return false;
    } else {
      updateIndices(holder.getInternalEntity(), null, IndexOperation.REMOVE);
      return true;
    }
  }

  @Override
  protected int doDeleteAllById(Iterable<Id<E>> ids) {

    int count = 0;
    for (Id<E> id : ids) {
      boolean deleted = deleteById(id);
      if (deleted) {
        count++;
      }
    }
    return count;
  }

  @SuppressWarnings({ "rawtypes", "unchecked", "null" })
  private void updateIndices(E entity, E oldEntity, IndexOperation operation) {

    for (MemoryIndex index : getIndices()) {
      int propertyCount = index.getPropertyCount();
      for (int propertyIndex = 0; propertyIndex < propertyCount; propertyIndex++) {
        String propertyName = index.getProperty(propertyIndex);
        WritableProperty property = entity.getProperty(propertyName);
        WritableProperty oldProperty = null;
        if (oldEntity != null) {
          oldProperty = oldEntity.getProperty(propertyName);
        }
        if (property != null) {
          if (property instanceof ReadableCollectionProperty<?, ?> c) {
            switch (operation) {
              case ADD -> index.addAll(c.get(), entity);
              case UPDATE -> index.updateAll(c.get(), entity, ((ReadableCollectionProperty<?, ?>) oldProperty).get());
              case REMOVE -> index.removeAll(c.get(), entity);
            }
          } else if (property instanceof StringCollectionProperty c) {
            switch (operation) {
              case ADD -> index.addAll(c, entity);
              case UPDATE -> index.updateAll(c, entity, (StringCollectionProperty) oldProperty);
              case REMOVE -> index.removeAll(c, entity);
            }
          } else {
            Object newKey = property.get();
            switch (operation) {
              case ADD -> index.add(newKey, entity);
              case UPDATE -> index.update(newKey, entity, oldProperty.get());
              case REMOVE -> index.remove(newKey);
            }
          }
        }
      }
    }
  }

  @Override
  protected void doInsert(E entity) {

    doInsert(entity, false);
  }

  private void doInsert(E entity, boolean internal) {

    Id<E> id = Id.from(entity);
    EntityHolder<E> holder = EntityHolder.of(entity, internal);
    connectResolver(holder.externalEntity);
    EntityHolder<E> duplicate = this.entityMap.putIfAbsent(id.withoutRevision(), holder);
    if (duplicate != null) {
      throw new IllegalStateException();
    }
    updateIndices(entity, null, IndexOperation.ADD);
  }

  @SuppressWarnings({ "unchecked" })
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
  protected void doUpdate(E entity) {

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
    updateIndices(entity, internal, IndexOperation.UPDATE);
    BeanHelper.copy(entity, internal);
  }

  /**
   * Loads an entire array of {@link EntityBean entities} from the given {@link StructuredReader} and insert them into
   * this repository.
   *
   * @param reader the {@link StructuredReader} to read the array of entities from.
   * @see #read(Path)
   */
  public void read(StructuredReader reader) {

    reader.require(StructuredState.START_ARRAY, true);
    while (!reader.readEndArray()) {
      E entity = ReadableBean.newInstance(this.prototype);
      entity.read(reader);
      doInsert(entity, true);
    }
  }

  /**
   * @param path the {@link Path} to the file with all {@link EntityBean entities} to load into this repository.
   * @see #write(Path)
   * @see #read(StructuredReader)
   */
  public void read(Path path) {

    StructuredFormatProvider provider = StructuredFormatHelper.getProvider(path);
    try (InputStream in = Files.newInputStream(path)) {
      StructuredReader reader = provider.create().reader(in);
      read(reader);
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  /**
   * @param writer the {@link StructuredWriter} to write all entities of this repository to.
   * @see #write(Path)
   */
  public void write(StructuredWriter writer) {

    writer.writeStartArray();
    for (EntityHolder<E> holder : this.entityMap.values()) {
      holder.internalEntity.write(writer);
    }
    writer.writeEnd();
  }

  /**
   * @param path the {@link Path} to the file where to write all {@link EntityBean entities} of this repository.
   * @see #read(Path)
   * @see #write(StructuredWriter)
   */
  public void write(Path path) {

    StructuredFormatProvider provider = StructuredFormatHelper.getProvider(path);
    try (OutputStream out = Files.newOutputStream(path)) {
      StructuredWriter writer = provider.create().writer(out);
      write(writer);
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
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
