package io.github.mmm.orm.spi.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.sequence.IdSequence;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.mapping.DbMapper;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.param.AbstractCriteriaParameters;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.statement.AbstractDbStatementFormatter;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.create.CreateIndexStatement;
import io.github.mmm.orm.statement.create.CreateSequenceStatement;
import io.github.mmm.orm.statement.create.CreateTableStatement;
import io.github.mmm.orm.statement.delete.DeleteClause;
import io.github.mmm.orm.statement.delete.DeleteStatement;
import io.github.mmm.orm.statement.insert.InsertStatement;
import io.github.mmm.orm.statement.merge.MergeStatement;
import io.github.mmm.orm.statement.select.SelectClause;
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
  protected abstract AbstractDbDialect<?> getDialect();

  protected abstract long executeSql(String sql, AbstractCriteriaParameters parameters, Consumer<DbResult> receiver,
      boolean unique);

  protected long executeStatement(DbStatement<?> statement) {

    return executeStatement(statement, null, true);
  }

  protected long executeStatement(DbStatement<?> statement, Consumer<DbResult> receiver, boolean unique) {

    AbstractDbStatementFormatter formatter = getDialect().createFormatter();
    String sql = formatter.formatStatement(statement).toString();
    AbstractCriteriaParameters parameters = (AbstractCriteriaParameters) formatter.getCriteriaFormatter()
        .getParameters();
    if (statement.getType().isQuery()) {
      Objects.requireNonNull(receiver);
    }
    return executeSql(sql, parameters, receiver, unique);
  }

  @Override
  public void createTable(CreateTableStatement<?> statement) {

    executeStatement(statement);
  }

  @Override
  public void createIndex(CreateIndexStatement<?> statement) {

    executeStatement(statement);
  }

  @Override
  public void createSequence(CreateSequenceStatement statement) {

    executeStatement(statement);
  }

  @Override
  public long delete(DeleteStatement<?> statement) {

    return executeStatement(statement);
  }

  @Override
  public <E extends EntityBean> boolean deleteById(Id<E> id, E prototype) {

    if ((id == null) || (id.get() == null)) {
      return false;
    }
    DeleteStatement<E> statement = new DeleteClause().from(prototype).where(prototype.Id().eq(id)).get();
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
    DeleteStatement<E> statement = new DeleteClause().from(prototype).where(prototype.Id().in(idCollection)).get();
    long count = executeStatement(statement);
    assert (count >= 0) && (count < Integer.MAX_VALUE);
    return (int) count;
  }

  @Override
  public void insert(InsertStatement<?> statement) {

    executeStatement(statement);
  }

  @Override
  public long execute(MergeStatement<?> statement) {

    return executeStatement(statement);
  }

  @Override
  public long update(UpdateStatement<?> statement) {

    return executeStatement(statement);
  }

  @Override
  public long execute(UpsertStatement<?> statement) {

    return executeStatement(statement);
  }

  @Override
  public <R> Iterable<R> select(SelectStatement<R> statement) {

    Orm orm = getDialect().getOrm();
    SelectClause<R> select = statement.getSelect();
    DbMapper<R> mapper = orm.createMapper(select);
    DbResultReceiverMultiple<R> receiver = new DbResultReceiverMultiple<>(mapper);
    executeStatement(statement, receiver, false);
    return receiver.getResults();
  }

  @Override
  public <R> R selectOne(SelectStatement<R> statement) {

    Orm orm = getDialect().getOrm();
    SelectClause<R> select = statement.getSelect();
    DbMapper<R> mapper = orm.createMapper(select);
    DbResultReceiverSingle<R> receiver = new DbResultReceiverSingle<>(mapper);
    executeStatement(statement, receiver, true);
    return receiver.getResult();
  }

  /**
   * @param sequenceName the {@link DbQualifiedName} of the database sequence.
   * @return the {@link IdSequence} implementation.
   */
  public abstract IdSequence createIdSequence(DbQualifiedName sequenceName);

}
