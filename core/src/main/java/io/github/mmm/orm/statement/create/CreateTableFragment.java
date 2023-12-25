/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import java.util.Objects;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.property.id.FkProperty;
import io.github.mmm.entity.property.id.IdProperty;
import io.github.mmm.entity.property.id.PkProperty;
import io.github.mmm.entity.property.link.LinkProperty;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.ddl.constraint.DbConstraint;
import io.github.mmm.orm.ddl.constraint.ForeignKeyConstraint;
import io.github.mmm.orm.ddl.constraint.NotNullConstraint;
import io.github.mmm.orm.ddl.constraint.PrimaryKeyConstraint;
import io.github.mmm.orm.ddl.constraint.UniqueConstraint;
import io.github.mmm.orm.statement.alter.AlterTable;
import io.github.mmm.orm.statement.alter.AlterTableOperations;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.value.PropertyPath;

/**
 * Interface for a fragment or clause to add {@link DbColumnSpec #column(DbColumnName) columns} and #constraints.
 *
 * @param <E> type of the {@link AlterTable#getEntity() entity}.
 */
public interface CreateTableFragment<E extends EntityBean> {

  /**
   * @param property the {@link PropertyPath property} to add as column.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default CreateTableContents<E> column(ReadableProperty<?> property) {

    return column(new DbColumnSpec(property));
  }

  /**
   * @param column the {@link DbColumnSpec column} to add.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  CreateTableContents<E> column(DbColumnSpec column);

  /**
   * @param property the {@link PropertyPath} to add.
   * @param autoConstraints - {@code true} to automatically add constraints, {@code false} otherwise (to only add the
   *        property as column).
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default CreateTableContents<E> column(ReadableProperty<?> property, boolean autoConstraints) {

    Objects.requireNonNull(property, "properety");
    DbColumnSpec column = new DbColumnSpec(property);
    if (autoConstraints) {
      if (property.getMetadata().getValidator().isMandatory()) {
        return columnNotNull(column);
      } else if (property instanceof PkProperty) {
        column(column);
        PrimaryKeyConstraint constraint = new PrimaryKeyConstraint(column);
        return constraint(constraint);
      } else if (property instanceof FkProperty<?> fk) {
        return columnForeignKey(fk);
      } else if (property instanceof LinkProperty<?> link) {
        return columnForeignKey(link);
      }
    }
    return column(column);
  }

  /**
   * @param constraint the {@link DbConstraint} to add.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  CreateTableContents<E> constraint(DbConstraint constraint);

  /**
   * @param column the {@link DbColumnSpec column} to add with {@link NotNullConstraint}.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default CreateTableContents<E> columnNotNull(DbColumnSpec column) {

    column(column);
    NotNullConstraint constraint = new NotNullConstraint(column);
    return constraint(constraint);
  }

  /**
   * @param column the {@link DbColumnSpec column} to add with {@link UniqueConstraint}.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default CreateTableContents<E> columnUnique(DbColumnSpec column) {

    column(column);
    UniqueConstraint constraint = new UniqueConstraint(column);
    return constraint(constraint);
  }

  /**
   * @param property the {@link IdProperty} to add as column with {@link ForeignKeyConstraint}.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default CreateTableContents<E> columnForeignKey(FkProperty<?> property) {

    DbColumnSpec column = new DbColumnSpec(property);
    column(column);
    return constraintFk(column, property.get().getEntityClass());
  }

  /**
   * @param property the {@link LinkProperty} to add as column with {@link ForeignKeyConstraint}.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default CreateTableContents<E> columnForeignKey(LinkProperty<?> property) {

    DbColumnSpec column = new DbColumnSpec(property);
    column(column);
    return constraintFk(column, property.getEntityClass());
  }

  private CreateTableContents<E> constraintFk(DbColumnSpec column, Class<? extends EntityBean> entityClass) {

    EntityBean targetEntity = BeanFactory.get().create(entityClass);
    DbColumnSpec referenceColumn = new DbColumnSpec(targetEntity.Id());
    ForeignKeyConstraint constraint = new ForeignKeyConstraint(column, referenceColumn);
    return constraint(constraint);
  }

}
