/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.List;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.marshall.MarshallingObject;
import io.github.mmm.orm.statement.create.CreateTable;
import io.github.mmm.orm.statement.delete.Delete;
import io.github.mmm.orm.statement.delete.DeleteFrom;
import io.github.mmm.orm.statement.drop.DropTable;
import io.github.mmm.orm.statement.insert.Insert;
import io.github.mmm.orm.statement.insert.InsertInto;
import io.github.mmm.orm.statement.merge.Merge;
import io.github.mmm.orm.statement.merge.MergeInto;
import io.github.mmm.orm.statement.select.SelectEntity;
import io.github.mmm.orm.statement.select.SelectFrom;
import io.github.mmm.orm.statement.select.SelectProjection;
import io.github.mmm.orm.statement.select.SelectSingle;
import io.github.mmm.orm.statement.update.Update;
import io.github.mmm.orm.statement.upsert.Upsert;
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
   * @return the {@link SelectEntity} clause.
   */
  public static <E extends EntityBean> SelectFrom<E, E> select(E entity) {

    return new SelectEntity<>(entity).from();
  }

  /**
   * Alternative for {@code new SelectSingle<>(selection)}.
   *
   * @param <R> type of the result of the selection.
   * @param selection the single {@link CriteriaObject} to select.
   * @return the new {@link SelectSingle} clause.
   */
  public static <R> SelectSingle<R> select(CriteriaObject<R> selection) {

    return new SelectSingle<>(selection);
  }

  /**
   * Alternative for {@code new SelectProjection<>(bean)}.
   *
   * @param <R> type of the {@link WritableBean} to select.
   * @param bean the {@link WritableBean} to select.
   * @return the new {@link SelectProjection} clause.
   */
  public static <R extends WritableBean> SelectProjection<R> selectProjection(R bean) {

    return new SelectProjection<>(bean);
  }

  /**
   * Alternative for {@code new Delete().from(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to delete.
   * @param entity the {@link EntityBean} to delete.
   * @return the {@link DeleteFrom} clause.
   */
  public static <E extends EntityBean> DeleteFrom<E> delete(E entity) {

    return new Delete().from(entity);
  }

  /**
   * Alternative for {@code new Insert().into(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to insert.
   * @param entity the {@link EntityBean} to insert.
   * @return the {@link InsertInto} clause.
   */
  public static <E extends EntityBean> InsertInto<E> insert(E entity) {

    return new Insert().into(entity);
  }

  /**
   * Alternative for {@code new Update<>(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to update.
   * @param entity the {@link EntityBean} to update.
   * @return the {@link Update} clause.
   */
  public static <E extends EntityBean> Update<E> update(E entity) {

    return new Update<>(entity);
  }

  /**
   * Alternative for {@code new Upsert().into(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to update.
   * @param entity the {@link EntityBean} to update.
   * @return the {@link UpsertInto} clause.
   */
  public static <E extends EntityBean> UpsertInto<E> upset(E entity) {

    return new Upsert().into(entity);
  }

  /**
   * Alternative for {@code new Merge().into(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to update.
   * @param entity the {@link EntityBean} to update.
   * @return the {@link UpsertInto} clause.
   */
  public static <E extends EntityBean> MergeInto<E> merge(E entity) {

    return new Merge().into(entity);
  }

  /**
   * Alternative for {@code new CreateTable<>(entity)}.
   *
   * @param <E> type of the {@link EntityBean} to create the table for.
   * @param entity the {@link EntityBean} to create the table for.
   * @return the {@link CreateTable} clause.
   */
  public static <E extends EntityBean> CreateTable<E> createTable(E entity) {

    return new CreateTable<>(entity);
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
