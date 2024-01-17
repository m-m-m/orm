package io.github.mmm.orm.statement.alter;

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
import io.github.mmm.orm.ddl.operation.TableOperation;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.value.PropertyPath;

/**
 * Interface for a fragment or clause to add {@link TableOperation}s.
 *
 * @param <E> type of the {@link AlterTableClause#getEntity() entity}.
 */
public interface AlterTableFragment<E extends EntityBean> {

  /**
   * @param property the {@link PropertyPath property} to add as column.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default AlterTableOperations<E> addColumn(ReadableProperty<?> property) {

    return addColumn(new DbColumnSpec(property));
  }

  /**
   * @param column the {@link DbColumnSpec column} to add.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  AlterTableOperations<E> addColumn(DbColumnSpec column);

  /**
   * @param property the {@link PropertyPath} to add.
   * @param autoConstraints - {@code true} to automatically add constraints, {@code false} otherwise (to only add the
   *        property as column).
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default AlterTableOperations<E> addColumn(ReadableProperty<?> property, boolean autoConstraints) {

    Objects.requireNonNull(property, "properety");
    DbColumnSpec column = new DbColumnSpec(property);
    if (autoConstraints) {
      if (property.getMetadata().getValidator().isMandatory()) {
        return addColumnNotNull(column);
      } else if (property instanceof PkProperty) {
        addColumn(column);
        PrimaryKeyConstraint constraint = new PrimaryKeyConstraint(column);
        return addConstraint(constraint);
      } else if (property instanceof FkProperty<?> fk) {
        return addColumnForeignKey(fk);
      } else if (property instanceof LinkProperty<?> link) {
        return addColumnForeignKey(link);
      }
    }
    return addColumn(column);
  }

  /**
   * @param constraint the {@link DbConstraint} to add.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  AlterTableOperations<E> addConstraint(DbConstraint constraint);

  /**
   * @param column the {@link DbColumnSpec column} to add with {@link NotNullConstraint}.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default AlterTableOperations<E> addColumnNotNull(DbColumnSpec column) {

    addColumn(column);
    NotNullConstraint constraint = new NotNullConstraint(column);
    return addConstraint(constraint);
  }

  /**
   * @param column the {@link DbColumnSpec column} to add with {@link UniqueConstraint}.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default AlterTableOperations<E> addColumnUnique(DbColumnSpec column) {

    addColumn(column);
    UniqueConstraint constraint = new UniqueConstraint(column);
    return addConstraint(constraint);
  }

  /**
   * @param property the {@link IdProperty} to add as column with {@link ForeignKeyConstraint}.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default AlterTableOperations<E> addColumnForeignKey(FkProperty<?> property) {

    DbColumnSpec column = new DbColumnSpec(property);
    addColumn(column);
    return addFkConstraint(column, property.get().getEntityClass());
  }

  /**
   * @param property the {@link LinkProperty} to add as column with {@link ForeignKeyConstraint}.
   * @return this {@link AlterTableOperations} for fluent API calls.
   */
  default AlterTableOperations<E> addColumnForeignKey(LinkProperty<?> property) {

    DbColumnSpec column = new DbColumnSpec(property);
    addColumn(column);
    return addFkConstraint(column, property.getEntityClass());
  }

  private AlterTableOperations<E> addFkConstraint(DbColumnSpec column, Class<? extends EntityBean> entityClass) {

    EntityBean targetEntity = BeanFactory.get().create(entityClass);
    DbColumnSpec referenceColumn = new DbColumnSpec(targetEntity.Id());
    ForeignKeyConstraint constraint = new ForeignKeyConstraint(column, referenceColumn);
    return addConstraint(constraint);
  }

  /**
   * @param property the {@link PropertyPath property} to drop as column.
   * @return the {@link AlterTableOperations} for fluent API calls.
   */
  default AlterTableOperations<E> dropColumn(ReadableProperty<?> property) {

    return dropColumn(new DbColumnSpec(property));
  }

  /**
   * @param column the {@link DbColumnSpec column} to drop.
   * @return the {@link AlterTableOperations} for fluent API calls.
   */
  AlterTableOperations<E> dropColumn(DbColumnSpec column);

  /**
   * @param constraint the {@link DbConstraint} to drop.
   * @return the {@link AlterTableOperations} for fluent API calls.
   */
  AlterTableOperations<E> dropConstraint(DbConstraint constraint);

  /**
   * @param constraint the {@link DbConstraint#getName() name of the constraint} to drop.
   * @return the {@link AlterTableOperations} for fluent API calls.
   */
  AlterTableOperations<E> dropConstraint(String constraint);

  /**
   * @param column the {@link DbColumnSpec column} to rename.
   * @param newColumn the new {@link DbColumnSpec column} to rename to.
   * @return the {@link AlterTableOperations} for fluent API calls.
   */
  AlterTableOperations<E> renameColumn(DbColumnSpec column, DbColumnSpec newColumn);

  /**
   * @param constraint the {@link DbConstraint#getName() name of the constraint} to rename.
   * @param newName the new {@link DbConstraint#getName() constraint name}.
   * @return the {@link AlterTableOperations} for fluent API calls.
   */
  AlterTableOperations<E> renameConstraint(String constraint, String newName);

  /**
   * @param constraint the {@link DbConstraint} to rename.
   * @param newName the new {@link DbConstraint#getName() constraint name}.
   * @return the {@link AlterTableOperations} for fluent API calls.
   */
  AlterTableOperations<E> renameConstraint(DbConstraint constraint, String newName);

}
