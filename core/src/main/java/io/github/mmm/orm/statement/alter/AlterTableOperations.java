package io.github.mmm.orm.statement.alter;

import java.util.ArrayList;
import java.util.List;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.ddl.constraint.DbConstraint;
import io.github.mmm.orm.ddl.operation.TableAddColumnOperation;
import io.github.mmm.orm.ddl.operation.TableAddConstraintOperation;
import io.github.mmm.orm.ddl.operation.TableDropColumnOperation;
import io.github.mmm.orm.ddl.operation.TableDropConstraintOperation;
import io.github.mmm.orm.ddl.operation.TableOperation;
import io.github.mmm.orm.ddl.operation.TableRenameColumnOperation;
import io.github.mmm.orm.ddl.operation.TableRenameConstraintOperation;
import io.github.mmm.orm.statement.AbstractTypedClause;
import io.github.mmm.orm.statement.DbClause;
import io.github.mmm.orm.statement.MainDbClause;

/**
 * A {@link AlterTableOperations}-{@link DbClause} of an SQL {@link AlterTableStatement}.
 *
 * @param <E> type of the {@link io.github.mmm.orm.statement.AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public class AlterTableOperations<E extends EntityBean> extends AbstractTypedClause<E, AlterTableOperations<E>>
    implements MainDbClause<E>, AlterTableFragment<E> {

  private final AlterTableStatement<E> statement;

  private final List<TableOperation> operations;

  /**
   * The constructor.
   *
   * @param statement the {@link AlterTableStatement}.
   */
  public AlterTableOperations(AlterTableStatement<E> statement) {

    super();
    this.statement = statement;
    this.operations = new ArrayList<>();
  }

  @Override
  public AlterTableStatement<E> get() {

    return this.statement;
  }

  /**
   * @return the {@link List} of {@link TableOperation}s.
   */
  public List<TableOperation> getOperations() {

    return this.operations;
  }

  @Override
  public AlterTableOperations<E> addColumn(DbColumnSpec column) {

    this.operations.add(new TableAddColumnOperation(column));
    return this;
  }

  @Override
  public AlterTableOperations<E> addConstraint(DbConstraint constraint) {

    this.operations.add(new TableAddConstraintOperation(constraint));
    return this;
  }

  @Override
  public AlterTableOperations<E> dropColumn(DbColumnSpec column) {

    this.operations.add(new TableDropColumnOperation(column));
    return this;
  }

  @Override
  public AlterTableOperations<E> dropConstraint(DbConstraint constraint) {

    this.operations.add(new TableDropConstraintOperation(constraint));
    return this;
  }

  @Override
  public AlterTableOperations<E> dropConstraint(String constraint) {

    this.operations.add(new TableDropConstraintOperation(constraint));
    return this;
  }

  @Override
  public AlterTableOperations<E> renameColumn(DbColumnSpec column, DbColumnSpec newColumn) {

    this.operations.add(new TableRenameColumnOperation(column, newColumn));
    return this;
  }

  @Override
  public AlterTableOperations<E> renameConstraint(String constraint, String newName) {

    this.operations.add(new TableRenameConstraintOperation(constraint, newName));
    return this;
  }

  @Override
  public AlterTableOperations<E> renameConstraint(DbConstraint constraint, String newName) {

    this.operations.add(new TableRenameConstraintOperation(constraint, newName));
    return this;
  }

}
