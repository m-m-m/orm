/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.repository;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.generator.IdGenerator;
import io.github.mmm.entity.id.generator.SequenceIdGenerator;
import io.github.mmm.entity.id.generator.UuidIdGenerator;
import io.github.mmm.entity.id.sequence.IdSequence;
import io.github.mmm.entity.property.id.FkProperty;
import io.github.mmm.entity.property.link.LinkProperty;
import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.repository.AbstractEntityRepository;
import io.github.mmm.orm.repository.DbRepository;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.spi.access.AbstractDbAccess;
import io.github.mmm.orm.spi.access.DbAccess;
import io.github.mmm.orm.spi.sequence.IdSequencePooled;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.create.CreateIndexStatement;
import io.github.mmm.orm.statement.create.CreateSequenceClause;
import io.github.mmm.orm.statement.create.CreateSequenceStatement;
import io.github.mmm.orm.statement.delete.DeleteStatement;
import io.github.mmm.orm.statement.select.SelectStatement;
import io.github.mmm.orm.statement.update.UpdateStatement;
import io.github.mmm.property.WritableProperty;

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

  private final DbConnectionData connectionData;

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
    DbSource source = getSource();
    this.dbAccess = (AbstractDbAccess) DbAccess.get(source);
    this.connectionData = DbConnectionData.of(source);
    if (idGenerator == null) {
      AbstractDbDialect<?> dialect = this.dbAccess.getDialect();
      if (dialect.isSupportingSequence()) {
        DbQualifiedName sequenceName = getQualifiedSequenceName();
        if (sequenceName == null) {
          idGenerator = new UuidIdGenerator();
        } else {
          IdSequence idSequence = this.dbAccess.createIdSequence(sequenceName);
          int sequenceIncrement = this.connectionData.getSequenceIncrement();
          if (sequenceIncrement > 1) {
            idSequence = new IdSequencePooled(idSequence, sequenceIncrement);
          }
          idGenerator = new SequenceIdGenerator(idSequence);
        }
      } else {
        IdSequence idSequence = this.dbAccess.createIdSequence(new DbQualifiedName(null, null, DbName.of("none")));
        idGenerator = new SequenceIdGenerator(idSequence);
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

  /**
   * @return the {@link DbConnectionData} for convenient access.
   */
  protected DbConnectionData getConnectionData() {

    return this.connectionData;
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
    return this.connectionData.getQualifiedNameTemplate().withName(sequenceName);
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
    createIndexes();
  }

  /**
   * This method is called from {@link #createTable()} in order to create the indexes for the table. By default, for
   * each foreign key an index will be created. You can override this entire method or XXX in order to replace, extend
   * or adapt the default behaviour.
   */
  protected void createIndexes() {

    for (WritableProperty<?> property : this.prototype.getProperties()) {
      CreateIndexStatement<E> createIndexStatement = createIndex(property);
      if (createIndexStatement != null) {
        this.dbAccess.createIndex(createIndexStatement);
      }
    }
  }

  /**
   * @param property the {@link WritableProperty} to potentially generate an automatic index for.
   * @return the {@link CreateIndexStatement} or {@code null} for no index.
   */
  protected CreateIndexStatement<E> createIndex(WritableProperty<?> property) {

    Class<?> targetEntity = null;
    if (property instanceof FkProperty<?> fkProperty) {
      targetEntity = fkProperty.get().getEntityClass();
    } else if (property instanceof LinkProperty<?> linkProperty) {
      targetEntity = linkProperty.getEntityClass();
    }
    if (targetEntity != null) {
      return DbStatement.createIndex().on(this.prototype).column(property).get();
    }
    return null;
  }

  /**
   * Create the ID sequence for the managed entity.
   */
  public void createSequence() {

    AbstractDbDialect<?> dialect = this.dbAccess.getDialect();
    getSource();
    if (!dialect.isSupportingSequence()) {
      return;
    }
    String sequenceName = getSequenceName();
    // if ((sequenceName == null) || (DEFAULT_SEQUENCE.equals(sequenceName))) {
    if (sequenceName == null) {
      return;
    }
    CreateSequenceStatement createSequenceStatement = new CreateSequenceClause(sequenceName)
        .incrementBy(this.connectionData.getSequenceIncrement()).startWith(1000000000000L).minValue(1000000000000L - 1)
        .maxValue(9123456789123456789L).nocycle().get();
    this.dbAccess.createSequence(createSequenceStatement);
  }

}
