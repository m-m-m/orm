/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.bean.BeanHelper;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.AbstractId;
import io.github.mmm.entity.id.GenericId;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.OptimisicLockException;
import io.github.mmm.entity.property.id.PkProperty;
import io.github.mmm.orm.access.AbstractDbAccess;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.jdbc.connection.JdbcConnection;
import io.github.mmm.orm.jdbc.session.JdbcEntitySession;
import io.github.mmm.orm.jdbc.session.JdbcSession;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbTable;
import io.github.mmm.orm.naming.DbNamingStrategy;
import io.github.mmm.orm.param.AbstractCriteriaParameters;
import io.github.mmm.orm.statement.insert.Insert;
import io.github.mmm.orm.statement.insert.InsertStatement;
import io.github.mmm.orm.statement.select.SelectEntity;
import io.github.mmm.orm.statement.select.SelectStatement;
import io.github.mmm.orm.statement.update.Update;
import io.github.mmm.orm.statement.update.UpdateSet;
import io.github.mmm.orm.statement.update.UpdateStatement;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.criteria.CriteriaPredicate;
import io.github.mmm.property.criteria.PredicateOperator;
import io.github.mmm.property.criteria.PropertyAssignment;
import io.github.mmm.property.criteria.SimplePath;
import io.github.mmm.value.PropertyPath;

/**
 * Abstract implementation of {@link io.github.mmm.orm.access.DbAccess} using JDBC.
 */
public class JdbcDbAccess extends AbstractDbAccess {

  private static final Logger LOG = LoggerFactory.getLogger(JdbcDbAccess.class);

  private static final PropertyPath<Object> COLUMN_REVISION = SimplePath.of("REV"); // see PkMapper

  protected JdbcSession getSession() {

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

    Id<?> id = entity.getId();
    InsertStatement<EntityBean> insert = new Insert().into(entity).values().get();
    executeStatement(insert);
    EntityBean managed = ReadableBean.copy(entity);
    JdbcEntitySession entitySession = getSession().get(entity);
    entitySession.put(managed);
  }

  @Override
  public <E extends EntityBean> E selectById(Id<E> id, E prototype) {

    SelectStatement<E> select = new SelectEntity<>(prototype).from().where(prototype.Id().eq(id)).get();
    Iterable<E> entities = select(select);
    E result = null;
    for (E entity : entities) {
      if (result == null) {
        result = entity;
      } else {
        throw new DuplicateObjectException(entity, id, result);
      }
    }
    return result;
  }

  @Override
  public void update(EntityBean entity) {

    PkProperty pk = entity.Id();
    AbstractId<?, ?, ?> id = (AbstractId<?, ?, ?>) pk.get();
    if (id.isTransient()) {
      throw new IllegalStateException(
          "Cannot update entity of type " + entity.getType().getQualifiedName() + " because it is transient.");
    }
    JdbcEntitySession entitySession = getSession().get(entity);
    EntityBean managed = entitySession.get(id);
    if (managed == null) {
      throw new IllegalStateException("Cannot update entity of type " + entity.getType().getQualifiedName()
          + " with ID " + id + " because it is not managed in the current transaction!");
    } else if (managed.getId().getRevision() != id.getRevision()) {
      throw new OptimisicLockException(id, entity.getType().getQualifiedName());
    }
    GenericId<?, ?, ?> newId = id.updateRevision();
    Update<EntityBean> updateEntity = new Update<>(entity);
    UpdateSet<EntityBean> set = null;
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
        if (set == null) {
          set = updateEntity.set(assignment);
        } else {
          set = set.and(assignment);
        }
      }
    }
    if (set == null) {
      LOG.debug("Omitting update of {} with ID {} because nothing has changed.", entity.getType().getStableName(), id);
      return;
    }
    UpdateStatement<EntityBean> update = set.where(pk.eq(id))
        .and(CriteriaPredicate.of(COLUMN_REVISION, PredicateOperator.EQ, id.getRevision())).get();
    long updateCount = executeStatement(update);
    if (updateCount == 0) {
      throw new OptimisicLockException(id, entity.getType().getQualifiedName());
    }
    assert (updateCount == 1);
    entity.setId(newId);
    // TODO only copy non-transient properties...
    BeanHelper.copy(entity, managed);
  }

  @Override
  protected long executeSql(String sql, AbstractCriteriaParameters parameters) {

    try {
      Connection connection = getSession().getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
      parameters.apply(statement, connection);
      // TODO support batch updates...
      return statement.executeLargeUpdate();
    } catch (SQLException e) {
      // TODO proper custom runtinme exception class and error message including the SQL
      throw new IllegalStateException(e);
    }
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
