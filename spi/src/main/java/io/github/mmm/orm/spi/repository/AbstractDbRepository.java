/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.repository;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.generator.IdGenerator;
import io.github.mmm.entity.id.generator.LongIdGenerator;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.repository.DbRepository;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.spi.access.AbstractDbAccess;
import io.github.mmm.orm.spi.access.DbAccess;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * Abstract base implementation of {@link DbRepository}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 */
public abstract class AbstractDbRepository<E extends EntityBean> extends AbstractEntityRepository<E>
    implements DbRepository<E> {

  /** The default {@link #getSequenceName() sequence name}. */
  public static final String DEFAULT_SEQUENCE = "ENTITY_SEQUENCE";

  private final DbAccess dbAccess;

  /** {@link IdGenerator} used to {@link IdGenerator#generate(Id) generate} new unique {@link Id}s. */
  protected final IdGenerator idGenerator;

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param dbAccess the {@link DbAccess}.
   */
  public AbstractDbRepository(E prototype, DbAccess dbAccess) {

    this(prototype, dbAccess, null);
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param dbAccess the {@link DbAccess}.
   * @param idGenerator the {@link IdGenerator} used to {@link IdGenerator#generate(Id) generate} new unique
   *        {@link Id}s.
   */
  public AbstractDbRepository(E prototype, DbAccess dbAccess, IdGenerator idGenerator) {

    super(prototype);
    this.dbAccess = dbAccess;
    if (idGenerator == null) {
      DbQualifiedName sequenceName = new DbQualifiedName(null, null, DbName.of(getSequenceName()));
      idGenerator = new LongIdGenerator(((AbstractDbAccess) dbAccess).createIdSequence(sequenceName));
    }
    this.idGenerator = idGenerator;
  }

  /**
   * @return the {@link DbSource} of this repository. Typically this is the {@link DbSource#get() default}
   *         {@link DbSource}. May be overridden to connect your repository to a different {@link DbSource database
   *         source}.
   */
  public DbSource getSource() {

    return DbSource.get();
  }

  @Override
  protected IdGenerator getIdGenerator() {

    return this.idGenerator;
  }

  /**
   * @return the (unqualified) name of the database sequence.
   */
  protected String getSequenceName() {

    return DEFAULT_SEQUENCE;
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
