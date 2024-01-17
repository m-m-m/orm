/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.List;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.marshall.MarshallingObject;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.statement.create.CreateTableClause;
import io.github.mmm.orm.statement.delete.DeleteClause;
import io.github.mmm.orm.statement.delete.DeleteFrom;
import io.github.mmm.orm.statement.drop.DropTable;
import io.github.mmm.orm.statement.insert.InsertClause;
import io.github.mmm.orm.statement.insert.InsertInto;
import io.github.mmm.orm.statement.merge.MergeClause;
import io.github.mmm.orm.statement.merge.MergeInto;
import io.github.mmm.orm.statement.select.SelectEntityClause;
import io.github.mmm.orm.statement.select.SelectFrom;
import io.github.mmm.orm.statement.select.SelectProjectionClause;
import io.github.mmm.orm.statement.select.SelectSequenceNextValueClause;
import io.github.mmm.orm.statement.select.SelectSingleClause;
import io.github.mmm.orm.statement.select.SelectStatement;
import io.github.mmm.orm.statement.update.UpdateClause;
import io.github.mmm.orm.statement.upsert.UpsertClause;
import io.github.mmm.orm.statement.upsert.UpsertInto;
import io.github.mmm.value.CriteriaObject;

/**
 * A complete SQL statement that may be executed to the database.
 *
 * @param <E> type of the entity or object this statement primarily operates on.
 * @since 1.0.0
 */
public abstract interface DbStatement<E> extends MarshallingObject {

  /**
   * @return the {@link StartClause}
   */
  StartClause getStart();

  /**
   * @return the {@link List} of {@link DbClause}s this {@link DbStatement} is composed of. Please note that this is a
   *         generic API. Specific sub-classes implementing {@link DbStatement} will have dedicated getters for each
   *         type of {@link DbClause}.
   */
  List<? extends DbClause> getClauses();

  /**
   * @return the {@link DbStatementType type} of this {@link DbStatement statement}.
   */
  DbStatementType getType();

  /**
   * Alternative for {@code new SelectEntity<>(entity).from()}.
   *
   * @param <E> type of the {@link EntityBean} to select.
   * @param entity the {@link EntityBean} to select.
   * @return the {@link SelectEntityClause} clause.
   */
  public static <E extends EntityBean> SelectFrom<E, E> select(E entity) {

    return new SelectEntityClause<>(entity).from();
  }

  /**
   * Alternative for {@code new SelectSingle<>(selection)}.
   *
   * @param <R> type of the result of the selection.
   * @param selection the single {@link CriteriaObject} to select.
   * @return the new {@link SelectSingleClause} clause.
   */
  public static <R> SelectSingleClause<R> select(CriteriaObject<R> selection) {

    return new SelectSingleClause<>(selection);
  }

  /**
   * Alternative for {@code new SelectProjection<>(bean)}.
   *
   * @param <R> type of the {@link WritableBean} to select.
   * @param bean the {@link WritableBean} to select.
   * @return the new {@link SelectProjectionClause} clause.
   */
  public static <R extends WritableBean> SelectProjectionClause<R> selectProjection(R bean) {

    return new SelectProjectionClause<>(bean);
  }

  /**
   * @param sequenceName the {@link DbName} of the sequence to select.
   * @return the {@link SelectStatement} to select the next value from the specified sequence.
   */
  public static SelectStatement<Long> selectSeqNextVal(DbName sequenceName) {

    return selectSeqNextVal(new DbQualifiedName(null, null, sequenceName));
  }

  /**
   * @param sequenceName the {@link DbQualifiedName} of the sequence to select.
   * @return the {@link SelectStatement} to select the next value from the specified sequence.
   */
  public static SelectStatement<Long> selectSeqNextVal(DbQualifiedName sequenceName) {

    return new SelectSequenceNextValueClause(sequenceName).getStatement();
  }

  /**
   * Alternative for {@code new Delete().from(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to delete.
   * @param entity the {@link EntityBean} to delete.
   * @return the {@link DeleteFrom} clause.
   */
  public static <E extends EntityBean> DeleteFrom<E> delete(E entity) {

    return new DeleteClause().from(entity);
  }

  /**
   * Alternative for {@code new Insert().into(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to insert.
   * @param entity the {@link EntityBean} to insert.
   * @return the {@link InsertInto} clause.
   */
  public static <E extends EntityBean> InsertInto<E> insert(E entity) {

    return new InsertClause().into(entity);
  }

  /**
   * Alternative for {@code new Update<>(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to update.
   * @param entity the {@link EntityBean} to update.
   * @return the {@link UpdateClause} clause.
   */
  public static <E extends EntityBean> UpdateClause<E> update(E entity) {

    return new UpdateClause<>(entity);
  }

  /**
   * Alternative for {@code new Upsert().into(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to update.
   * @param entity the {@link EntityBean} to update.
   * @return the {@link UpsertInto} clause.
   */
  public static <E extends EntityBean> UpsertInto<E> upset(E entity) {

    return new UpsertClause().into(entity);
  }

  /**
   * Alternative for {@code new Merge().into(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to update.
   * @param entity the {@link EntityBean} to update.
   * @return the {@link UpsertInto} clause.
   */
  public static <E extends EntityBean> MergeInto<E> merge(E entity) {

    return new MergeClause().into(entity);
  }

  /**
   * Alternative for {@code new CreateTable<>(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to create the table for.
   * @param entity the {@link EntityBean} to create the table for.
   * @return the {@link CreateTableClause} clause.
   */
  public static <E extends EntityBean> CreateTableClause<E> createTable(E entity) {

    return new CreateTableClause<>(entity);
  }

  /**
   * Alternative for {@code new DropTable<>(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to drop the table.
   * @param entity the {@link EntityBean} to create drop the table.
   * @return the {@link DropTable} clause.
   */
  public static <E extends EntityBean> DropTable<E> dropTable(E entity) {

    return new DropTable<>(entity);
  }
}
