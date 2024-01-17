/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.bean.BeanHelper;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.AbstractId;
import io.github.mmm.entity.id.GenericId;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.OptimisicLockException;
import io.github.mmm.entity.id.sequence.IdSequence;
import io.github.mmm.entity.property.id.PkProperty;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.jdbc.connection.JdbcConnection;
import io.github.mmm.orm.jdbc.result.JdbcResult;
import io.github.mmm.orm.jdbc.sequence.JdbcSequence;
import io.github.mmm.orm.jdbc.session.JdbcEntitySession;
import io.github.mmm.orm.jdbc.session.JdbcSession;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.metadata.DbTable;
import io.github.mmm.orm.naming.DbNamingStrategy;
import io.github.mmm.orm.param.AbstractCriteriaParameters;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.spi.access.AbstractDbAccess;
import io.github.mmm.orm.statement.NonUniqueResultException;
import io.github.mmm.orm.statement.insert.InsertClause;
import io.github.mmm.orm.statement.insert.InsertStatement;
import io.github.mmm.orm.statement.select.SelectEntityClause;
import io.github.mmm.orm.statement.select.SelectStatement;
import io.github.mmm.orm.statement.update.UpdateClause;
import io.github.mmm.orm.statement.update.UpdateSet;
import io.github.mmm.orm.statement.update.UpdateStatement;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.criteria.CriteriaPredicate;
import io.github.mmm.property.criteria.PredicateOperator;
import io.github.mmm.property.criteria.PropertyAssignment;
import io.github.mmm.value.PropertyPath;
import io.github.mmm.value.SimplePath;

/**
 * Abstract implementation of {@link io.github.mmm.orm.spi.access.DbAccess} using JDBC.
 *
 * @since 1.0.0
 */
public class JdbcAccess extends AbstractDbAccess {

  private static final Logger LOG = LoggerFactory.getLogger(JdbcAccess.class);

  private static final PropertyPath<Object> COLUMN_REVISION = SimplePath.of("REV"); // see PkMapper

  private JdbcSession getSession() {

    return JdbcSession.get();
  }

  @Override
  protected AbstractDbDialect<?> getDialect() {

    return (AbstractDbDialect<?>) JdbcSession.get().getConnectionData().getDialect();
  }

  private DbNamingStrategy getNamingStrategy() {

    return JdbcSession.get().getConnectionData().getDialect().getNamingStrategy();
  }

  @Override
  public void insert(EntityBean entity) {

    doInsert(entity);
  }

  private <E extends EntityBean> void doInsert(E entity) {

    InsertStatement<E> insert = new InsertClause().into(entity).valuesAll().get();
    insert(insert);
    E managed = ReadableBean.copy(entity);
    JdbcEntitySession<E> entitySession = getSession().get(entity);
    entitySession.put(managed);
  }

  @Override
  public <E extends EntityBean> E selectById(Id<E> id, E prototype) {

    SelectStatement<E> select = new SelectEntityClause<>(prototype).from().where(prototype.Id().eq(id)).get();
    E entity = selectOne(select);
    return entity;
  }

  @Override
  public void update(EntityBean entity) {

    doUpdate(entity);
  }

  @SuppressWarnings("unchecked")
  private <E extends EntityBean> void doUpdate(E entity) {

    PkProperty pk = entity.Id();
    AbstractId<E, ?, ?> id = (AbstractId<E, ?, ?>) pk.get();
    if (id.isTransient()) {
      throw new IllegalStateException(
          "Cannot update entity of type " + entity.getType().getQualifiedName() + " because it is transient.");
    }
    JdbcEntitySession<E> entitySession = getSession().get(entity);
    E managed = entitySession.get(id);
    if (managed == null) {
      throw new IllegalStateException("Cannot update entity of type " + entity.getType().getQualifiedName()
          + " with ID " + id + " because it is not managed in the current transaction!");
    } else if (managed.getId().getRevision() != id.getRevision()) {
      throw new OptimisicLockException(id, entity.getType().getQualifiedName());
    }
    GenericId<?, ?, ?> newId = id.updateRevision();
    UpdateClause<EntityBean> updateEntity = new UpdateClause<>(entity);
    UpdateSet<EntityBean> set = updateEntity.setAll();
    for (WritableProperty<?> property : entity.getProperties()) {
      if (!property.isTransient()) {
        PropertyAssignment<?> assignment;
        if (property == pk) {
          assignment = PropertyAssignment.of(COLUMN_REVISION, newId.getRevision());
        } else {
          Object managedValue = managed.get(property.getName());
          Object updateValue = property.get();
          if (Objects.equals(updateValue, managedValue)) {
            continue;
          }
          assignment = PropertyAssignment.ofValue(property);
        }
        set = set.set(assignment);
      }
    }
    if (set == null) {
      LOG.debug("Omitting update of {} with ID {} because nothing has changed.", entity.getType().getStableName(), id);
      return;
    }
    UpdateStatement<EntityBean> update = set.where(pk.eq(id))
        .and(CriteriaPredicate.of(COLUMN_REVISION, PredicateOperator.EQ, id.getRevision())).get();
    long updateCount = update(update);
    if (updateCount == 0) {
      throw new OptimisicLockException(id, entity.getType().getQualifiedName());
    }
    assert (updateCount == 1);
    pk.set(newId);
    // TODO only copy non-transient properties...
    BeanHelper.copy(entity, managed);
  }

  @Override
  protected long executeSql(String sql, AbstractCriteriaParameters parameters, Consumer<DbResult> receiver,
      boolean unique) {

    LOG.debug(sql);
    Connection connection = getSession().getConnection();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      parameters.apply(statement, connection);
      long count;
      if (receiver == null) {
        count = statement.executeLargeUpdate();
      } else {
        count = 0;
        boolean dataResult = statement.execute();
        assert (dataResult);
        if (dataResult) {
          JdbcResult jdbcResult = null;
          do {
            try (ResultSet resultSet = statement.getResultSet()) {
              if (jdbcResult == null) {
                jdbcResult = new JdbcResult(resultSet);
              } else {
                jdbcResult.setResultSet(resultSet);
              }
              while (resultSet.next()) {
                receiver.accept(jdbcResult);
                if (!unique) {
                  resultSet.last();
                  int size = resultSet.getRow();
                  throw new NonUniqueResultException(size, sql);
                }
              }
              count++;
            }
          } while (statement.getMoreResults());
        } else {
          count = statement.getUpdateCount();
        }
      }
      return count;
    } catch (SQLException e) {
      // TODO proper custom runtinme exception class and error message including the SQL
      throw new IllegalStateException("Failed to execute SQL: " + sql, e);
    }
  }

  @Override
  public IdSequence createIdSequence(DbQualifiedName sequenceName) {

    return new JdbcSequence(sequenceName);
  }

  @Override
  public void syncTable(EntityBean entity) {

    JdbcConnection jdbcConnection = getSession().getJdbcConnection();
    DbName tableName = DbName.of(getNamingStrategy().getTableName(entity));
    DbTable table = jdbcConnection.getMetaData().getTable(tableName);
    if (table == null) {
      createTable(entity);
    } else {
      // alterTable
      // executeStatement();
    }
  }

}
