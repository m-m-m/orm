package io.github.mmm.orm.spi.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.mapping.DbBeanMapper;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.param.AbstractCriteriaParameters;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultRow;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.DbStatementFormatter;
import io.github.mmm.orm.statement.create.CreateIndexStatement;
import io.github.mmm.orm.statement.create.CreateTableStatement;
import io.github.mmm.orm.statement.delete.Delete;
import io.github.mmm.orm.statement.delete.DeleteStatement;
import io.github.mmm.orm.statement.insert.InsertStatement;
import io.github.mmm.orm.statement.merge.MergeStatement;
import io.github.mmm.orm.statement.select.Select;
import io.github.mmm.orm.statement.select.SelectStatement;
import io.github.mmm.orm.statement.update.UpdateStatement;
import io.github.mmm.orm.statement.upsert.UpsertStatement;

/**
 * Abstract base implementation of {@link DbAccess}.
 */
public abstract class AbstractDbAccess implements DbAccess {

  /**
   * @return the {@link DbDialect}.
   */
  protected abstract DbDialect getDialect();

  protected abstract long executeSql(String sql, AbstractCriteriaParameters parameters, Consumer<DbResult> receiver);

  protected long executeStatement(DbStatement<?> statement, Consumer<DbResult> receiver) {

    DbStatementFormatter formatter = getDialect().createFormatter();
    String sql = formatter.onStatement(statement).toString();
    AbstractCriteriaParameters parameters = (AbstractCriteriaParameters) formatter.getCriteriaFormatter()
        .getParameters();
    if (statement.getType().isQuery()) {
      Objects.requireNonNull(receiver);
    }
    return executeSql(sql, parameters, receiver);
  }

  @Override
  public void createIndex(CreateIndexStatement<?> statement) {

    executeStatement(statement, null);
  }

  @Override
  public void createTable(CreateTableStatement<?> statement) {

    executeStatement(statement, null);
  }

  @Override
  public long delete(DeleteStatement<?> statement) {

    return executeStatement(statement, null);
  }

  @Override
  public <E extends EntityBean> boolean deleteById(Id<E> id, E prototype) {

    if ((id == null) || (id.get() == null)) {
      return false;
    }
    DeleteStatement<E> statement = new Delete().from(prototype).where(prototype.Id().eq(id)).get();
    long count = delete(statement);
    if (count > 0) {
      assert (count == 1);
      return true;
    } else {
      assert (count == 0);
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <E extends EntityBean> int deleteAllById(Iterable<Id<E>> ids, E prototype) {

    if (ids == null) {
      return 0;
    }
    Collection<Id<?>> idCollection;
    if (ids instanceof Collection c) {
      idCollection = c;
    } else {
      idCollection = new ArrayList<>();
      for (Id<E> id : ids) {
        idCollection.add(id);
      }
    }
    if (idCollection.isEmpty()) {
      return 0;
    }
    DeleteStatement<E> statement = new Delete().from(prototype).where(prototype.Id().in(idCollection)).get();
    long count = executeStatement(statement, null);
    assert (count >= 0) && (count < Integer.MAX_VALUE);
    return (int) count;
  }

  @Override
  public void insert(InsertStatement<?> statement) {

    executeStatement(statement, null);
  }

  @Override
  public long execute(MergeStatement<?> statement) {

    return executeStatement(statement, null);
  }

  @Override
  public long update(UpdateStatement<?> statement) {

    return executeStatement(statement, null);
  }

  @Override
  public long execute(UpsertStatement<?> statement) {

    return executeStatement(statement, null);
  }

  @Override
  public <E extends EntityBean> Iterable<E> select(SelectStatement<E> statement) {

    Orm orm = ((AbstractDbDialect<?>) getDialect()).getOrm();
    Select<E> select = statement.getSelect();
    DbBeanMapper<E> beanMapping = orm.createBeanMapping(select.getResultBean());
    Consumer<DbResult> receiver = r -> {
    };
    executeStatement(statement, receiver);
    DbResultRow dbResult = null;
    E entity = beanMapping.db2java(dbResult);
    return null;
  }

  @Override
  public <E extends EntityBean> E selectOne(SelectStatement<E> statement) {

    Iterator<E> entities = select(statement).iterator();
    E entity = null;
    while (entities.hasNext()) {
      E next = entities.next();
      Objects.requireNonNull(next);
      if (entity == null) {
        entity = next;
      } else {
        throw new DuplicateObjectException(entity, statement, next);
      }
    }
    return entity;
  }

}
