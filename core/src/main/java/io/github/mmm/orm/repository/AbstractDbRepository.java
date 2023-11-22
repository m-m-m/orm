/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.repository;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.bean.repository.AbstractEntityRepository;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.IdGenerator;
import io.github.mmm.orm.access.DbAccess;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * Abstract base implementation of {@link DbRepository}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 */
public abstract class AbstractDbRepository<E extends EntityBean> extends AbstractEntityRepository<E>
    implements DbRepository<E> {

  private final DbAccess dbAccess;

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param idGenerator the {@link IdGenerator} used to {@link IdGenerator#generate(Id) generate} new unique
   *        {@link Id}s.
   * @param dbAccess the {@link DbAccess}.
   */
  public AbstractDbRepository(E prototype, IdGenerator idGenerator, DbAccess dbAccess) {

    super(prototype, idGenerator);
    this.dbAccess = dbAccess;
  }

  protected void verifyEntityClass(Class<?> entityClass) {

    // TODO
    assert (entityClass == this.prototype.getType().getClass());
  }

  @Override
  public E findById(Id<E> id) {

    verifyEntityClass(id.getEntityClass());
    return this.dbAccess.selectById(id, this.prototype);
  }

  @Override
  public E findOneByQuery(SelectStatement<E> statement) {

    verifyEntityClass(statement.getSelect().getResultBean().getType().getClass());
    return this.dbAccess.selectOne(statement);
  }

  @Override
  public Iterable<E> findByQuery(SelectStatement<E> statement) {

    verifyEntityClass(statement.getSelect().getResultBean().getType().getClass());
    return this.dbAccess.select(statement);
  }

  @Override
  public boolean deleteById(Id<E> id) {

    verifyEntityClass(id.getEntityClass());
    return this.dbAccess.deleteById(id, this.prototype);
  }

  @Override
  public int deleteAllById(Iterable<Id<E>> ids) {

    return this.dbAccess.deleteAllById(ids, this.prototype);
  }

  @Override
  protected void insert(E entity) {

    this.dbAccess.insert(entity);
  }

  @Override
  protected void update(E entity) {

    this.dbAccess.update(entity);
  }

}
