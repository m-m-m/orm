/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.repository;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.generator.IdGenerator;
import io.github.mmm.entity.id.generator.SequenceIdGenerator;
import io.github.mmm.entity.id.generator.UuidIdGenerator;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.repository.AbstractEntityRepository;
import io.github.mmm.orm.repository.DbRepository;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.spi.access.AbstractDbAccess;
import io.github.mmm.orm.spi.access.DbAccess;
import io.github.mmm.orm.statement.create.CreateSequenceClause;
import io.github.mmm.orm.statement.create.CreateSequenceStatement;
import io.github.mmm.orm.statement.delete.DeleteStatement;
import io.github.mmm.orm.statement.select.SelectStatement;
import io.github.mmm.orm.statement.update.UpdateStatement;

/**
 * Abstract base implementation of {@link DbRepository}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public abstract class AbstractDbRepository<E extends EntityBean> extends AbstractEntityRepository<E>
    implements DbRepository<E> {

  /** The default {@link #getSequenceName() sequence name}. */
  public static final String DEFAULT_SEQUENCE = "ENTITY_SEQUENCE";

  private final AbstractDbAccess dbAccess;

  /** {@link IdGenerator} used to {@link IdGenerator#generate(Id) generate} new unique {@link Id}s. */
  private final IdGenerator idGenerator;

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   */
  public AbstractDbRepository(E prototype) {

    this(prototype, null);
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   * @param idGenerator the {@link IdGenerator} used to {@link IdGenerator#generate(Id) generate} new unique
   *        {@link Id}s.
   */
  public AbstractDbRepository(E prototype, IdGenerator idGenerator) {

    super(prototype);
    this.dbAccess = (AbstractDbAccess) DbAccess.get(getSource());
    if (idGenerator == null) {
      DbQualifiedName sequenceName = getQualifiedSequenceName();
      if (sequenceName == null) {
        idGenerator = new UuidIdGenerator();
      } else {
        idGenerator = new SequenceIdGenerator(this.dbAccess.createIdSequence(sequenceName));
      }
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
   * @return the (unqualified) name of the database sequence for the managed entity. May be {@code null} for non
   *         sequence based IDs (e.g. UUID).
   */
  protected String getSequenceName() {

    return DEFAULT_SEQUENCE;
  }

  /**
   * @return the {@link DbQualifiedName} of the database sequence for the managed entity.
   * @see #getSequenceName()
   */
  protected DbQualifiedName getQualifiedSequenceName() {

    String sequenceName = getSequenceName();
    if (sequenceName == null) {
      return null;
    }
    return new DbQualifiedName(null, null, DbName.of(sequenceName));
  }

  @Override
  public E doFindById(Id<E> id) {

    return this.dbAccess.selectById(id, this.prototype);
  }

  @Override
  public E findOneByQuery(SelectStatement<E> statement) {

    verifyEntityClass(statement.getSelect().getResultBean().getType().getClass());
    return this.dbAccess.selectOne(statement);
  }

  @Override
  public Iterable<E> findByQuery(SelectStatement<E> statement) {

    verifyEntityClass(statement.getSelect().getResultBean().getJavaClass());
    return this.dbAccess.select(statement);
  }

  @Override
  public long delete(DeleteStatement<E> statement) {

    return this.dbAccess.delete(statement);
  }

  @Override
  protected boolean doDeleteById(Id<E> id) {

    return this.dbAccess.deleteById(id, this.prototype);
  }

  @Override
  protected int doDeleteAllById(Iterable<Id<E>> ids) {

    return this.dbAccess.deleteAllById(ids, this.prototype);
  }

  @Override
  protected void doInsert(E entity) {

    this.dbAccess.insert(entity);
  }

  @Override
  protected void doUpdate(E entity) {

    this.dbAccess.update(entity);
  }

  @Override
  public long update(UpdateStatement<E> statement) {

    return this.dbAccess.update(statement);
  }

  /**
   * Create the table for the managed entity.
   */
  public void createTable() {

    this.dbAccess.createTable(this.prototype);
  }

  /**
   * Create the ID sequence for the managed entity.
   */
  public void createSequence() {

    String sequenceName = getSequenceName();
    // if ((sequenceName == null) || (DEFAULT_SEQUENCE.equals(sequenceName))) {
    if (sequenceName == null) {
      return;
    }
    CreateSequenceStatement createSequenceStatement = new CreateSequenceClause(sequenceName).incrementBy(10)
        .startWith(1000000000000L).minValue(1000000000000L).maxValue(9123456789123456789L).nocycle().get();
    this.dbAccess.createSequence(createSequenceStatement);
  }

}
