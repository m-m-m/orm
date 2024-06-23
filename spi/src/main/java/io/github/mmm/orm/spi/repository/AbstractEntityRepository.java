/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.repository;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.entity.Entity;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.generator.IdGenerator;
import io.github.mmm.entity.link.Link;
import io.github.mmm.orm.listener.EntityListener;
import io.github.mmm.orm.repository.EntityRepository;

/**
 * Abstract base implementation of {@link EntityRepository}.
 *
 * @param <E> type of the managed {@link Entity}.
 * @since 1.0.0
 */
public abstract class AbstractEntityRepository<E extends EntityBean> implements EntityRepository<E> {

  /** @see #getPrototype() */
  protected final E prototype;

  /** @see #getEntityClass() */
  protected final Class<E> entityClass;

  private EntityListenerAdapter<E> listenerAdapter;

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   */
  public AbstractEntityRepository(E prototype) {

    super();
    this.prototype = prototype;
    this.entityClass = ReadableBean.getJavaClass(this.prototype);
    this.listenerAdapter = EntityListenerAdapter.empty();
  }

  /**
   * @return the {@link IdGenerator} to use.
   */
  protected abstract IdGenerator getIdGenerator();

  /**
   * @return an instance of the managed {@link EntityBean entity} to be used as template. Please note that this is an
   *         internal method of the implementation that should only be used by framework code. Mutations on the
   *         prototype by callers of this method are strictly forbidden and will lead to severe bugs.
   */
  public E getPrototype() {

    return this.prototype;
  }

  /**
   * Verifies that an entity {@link Class} (e.g. from an {@link Id}) is valid for this repository.
   *
   * @param entityType the {@link Class} reflecting the {@link EntityBean} to process.
   */
  protected void verifyEntityClass(Class<?> entityType) {

    // TODO support inheritance via isAssignableFrom
    assert (entityType == this.prototype.getJavaClass());
  }

  /**
   * @return the {@link Class} reflecting the managed {@link EntityBean}.
   */
  @Override
  public Class<E> getEntityClass() {

    return this.entityClass;
  }

  @Override
  public final E findById(Id<E> id) {

    verifyEntityClass(id.getEntityClass());
    E entity = doFindById(id);
    if (entity != null) {
      this.listenerAdapter.postLoad(entity);
    }
    return entity;
  }

  /**
   * @param id the {@link Id} of the requested {@link EntityBean entity}.
   * @return the requested {@link EntityBean entity} or {@code null} if no such entity exists.
   * @see #findById(Id)
   */
  protected abstract E doFindById(Id<E> id);

  @Override
  public E findByLink(Link<E> link) {

    // TODO Auto-generated method stub
    return EntityRepository.super.findByLink(link);
  }

  @Override
  public final Id<E> save(E entity) {

    Id<E> id = Id.from(entity);
    if (id.isTransient()) {
      id = getIdGenerator().generate(id);
      entity.setId(id);
      this.listenerAdapter.preInsert(entity);
      doInsert(entity);
    } else {
      this.listenerAdapter.preUpdate(entity);
      doUpdate(entity);
      id = Id.from(entity);
    }
    return id;
  }

  /**
   * Internal method for the raw insert of an {@link EntityBean} to the underlying store.
   *
   * @param entity the {@link EntityBean} to insert.
   */
  protected abstract void doInsert(E entity);

  /**
   * Internal method for the raw update of an {@link EntityBean} in the underlying store.
   *
   * @param entity the {@link EntityBean} to update.
   */
  protected abstract void doUpdate(E entity);

  @Override
  public final boolean deleteById(Id<E> id) {

    verifyEntityClass(id.getEntityClass());
    this.listenerAdapter.preDelete(id);
    return doDeleteById(id);
  }

  /**
   * @param id the {@link Id} (primary key) of the {@link EntityBean entity} to delete.
   * @return {@code true} on success, {@code false} otherwise.
   * @see #deleteById(Id)
   */
  protected abstract boolean doDeleteById(Id<E> id);

  @Override
  public final int deleteAllById(Iterable<Id<E>> ids) {

    this.listenerAdapter.preDelete(ids);
    return doDeleteAllById(ids);
  }

  /**
   * @param ids the {@link Iterable} of {@link Id}s to {@link #deleteById(Id) delete}.
   * @return the number of entities that have been physically deleted. * @param ids
   * @see #deleteAllById(Iterable)
   */
  protected abstract int doDeleteAllById(Iterable<Id<E>> ids);

  /**
   * @return {@code true} to verify and update the {@link io.github.mmm.entity.id.Id#getRevision() revision} on
   *         {@link #save(EntityBean) save}.
   */
  protected boolean isUseRevision() {

    return true;
  }

  /**
   * @param listener the {@link EntityListener} to add.
   */
  protected void addListener(EntityListener<? super E> listener) {

    this.listenerAdapter = this.listenerAdapter.add(listener);
  }

  /**
   * @param listener the {@link EntityListener} to remove.
   * @return {@code true} if successfully removed, {@code false} otherwise (was not {@link #addListener(EntityListener)
   *         added} before).
   */
  protected boolean removeListener(EntityListener<? super E> listener) {

    EntityListenerAdapter<E> removed = this.listenerAdapter.remove(listener);
    if (removed == null) {
      return false;
    }
    this.listenerAdapter = removed;
    return true;
  }

}
