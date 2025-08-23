/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.io.AppendableWriter;
import io.github.mmm.bean.mapping.ClassNameMapper;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.PkMapper;
import io.github.mmm.orm.criteria.SequenceNextValue;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.ddl.DbElement;
import io.github.mmm.orm.ddl.constraint.CheckConstraint;
import io.github.mmm.orm.ddl.constraint.DbConstraint;
import io.github.mmm.orm.ddl.constraint.DbConstraintDeferrable;
import io.github.mmm.orm.ddl.constraint.DbConstraintInitially;
import io.github.mmm.orm.ddl.constraint.DbConstraintRely;
import io.github.mmm.orm.ddl.constraint.DbConstraintState;
import io.github.mmm.orm.ddl.constraint.ForeignKeyConstraint;
import io.github.mmm.orm.ddl.constraint.PrimaryKeyConstraint;
import io.github.mmm.orm.ddl.operation.TableColumnOperation;
import io.github.mmm.orm.ddl.operation.TableConstraintOperation;
import io.github.mmm.orm.ddl.operation.TableOperation;
import io.github.mmm.orm.ddl.operation.TableOperationType;
import io.github.mmm.orm.ddl.operation.TableRenameConstraintOperation;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbContext;
import io.github.mmm.orm.mapping.DbBeanMapper;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.naming.DbNamingStrategy;
import io.github.mmm.orm.param.CriteriaParametersFactory;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;
import io.github.mmm.orm.statement.alter.AlterTableClause;
import io.github.mmm.orm.statement.alter.AlterTableOperations;
import io.github.mmm.orm.statement.create.CreateIndexClause;
import io.github.mmm.orm.statement.create.CreateIndexColumns;
import io.github.mmm.orm.statement.create.CreateIndexOnClause;
import io.github.mmm.orm.statement.create.CreateSequenceClause;
import io.github.mmm.orm.statement.create.CreateTableClause;
import io.github.mmm.orm.statement.create.CreateTableContentsClause;
import io.github.mmm.orm.statement.delete.DeleteClause;
import io.github.mmm.orm.statement.impl.CriteriaJqlParametersInline;
import io.github.mmm.orm.statement.insert.InsertClause;
import io.github.mmm.orm.statement.insert.InsertValues;
import io.github.mmm.orm.statement.merge.MergeClause;
import io.github.mmm.orm.statement.select.GroupByClause;
import io.github.mmm.orm.statement.select.HavingClause;
import io.github.mmm.orm.statement.select.OrderByClause;
import io.github.mmm.orm.statement.select.SelectClause;
import io.github.mmm.orm.statement.select.SelectFrom;
import io.github.mmm.orm.statement.select.SelectSequenceNextValueClause;
import io.github.mmm.orm.statement.select.SelectStatement;
import io.github.mmm.orm.statement.update.UpdateClause;
import io.github.mmm.orm.statement.upsert.UpsertClause;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.criteria.BooleanLiteral;
import io.github.mmm.property.criteria.CriteriaExpression;
import io.github.mmm.property.criteria.CriteriaFormatter;
import io.github.mmm.property.criteria.CriteriaOperator;
import io.github.mmm.property.criteria.CriteriaOrdering;
import io.github.mmm.property.criteria.CriteriaPredicate;
import io.github.mmm.property.criteria.Literal;
import io.github.mmm.property.criteria.PredicateOperator;
import io.github.mmm.property.criteria.ProjectionProperty;
import io.github.mmm.property.criteria.PropertyAssignment;
import io.github.mmm.value.CriteriaObject;
import io.github.mmm.value.PropertyPath;
import io.github.mmm.value.SimplePath;
import io.github.mmm.value.converter.TypeMapper;

/**
 * Formatter to format a {@link DbClause} or {@link DbStatement} to SQL.
 *
 * @since 1.0.0
 */
public class BasicDbStatementFormatter extends CriteriaFormatter implements DbStatementFormatter {

  /** Default value for {@link #getIndentation() indentation}. */
  protected static final String INDENTATION = "  ";

  private static final Logger LOG = LoggerFactory.getLogger(BasicDbStatementFormatter.class);

  private static final CriteriaPredicate PARENT_AND = PredicateOperator.AND.expression(List.of(BooleanLiteral.TRUE));

  /** The {@link AbstractDbDialect} or {@code null} for generic formatter. */
  protected final AbstractDbDialect<?> dialect;

  private final StringBuilder sb;

  private CriteriaParametersFactory parametersFactory;

  private DbPlainStatement plainStatement;

  private final String indentation;

  private int indent;

  private int line;

  BasicDbStatementFormatter() {

    this(INDENTATION);
  }

  BasicDbStatementFormatter(String indentation) {

    this(null, CriteriaJqlParametersInline.FACTORY, indentation);
  }

  /**
   * The constructor.
   *
   * @param dialect the owning {@link AbstractDbDialect}.
   * @param parametersFactory the {@link CriteriaParametersFactory}.
   */
  public BasicDbStatementFormatter(AbstractDbDialect<?> dialect, CriteriaParametersFactory parametersFactory) {

    this(dialect, parametersFactory, INDENTATION);
  }

  /**
   * The constructor.
   *
   * @param dialect the owning {@link AbstractDbDialect}.
   * @param parametersFactory the {@link CriteriaParametersFactory}.
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public BasicDbStatementFormatter(AbstractDbDialect<?> dialect, CriteriaParametersFactory parametersFactory,
      String indentation) {

    this(dialect, parametersFactory, indentation, new StringBuilder(32));
  }

  private BasicDbStatementFormatter(AbstractDbDialect<?> dialect, CriteriaParametersFactory parametersFactory,
      String indentation, StringBuilder sb) {

    super(parametersFactory.create(dialect), new AppendableWriter(sb));
    this.dialect = dialect;
    this.sb = sb;
    this.parametersFactory = parametersFactory;
    this.indentation = indentation;
  }

  /**
   * @return the indentation (e.g. " " or "") or {@code null} to format as single line.
   */
  protected String getIndentation() {

    return this.indentation;
  }

  /**
   * @return the {@link Orm object/relational mapping}.
   */
  protected Orm getOrm() {

    if (this.dialect == null) {
      return null;
    }
    return this.dialect.getOrm();
  }

  @Override
  public void onPropertyPath(PropertyPath<?> property, int i, CriteriaExpression<?> parent) {

    DbNamingStrategy namingStrategy = this.dialect.getNamingStrategy();
    String columnName = namingStrategy.getColumnName(property);
    write(columnName);
  }

  /**
   * Starts a new {@link DbPlainStatement} in case multiple statements are needed.<br>
   * <b>ATTENTION:</b> When producing multiple plain statements, they have to be created in reverse order due to the
   * nature of {@link DbPlainStatement#getNext()}.
   */
  protected void newStatement() {

    this.plainStatement = new DbPlainStatement(this.out.toString(), this.parameters, this.plainStatement);
    this.parameters = this.parametersFactory.create(this.dialect);
    this.sb.setLength(0);
  }

  /**
   * @param name the {@link DbName} to format.
   */
  protected void formatName(String name) {

    formatUnqualifiedName(DbName.of(name));
  }

  /**
   * @param name the {@link DbName} to format.
   */
  protected void formatUnqualifiedName(DbName name) {

    write(name.toString(this.dialect));
  }

  /**
   * @param qName the {@link DbQualifiedName} to format.
   */
  protected void formatQualifiedName(DbQualifiedName qName) {

    DbName catalog = qName.getCatalog();
    DbName schema = qName.getSchema();
    DbName name = qName.getName();
    if ((catalog == null) && (schema == null)) {
      formatUnqualifiedName(name);
    } else {
      if (catalog != null) {
        write(catalog.toString(this.dialect));
        write(".");
      }
      if (schema != null) {
        write(schema.toString(this.dialect));
        write(".");
      }
      write(name.toString(this.dialect));
    }
  }

  private void formatName(DbElement element) {

    String name;
    if (element.hasName() || (this.dialect == null)) {
      name = element.getName();
    } else {
      name = element.createName(this.dialect.getNamingStrategy());
    }
    formatName(name);
  }

  @Override
  public DbPlainStatement formatStatement(DbStatement<?> statement, DbContext context) {

    for (DbClause clause : statement.getClauses()) {
      formatClause(clause, context);
    }
    return get();
  }

  /**
   * @param clause the {@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatClause(DbClause clause, DbContext context) {

    if (!clause.isOmit()) {
      if (clause instanceof StartClause) {
        formatStartClause((StartClause) clause, context);
      } else if (clause instanceof MainDbClause) {
        formatMainClause((MainDbClause<?>) clause, context);
      } else {
        formatOtherClause(clause, context);
      }
    }
  }

  /**
   * @param start the {@link StartClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatStartClause(StartClause start, DbContext context) {

    if (start instanceof SelectClause) {
      formatSelectClause((SelectClause<?>) start, context);
    } else if (start instanceof UpdateClause update) {
      formatUpdateClause(update, context);
    } else if (start instanceof InsertClause insert) {
      formatInsertClause(insert, context);
    } else if (start instanceof DeleteClause delete) {
      formatDeleteClause(delete, context);
    } else if (start instanceof MergeClause merge) {
      formatMergeClause(merge, context);
    } else if (start instanceof UpsertClause upsert) {
      formatUpsertClause(upsert, context);
    } else if (start instanceof CreateTableClause<?> createTable) {
      formatCreateTableClause(createTable, context);
    } else if (start instanceof CreateIndexClause createIndex) {
      formatCreateIndexClause(createIndex, context);
    } else if (start instanceof CreateSequenceClause seq) {
      formatCreateSequenceClause(seq, context);
    } else if (start instanceof AlterTableClause<?> alterTable) {
      formatAlterTableClause(alterTable, context);
    }
  }

  /**
   * @param clause the {@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatMainClause(MainDbClause<?> clause, DbContext context) {

    if (clause instanceof FromClause<?, ?, ?> from) {
      formatFromClause(from, context);
    } else if (clause instanceof WhereClause<?, ?> where) {
      formatWhereClause(where, context);
    } else if (clause instanceof GroupByClause<?> groupBy) {
      formatGroupByClause(groupBy, context);
    } else if (clause instanceof HavingClause<?> having) {
      formatHavingClause(having, context);
    } else if (clause instanceof OrderByClause<?> orderBy) {
      formatOrderByClause(orderBy, context);
    } else if (clause instanceof ValuesClause<?, ?> values) {
      formatValuesClause(values, context);
    } else if (clause instanceof SetClause<?, ?> set) {
      formatSetClause(set, context);
    } else if (clause instanceof CreateTableContentsClause<?> columns) {
      formatCreateTableContentsClause(columns, context);
    } else if (clause instanceof CreateIndexColumns<?> columns) {
      formatCreateIndexColumnsClause(columns, context);
    } else if (clause instanceof AlterTableOperations<?> addColumns) {
      formatAlterTableOperationsClause(addColumns, context);
    }
  }

  /**
   * @param clause the {@link DbClause} that is neither a {@link StartClause} nor a {@link MainDbClause}.
   * @param context the {@link DbContext}.
   */
  public void formatOtherClause(DbClause clause, DbContext context) {

    if (clause instanceof IntoClause<?, ?, ?> into) {
      formatIntoClause(into, context);
    } else if (clause instanceof CreateIndexOnClause<?> on) {
      formatCreateIndexOnClause(on, context);
    }
  }

  /**
   * Writes an indentation as needed.
   */
  protected void writeIndent() {

    if (this.indentation == null) {
      return;
    }
    if (this.line > 0) {
      write("\n");
    }
    this.line++;
    if (!this.indentation.isEmpty() && this.indent > 0) {
      for (int i = this.indent; i > 0; i--) {
        write(this.indentation);
      }
    }
  }

  private void incIndent() {

    this.indent++;
  }

  private void decIndent() {

    assert (this.indent > 0);
    this.indent--;
  }

  /**
   * @param select the {@link SelectClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatSelectClause(SelectClause<?> select, DbContext context) {

    if (select instanceof SelectSequenceNextValueClause seq) {
      formatSelectSeqNextVal(seq);
      return;
    }
    writeIndent();
    write("SELECT ");
    SelectStatement<?> statement = select.getStatement();
    SelectFrom<?, ?> selectFrom = null;
    if (statement != null) {
      selectFrom = statement.getFrom();
    }
    formatSelections(select, selectFrom);
  }

  /**
   * @param seq the {@link SelectSequenceNextValueClause} to format.
   */
  protected void formatSelectSeqNextVal(SelectSequenceNextValueClause seq) {

    writeIndent();
    write("SELECT ");
    write(SequenceNextValue.NEXT_VALUE);
    write("(");
    formatQualifiedName(seq.getSequenceName());
    write(")");
  }

  /**
   * @param delete the {@link DeleteClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatDeleteClause(DeleteClause delete, DbContext context) {

    writeIndent();
    write("DELETE ");
  }

  /**
   * @param insert the {@link InsertClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatInsertClause(InsertClause insert, DbContext context) {

    writeIndent();
    write("INSERT ");
  }

  /**
   * @param update the {@link UpdateClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatUpdateClause(UpdateClause<?> update, DbContext context) {

    writeIndent();
    write("UPDATE ");
    formatEntities(update);
  }

  /**
   * @param merge the {@link MergeClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatMergeClause(MergeClause merge, DbContext context) {

    writeIndent();
    write("MERGE ");
  }

  /**
   * @param upsert the {@link UpsertClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatUpsertClause(UpsertClause upsert, DbContext context) {

    writeIndent();
    write("UPSERT");
  }

  /**
   * @param createTable the {@link CreateTableClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatCreateTableClause(CreateTableClause<?> createTable, DbContext context) {

    writeIndent();
    write("CREATE TABLE ");
    formatEntityName(createTable);
    write(" (");
  }

  /**
   * @param contents the {@link CreateTableContentsClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatCreateTableContentsClause(CreateTableContentsClause<?> contents, DbContext context) {

    incIndent();
    List<DbColumnSpec> columns = contents.getColumns();
    formatColumns(columns, contents.get().getCreateTable().getEntity(), true, false);
    if (!columns.isEmpty()) {
      write(",");
    }
    String s = "";
    List<DbConstraint> constraints = contents.getConstraints().stream()
        .sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).collect(Collectors.toList());
    for (DbConstraint constraint : constraints) {
      write(s);
      writeIndent();
      formatConstraint(TableOperationType.ADD, constraint);
      s = ",";
    }
    decIndent();
    writeIndent();
    write(")");
  }

  /**
   * @param alterTable the {@link AlterTableClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatAlterTableClause(AlterTableClause<?> alterTable, DbContext context) {

    writeIndent();
    write("ALTER TABLE ");
    formatEntityName(alterTable);
  }

  /**
   * @param select the {@link SelectClause} with the {@link SelectClause#getSelections() selections}.
   * @param selectFrom the {@link SelectFrom}.
   */
  protected void formatSelections(SelectClause<?> select, SelectFrom<?, ?> selectFrom) {

    List<? extends CriteriaObject<?>> selectionCriterias = select.getSelections();
    if (selectionCriterias.isEmpty()) {
      if (!select.isSelectEntity()) {
        LOG.info("Formatting invalid select statement.");
      }
      formatSelectAll(selectFrom);
    } else {
      String s;
      if (select.isSelectResult()) {
        s = "new (";
      } else if (select.isSelectEntity() || select.isSelectSingle()) {
        s = "(";
      } else {
        s = "new " + select.getResultName() + "(";
      }
      int i = 0;
      for (CriteriaObject<?> selection : selectionCriterias) {
        write(s);
        onArg(selection, i++, null);
        s = ", ";
      }
      write(") ");
    }
  }

  /**
   * @return {@code true} if a {@link SelectClause} of all properties should happen via {@link SelectFrom#getAlias()
   *         alias}, {@code false} otherwise (to simply use {@code *}). The default is {@code false}. Override to
   *         change. E.g. in JPQL you would write "SELECT a FROM Entity a ..." whereas in plain SQL you would write
   *         "SELECT * FROM Entity ..."
   */
  public boolean isSelectAllByAlias() {

    return true;
  }

  /**
   * @return {@code true} to use the {@code AS} keyword before an {@link SelectFrom#getAlias() alias} (e.g. "FROM Entity
   *         <b>AS</b> e"), {@code false} otherwise.
   */
  public boolean isUseAsBeforeAlias() {

    return false;
  }

  /**
   * @param selectFrom the {@link SelectFrom} giving access to the {@link SelectFrom#getAlias() alias}.
   */
  protected void formatSelectAll(SelectFrom<?, ?> selectFrom) {

    if (isSelectAllByAlias()) {
      write(selectFrom.getAlias());
      write(" ");
    } else {
      write("* ");
    }
  }

  /**
   * @param from the {@link FromClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatFromClause(FromClause<?, ?, ?> from, DbContext context) {

    write("FROM ");
    if ((from.getEntityName() == null) && (from.getEntity() == null)) {
      assert (from.get() instanceof SelectStatement);
    } else {
      formatEntities(from);
    }
  }

  /**
   * @param entities the {@link AbstractEntitiesClause} to format.
   */
  protected void formatEntities(AbstractEntitiesClause<?, ?, ?> entities) {

    formatEntity(entities);
    for (EntitySubClause<?, ?> entity : entities.getAdditionalEntities()) {
      formatAdditionalEntity(entity);
    }
  }

  /**
   * @param entity the {@link AbstractEntityClause} to format.
   */
  protected void formatEntity(AbstractEntityClause<?, ?, ?> entity) {

    formatEntity(entity, true);
  }

  /**
   * @param entity the {@link AbstractEntityClause} for which to format the {@link AbstractEntityClause#getEntityName()
   *        entity name} or table name and optional {@link #formatAlias(String, DbClause) alias}.
   * @param forceAlias - {@code true} to force an {@link #formatAlias(String, DbClause) alias}, {@code false} otherwise.
   */
  protected void formatEntity(AbstractEntityClause<?, ?, ?> entity, boolean forceAlias) {

    formatEntityName(entity);
    if (forceAlias || entity.hasAlias()) {
      formatAlias(entity.getAlias(), entity);
    }
  }

  /**
   * @param entityClause the {@link AbstractEntityClause} for which to format the
   *        {@link AbstractEntityClause#getEntityName() entity name} or table name.
   */
  protected void formatEntityName(AbstractEntityClause<?, ?, ?> entityClause) {

    write(entityClause.getEntityName());
  }

  /**
   * @param entity the {@link EntitySubClause} to format.
   */
  protected void formatAdditionalEntity(EntitySubClause<?, ?> entity) {

    write(", ");
    formatEntity(entity);
  }

  /**
   * @param alias the {@link EntitySubClause#getAlias() alias}.
   * @param clause the owning {@link AbstractEntityClause}.
   */
  protected void formatAlias(String alias, DbClause clause) {

    if (alias != null) {
      write(" ");
      if (isUseAsBeforeAlias()) {
        write("AS ");
      }
      write(alias);
    }
  }

  /**
   * @param where the {@link WhereClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatWhereClause(WhereClause<?, ?> where, DbContext context) {

    write(" WHERE ");
    onPredicateClause(where);
  }

  private void onPredicateClause(PredicateClause<?, ?> clause) {

    String s = "";
    for (CriteriaPredicate predicate : clause.getPredicates()) {
      write(s);
      onExpression(predicate, PARENT_AND);
      s = " AND ";
    }
  }

  /**
   * @param orderBy the {@link OrderByClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatOrderByClause(OrderByClause<?> orderBy, DbContext context) {

    write(" ORDER BY ");
    String s = "";
    for (CriteriaOrdering ordering : orderBy.getOrderings()) {
      write(s);
      onOrdering(ordering);
      s = ", ";
    }
  }

  /**
   * @param groupBy the {@link GroupByClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatGroupByClause(GroupByClause<?> groupBy, DbContext context) {

    write(" GROUP BY ");
    String s = "";
    for (PropertyPath<?> property : groupBy.getProperties()) {
      write(s);
      onPropertyPath(property, 0, null);
      s = ", ";
    }
  }

  /**
   * @param having the {@link HavingClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatHavingClause(HavingClause<?> having, DbContext context) {

    write(" HAVING ");
    String s = "";
    for (CriteriaPredicate predicate : having.getPredicates()) {
      write(s);
      onExpression(predicate);
      s = ", ";
    }
  }

  /**
   * @param into the {@link IntoClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatIntoClause(IntoClause<?, ?, ?> into, DbContext context) {

    write("INTO ");
    formatEntity(into, false);
  }

  /**
   * @param values the {@link ValuesClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatValuesClause(ValuesClause<?, ?> values, DbContext context) {

    boolean isInto = values instanceof InsertValues;
    List<PropertyAssignment<?>> assignments = values.getAssignments();
    List<CriteriaObject<?>> args = null;
    if (isInto) {
      args = new ArrayList<>(assignments.size());
      write("(");
    } else {
      write(" VALUES (");
    }
    String s = "";
    int i = 0;
    for (PropertyAssignment<?> assignment : assignments) {
      i = formatAssignmentRecursive(assignment.getProperty(), assignment.getValue(), i, false, args, values);
    }
    write(")");
    if (args != null) { // isInto
      write(" VALUES (");
      s = "";
      ;
      for (int argIndex = 0; argIndex < i; argIndex++) {
        write(s);
        onArg(args.get(argIndex), argIndex, null);
        s = ", ";
      }
      write(")");
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private CriteriaObject<?> mapValue(CriteriaObject<?> value, TypeMapper typeMapper) {

    if (value instanceof Literal<?> literal) {
      Object sourceValue = literal.get();
      if ((sourceValue instanceof Number) && (typeMapper instanceof PkMapper)) {
        return value;
      }
      Object mapped = typeMapper.toTarget(sourceValue);
      return Literal.of(mapped);
    } else if (value instanceof PropertyPath<?> property) {
      return mapProperty(property, typeMapper);
    } else if (value instanceof ProjectionProperty<?> projection) {
      return new ProjectionProperty(mapValue(projection.getSelection(), typeMapper),
          mapProperty(projection.getProperty(), typeMapper));
    } else {
      // currently unsupported...
      return value;
    }
  }

  @SuppressWarnings("rawtypes")
  private PropertyPath<?> mapProperty(PropertyPath<?> property, TypeMapper typeMapper) {

    String name = this.dialect.getNamingStrategy().getColumnName(property, typeMapper);
    return new SimplePath(property.parentPath(), name);
  }

  @SuppressWarnings("rawtypes")
  private int formatAssignmentRecursive(PropertyPath<?> property, CriteriaObject<?> value, int index, boolean assign,
      List<CriteriaObject<?>> args, DbClause clause) {

    if ((this.dialect != null) && (property instanceof ReadableProperty<?> p)) {
      TypeMapper typeMapper = p.getTypeMapper();
      if (typeMapper != null) {
        do {
          String name = this.dialect.getNamingStrategy().getColumnName(property, typeMapper);
          index = formatAssignmentRecursive(new SimplePath(property.parentPath(), name), mapValue(value, typeMapper),
              index, assign, args, clause);
          typeMapper = typeMapper.next();
        } while (typeMapper != null);
        return index;
      }
    }
    formatAssignment(property, value, index, assign, args, clause);
    return index + 1;
  }

  /**
   *
   * @param property the {@link PropertyAssignment#getProperty() property to assign}.
   * @param value the {@link PropertyAssignment#getValue() value to assign}.
   * @param index the index of the assignment in the potential list of assignments (0 for first).
   * @param assign - {@code true} for a regular assignment, {@code false} to only format the {@code value} and ignore
   *        the {@code property}.
   * @param args the optional {@link List} where to {@link List#add(Object) add} the {@code value} or {@code null}.
   * @param clause the owning {@link DbClause}.
   */
  protected void formatAssignment(PropertyPath<?> property, CriteriaObject<?> value, int index, boolean assign,
      List<CriteriaObject<?>> args, DbClause clause) {

    if (index > 0) {
      write(", ");
    }
    if (assign) {
      onPropertyPath(property, index, null);
      write(" = ");
      assert (args == null);
    }
    if (args == null) {
      onArg(value, index, null);
    } else {
      onPropertyPath(property, index, null);
      args.add(value);
    }
  }

  /**
   * @param set the {@link SetClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatSetClause(SetClause<?, ?> set, DbContext context) {

    write(" SET ");
    int i = 0;
    for (PropertyAssignment<?> assignment : set.getAssignments()) {
      i = formatAssignmentRecursive(assignment.getProperty(), assignment.getValue(), i, true, null, set);
    }
  }

  /**
   * @param columns the {@link Iterable} with the {@link DbColumnSpec columns} to format.
   * @param entity the {@link EntityBean} containing the columns.
   * @param withDeclaration - {@code true} to write column declarations, {@code false} otherwise (to omit declarations).
   * @param skipDecompose - {@code true} to skip decomposing columns via ORM (e.g. for PK-Constraint), {@code false}
   *        otherwise.
   */
  protected void formatColumns(Iterable<DbColumnSpec> columns, EntityBean entity, boolean withDeclaration,
      boolean skipDecompose) {

    String s = "";
    if (skipDecompose || (this.dialect == null)) {
      for (DbColumnSpec column : columns) {
        write(s);
        if (withDeclaration) {
          writeIndent();
        }
        formatName(column);
        if (withDeclaration) {
          formatDeclaration(column, column.getDeclaration());
        }
        s = ",";
      }
    } else {
      List<ReadableProperty<?>> properties = new ArrayList<>();
      for (DbColumnSpec column : columns) {
        properties.add(column.getProperty());
      }
      DbBeanMapper<EntityBean> mapping = this.dialect.getOrm().createBeanMapper(entity, properties);
      DbResult dbResult = mapping.java2db(entity);
      for (DbResultValue<?> dbValue : dbResult) {
        String columnName = dbValue.getName();
        write(s);
        if (withDeclaration) {
          writeIndent();
        }
        formatName(columnName);
        if (withDeclaration) {
          String declaration = dbValue.getDeclaration();
          write(" ");
          write(declaration);
        }
        s = ",";
      }
    }
  }

  private void formatDeclaration(DbColumnSpec column, String declaration) {

    if (declaration == null) {
      Class<?> valueClass = column.getProperty().getValueClass();
      declaration = ClassNameMapper.get().getNameOrQualified(valueClass);
    }
    write(" ");
    write(declaration);
  }

  /**
   * @param constraint the {@link DbConstraint} to format.
   */
  private void formatConstraint(TableOperationType type, DbConstraint constraint) {

    formatConstraintKeywordWithName(type, constraint);
    write(" ");
    formatConstraintType(constraint);
    write(" (");
    if (constraint instanceof CheckConstraint ck) {
      CriteriaPredicate predicate = ck.getPredicate();
      formatCriteriaExpression(predicate);
    } else {
      EntityBean entity = (EntityBean) constraint.getFirstColumn().getProperty().getMetadata().getLock();
      boolean isPk = constraint instanceof PrimaryKeyConstraint;
      formatColumns(constraint, entity, false, isPk);
    }
    write(")");
    if (constraint instanceof ForeignKeyConstraint fk) {
      formatConstraintFkReference(fk.getReferenceColumn());
    }
    formatConstraintState(constraint.getState());
  }

  private void formatConstraintType(DbConstraint constraint) {

    write(constraint.getType());
  }

  /**
   * @param referenceColumn the referenced {@link DbColumnSpec column} (the primary key to use as foreign key).
   */
  protected void formatConstraintFkReference(DbColumnSpec referenceColumn) {

    write(" REFERENCES ");
    formatName(referenceColumn.getTable());
    write("(");
    formatName(referenceColumn);
    write(")");

  }

  /**
   * Writes "CONSTRAINT «constraint_name»". May be overridden as needed for specific dialect (e.g. for MySQL flaws).
   *
   * @param type the {@link TableOperationType}.
   * @param constraint the {@link DbConstraint}.
   */
  protected void formatConstraintKeywordWithName(TableOperationType type, DbConstraint constraint) {

    write("CONSTRAINT ");
    formatName(constraint);
  }

  /**
   * @param state the {@link DbConstraintState}.
   * @see #formatConstraintStateInitially(DbConstraintInitially)
   * @see #formatConstraintStateDeferrable(DbConstraintDeferrable)
   * @see #formatConstraintStateRely(DbConstraintRely)
   */
  protected void formatConstraintState(DbConstraintState state) {

    formatConstraintStateInitially(state.getInitially());
    formatConstraintStateDeferrable(state.getDeferrable());
    formatConstraintStateRely(state.getRely());
  }

  /**
   * @param rely the {@link DbConstraintRely} to format.
   */
  protected void formatConstraintStateRely(DbConstraintRely rely) {

    switch (rely) {
      case RELY -> {
        write(" RELY");
      }
      case NORELY -> {
        write(" NORELY");
      }
      case DEFAULT -> {
      }
    }
  }

  /**
   * @param initially the {@link DbConstraintInitially} to format.
   */
  protected void formatConstraintStateInitially(DbConstraintInitially initially) {

    switch (initially) {
      case INITIALLY_DEFERRED -> {
        write(" INITIALLY DEFERRED");
      }
      case INITIALLY_IMMEDIATE -> {
        write(" INITIALLY IMMEDIATE");
      }
      case DEFAULT -> {
      }
    }
  }

  /**
   * @param deferrable the {@link DbConstraintDeferrable} to format.
   */
  protected void formatConstraintStateDeferrable(DbConstraintDeferrable deferrable) {

    switch (deferrable) {
      case DEFERRABLE -> {
        write(" DEFERRABLE");
      }
      case NOT_DEFERRABLE -> {
        write(" NOT DEFERRABLE");
      }
      case DEFAULT -> {
      }
    }
  }

  /**
   * @param predicate the {@link CriteriaPredicate} to format (for a check constraint).
   */
  protected void formatCriteriaExpression(CriteriaExpression<?> predicate) {

    CriteriaOperator operator = predicate.getOperator();
    if (PredicateOperator.isNullBased(operator)) {
      assert (predicate.getArgCount() == 1);
      formatCriteriaObject(predicate.getFirstArg());
      write(" ");
      formatOperator(operator);
    } else if (operator.isInfix()) {
      // TODO add parenthesis as needed according to priorities...
      boolean first = true;
      for (CriteriaObject<?> arg : predicate.getArgs()) {
        if (first) {
          first = false;
        } else {
          if (operator.isConjunction()) {
            write(" ");
            formatOperator(operator);
            write(" ");
          } else {
            formatOperator(operator);
          }
        }
        formatCriteriaObject(arg);
      }
    } else if (operator.isUnary()) {
      assert (predicate.getArgCount() == 1);
      formatOperator(operator);
      write("(");
      formatCriteriaObject(predicate.getFirstArg());
      write(")");
    }
  }

  /**
   * @param operator the {@link PredicateOperator} to format.
   */
  protected void formatOperator(CriteriaOperator operator) {

    write(operator.getSyntax());
  }

  /**
   * @param object the {@link CriteriaObject} to format.
   */
  protected void formatCriteriaObject(CriteriaObject<?> object) {

    if (object instanceof ReadableProperty<?> property) {
      formatColumn(property);
    } else if (object instanceof CriteriaExpression<?> expression) {
      formatCriteriaExpression(expression);
    } else if (object instanceof Literal<?> literal) {
      formatLiteral(literal);
    } else if (object instanceof PropertyPath<?> path) {
      formatPath(path);
    }
  }

  /**
   * @param path the {@link PropertyPath} to format.
   */
  private void formatPath(PropertyPath<?> path) {

    write(path.path());
  }

  /**
   * @param literal the {@link Literal} to format.
   */
  private void formatLiteral(Literal<?> literal) {

    write(literal.toString());
  }

  /**
   * @param property the {@link ReadableProperty} to format as column.
   */
  protected void formatColumn(ReadableProperty<?> property) {

    // TODO not entirely correct, may be multiple columns...
    formatName(new DbColumnSpec(property));
  }

  /**
   * @param createIndex the {@link CreateIndexClause}-{@link DbClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatCreateIndexClause(CreateIndexClause createIndex, DbContext context) {

    writeIndent();
    write("CREATE ");
    if (createIndex.isUnique()) {
      write("UNIQUE ");
    }
    write("INDEX ");
    formatName(createIndex.getName());
  }

  /**
   * @param on the {@link CreateIndexOnClause} to format.
   * @param context the {@link DbContext}.
   */
  public void formatCreateIndexOnClause(CreateIndexOnClause<?> on, DbContext context) {

    write(" ON ");
    formatEntity(on, false);
  }

  /**
   * @param columns the {@link CreateIndexColumns} to format.
   * @param context the {@link DbContext}.
   */
  public void formatCreateIndexColumnsClause(CreateIndexColumns<?> columns, DbContext context) {

    write(" (");
    formatColumns(columns.getColumns(), columns.get().getOn().getEntity(), false, false);
    write(")");
  }

  /**
   * @param seq the {@link CreateSequenceClause} to format.
   * @param context the {@link DbContext}.
   */
  protected void formatCreateSequenceClause(CreateSequenceClause seq, DbContext context) {

    writeIndent();
    write("CREATE SEQUENCE ");
    formatQualifiedName(seq.getSequenceName());
    formatCreateSequenceAttribute(" INCREMENT BY ", seq.getIncrementBy());
    formatCreateSequenceAttribute(" START WITH ", seq.getStartWith());
    formatCreateSequenceAttribute(" MINVALUE ", seq.getMinValue());
    formatCreateSequenceAttribute(" MAXVALUE ", seq.getMaxValue());
    Boolean cycle = seq.getCycle();
    if (cycle != null) {
      formatCreateSequenceCycle(cycle.booleanValue());
    }
  }

  /**
   * @param attribute the attribute to format.
   * @param value the {@link Number} value to format.
   */
  protected void formatCreateSequenceAttribute(String attribute, Number value) {

    if (value == null) {
      return;
    }
    write(attribute);
    write(value.toString());
  }

  /**
   * Formats the explicit cycle attribute of a sequence.
   *
   * @param cycle {@code true} for cycle, {@code false} otherwise (nocycle).
   */
  protected void formatCreateSequenceCycle(boolean cycle) {

    if (cycle) {
      write(" CYCLE");
    } else {
      write(" NOCYCLE");
    }
  }

  /**
   * @param operations the {@link AlterTableOperations} to format.
   * @param context the {@link DbContext}.
   */
  public void formatAlterTableOperationsClause(AlterTableOperations<?> operations, DbContext context) {

    String s = "\n";
    for (TableOperation operation : operations.getOperations()) {
      write(s);
      formatAlterTableOperation(operation);
      s = ",\n";
    }
  }

  /**
   * @param operation the {@link TableOperation} to format.
   */
  public void formatAlterTableOperation(TableOperation operation) {

    TableOperationType type = operation.getType();
    formatAlterTableOperationType(type);
    write(" ");
    if (operation instanceof TableColumnOperation columnOp) {
      formatAlterTableColumn(type);
      DbColumnSpec column = columnOp.getColumn();
      formatName(column);
      if ((type == TableOperationType.ADD) || (type == TableOperationType.MODIFY)) {
        formatDeclaration(column, column.getDeclaration());
      }
    } else if (operation instanceof TableConstraintOperation constraintOp) {
      DbConstraint constraint = constraintOp.getConstraint();
      if (type == TableOperationType.ADD) {
        formatConstraint(type, constraint);
      } else {
        String name;
        if (constraint == null) {
          name = constraintOp.getName();
        } else if (constraint.hasName() || (this.dialect == null)) {
          name = constraint.getName();
        } else {
          name = constraint.createName(this.dialect.getNamingStrategy());
        }
        formatName(name);
        if (operation instanceof TableRenameConstraintOperation rename) {
          write(" TO ");
          formatName(rename.getNewName());
        } else {
          assert (type == TableOperationType.DROP);
        }
      }
    }
  }

  /**
   * @param type the {@link TableOperationType} to format. May be overridden as needed for specific dialect (e.g. for MS
   *        SQL-Server flaws).
   */
  protected void formatAlterTableOperationType(TableOperationType type) {

    write(type.name());
  }

  /**
   * Write "COLUMN" keyword as needed for ALTER TABLE statement of the given {@code type}.
   *
   * @param type the {@link TableOperationType}.
   */
  protected void formatAlterTableColumn(TableOperationType type) {

    if ((type != TableOperationType.ADD) && (type != TableOperationType.MODIFY)) {
      write("COLUMN ");
    }
  }

  /**
   * @return the {@link #formatStatement(DbStatement, DbContext) formatted statement} as {@link String} (e.g. SQL).
   */
  public DbPlainStatement get() {

    return new DbPlainStatement(this.out.toString(), this.parameters, this.plainStatement);
  }

  @Override
  public String toString() {

    return get().toString();
  }

}
