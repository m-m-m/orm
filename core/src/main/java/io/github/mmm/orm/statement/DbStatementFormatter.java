/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.io.AppendableWriter;
import io.github.mmm.bean.mapping.ClassNameMapper;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.ddl.DbElement;
import io.github.mmm.orm.ddl.constraint.CheckConstraint;
import io.github.mmm.orm.ddl.constraint.DbConstraint;
import io.github.mmm.orm.ddl.constraint.ForeignKeyConstraint;
import io.github.mmm.orm.ddl.constraint.PrimaryKeyConstraint;
import io.github.mmm.orm.ddl.operation.TableColumnOperation;
import io.github.mmm.orm.ddl.operation.TableConstraintOperation;
import io.github.mmm.orm.ddl.operation.TableOperation;
import io.github.mmm.orm.ddl.operation.TableOperationType;
import io.github.mmm.orm.ddl.operation.TableRenameConstraintOperation;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.mapping.DbBeanMapper;
import io.github.mmm.orm.mapping.DbSelection;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;
import io.github.mmm.orm.statement.alter.AlterTable;
import io.github.mmm.orm.statement.alter.AlterTableOperations;
import io.github.mmm.orm.statement.create.CreateIndex;
import io.github.mmm.orm.statement.create.CreateIndexColumns;
import io.github.mmm.orm.statement.create.CreateIndexOn;
import io.github.mmm.orm.statement.create.CreateTable;
import io.github.mmm.orm.statement.create.CreateTableContents;
import io.github.mmm.orm.statement.delete.Delete;
import io.github.mmm.orm.statement.insert.Insert;
import io.github.mmm.orm.statement.insert.InsertValues;
import io.github.mmm.orm.statement.select.OrderBy;
import io.github.mmm.orm.statement.select.Select;
import io.github.mmm.orm.statement.select.SelectFrom;
import io.github.mmm.orm.statement.select.SelectStatement;
import io.github.mmm.orm.statement.update.Update;
import io.github.mmm.orm.statement.upsert.Upsert;
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
import io.github.mmm.property.criteria.SimplePath;
import io.github.mmm.value.CriteriaObject;
import io.github.mmm.value.PropertyPath;
import io.github.mmm.value.converter.TypeMapper;

/**
 * Formatter to format a {@link DbClause} or {@link DbStatement} to SQL.
 *
 * @since 1.0.0
 */
public class DbStatementFormatter implements DbClauseVisitor {

  /** Default value for {@link #getIndentation() indentation}. */
  protected static final String INDENTATION = "  ";

  private static final Logger LOG = LoggerFactory.getLogger(DbStatementFormatter.class);

  private static final CriteriaPredicate PARENT_AND = PredicateOperator.AND.expression(List.of(BooleanLiteral.TRUE));

  /** The {@link AbstractDbDialect} or {@code null} for generic formatter. */
  protected final AbstractDbDialect<?> dialect;

  private final CriteriaFormatter criteriaFormatter;

  private List<DbSelection<?>> selections;

  private final String indentation;

  private int indent;

  private int line;

  DbStatementFormatter() {

    this(new CriteriaSqlFormatterInline());
  }

  DbStatementFormatter(String indentation) {

    this(null, new CriteriaSqlFormatterInline(), indentation);
  }

  /**
   * The constructor.
   *
   * @param criteriaFormatter the {@link CriteriaFormatter} used to format criteria fragments to SQL.
   */
  public DbStatementFormatter(CriteriaFormatter criteriaFormatter) {

    this(null, criteriaFormatter, INDENTATION);
  }

  /**
   * The constructor.
   *
   * @param dialect the owning {@link AbstractDbDialect}.
   * @param criteriaFormatter the {@link CriteriaFormatter} used to format criteria fragments to SQL.
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public DbStatementFormatter(AbstractDbDialect<?> dialect, CriteriaFormatter criteriaFormatter, String indentation) {

    super();
    this.dialect = dialect;
    this.criteriaFormatter = criteriaFormatter;
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

  /**
   * @return the {@link CriteriaFormatter} used to format criteria fragments to database syntax (e.g. SQL).
   */
  public CriteriaFormatter getCriteriaFormatter() {

    return this.criteriaFormatter;
  }

  /**
   * @return the {@link Iterable} with the {@link DbSelection}s. Will be empty unless a {@link SelectStatement} has been
   *         formatted.
   */
  public Iterable<DbSelection<?>> getSelections() {

    if (this.selections == null) {
      return Collections.emptyList();
    }
    return this.selections;
  }

  /**
   * @param text the database syntax to append.
   */
  protected void write(String text) {

    this.criteriaFormatter.out().append(text);
  }

  /**
   * @param name the {@link DbName} to write.
   */
  protected void writeName(String name) {

    writeName(DbName.of(name));
  }

  /**
   * @param name the {@link DbName} to write.
   */
  protected void writeName(DbName name) {

    write(name.toString(this.dialect));
  }

  private void writeName(DbElement element) {

    String name;
    if (element.hasName() || (this.dialect == null)) {
      name = element.getName();
    } else {
      name = element.createName(this.dialect.getNamingStrategy());
    }
    writeName(name);
  }

  /**
   * @return the {@link AppendableWriter} wrapping the {@link Appendable} to write to.
   */
  protected AppendableWriter out() {

    return this.criteriaFormatter.out();
  }

  /**
   * @param statement the {@link DbStatement} to format as database syntax.
   * @return this {@link DbStatementFormatter} for fluent API calls.
   */
  public DbStatementFormatter onStatement(DbStatement<?> statement) {

    for (DbClause clause : statement.getClauses()) {
      onClause(clause);
    }
    return this;
  }

  @Override
  public DbStatementFormatter onClause(DbClause clause) {

    if (!clause.isOmit()) {
      DbClauseVisitor.super.onClause(clause);
    }
    return this;
  }

  private void writeIndent() {

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

  @Override
  public void onSelect(Select<?> select) {

    writeIndent();
    write("SELECT ");
    SelectStatement<?> statement = select.getStatement();
    SelectFrom<?, ?> selectFrom = null;
    if (statement != null) {
      selectFrom = statement.getFrom();
    }
    onSelections(select, selectFrom);
    DbClauseVisitor.super.onSelect(select);
  }

  @Override
  public void onDelete(Delete delete) {

    writeIndent();
    write("DELETE ");
    DbClauseVisitor.super.onDelete(delete);
  }

  @Override
  public void onInsert(Insert insert) {

    writeIndent();
    write("INSERT ");
    DbClauseVisitor.super.onInsert(insert);
  }

  @Override
  public void onUpdate(Update<?> update) {

    writeIndent();
    write("UPDATE ");
    onEntities(update);
    DbClauseVisitor.super.onUpdate(update);
  }

  @Override
  public void onUpsert(Upsert upsert) {

    writeIndent();
    write("UPSERT");
    DbClauseVisitor.super.onUpsert(upsert);
  }

  @Override
  public void onCreateTable(CreateTable<?> createTable) {

    writeIndent();
    write("CREATE TABLE ");
    writeEntityName(createTable);
    write(" (");
    DbClauseVisitor.super.onCreateTable(createTable);
  }

  @Override
  public void onCreateTableContents(CreateTableContents<?> contents) {

    incIndent();
    List<DbColumnSpec> columns = contents.getColumns();
    writeColumns(columns, contents.get().getCreateTable().getEntity(), true, false);
    if (!columns.isEmpty()) {
      write(",");
    }
    String s = "";
    List<DbConstraint> constraints = contents.getConstraints().stream()
        .sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).collect(Collectors.toList());
    for (DbConstraint constraint : constraints) {
      write(s);
      writeIndent();
      writeConstraint(TableOperationType.ADD, constraint);
      s = ",";
    }
    DbClauseVisitor.super.onCreateTableContents(contents);
    decIndent();
    writeIndent();
    write(")");
  }

  @Override
  public void onAlterTable(AlterTable<?> alterTable) {

    writeIndent();
    write("ALTER TABLE ");
    writeEntityName(alterTable);
    DbClauseVisitor.super.onAlterTable(alterTable);
  }

  /**
   * @param select the {@link Select} with the {@link Select#getSelections() selections}.
   * @param selectFrom the {@link SelectFrom}.
   */
  protected void onSelections(Select<?> select, SelectFrom<?, ?> selectFrom) {

    List<? extends CriteriaObject<?>> selectionCriterias = select.getSelections();
    if (selectionCriterias.isEmpty()) {
      if (!select.isSelectEntity()) {
        LOG.info("Formatting invalid select statement.");
      }
      onSelectAll(selectFrom);
    } else {
      String s;
      if (select.isSelectResult()) {
        s = " new (";
      } else if (select.isSelectEntity() || select.isSelectSingle()) {
        s = " (";
      } else {
        s = " new " + select.getResultName() + "(";
      }
      int i = 0;
      for (CriteriaObject<?> selection : selectionCriterias) {
        write(s);
        this.criteriaFormatter.onArg(selection, i++, null);
        s = ", ";
      }
      write(")");
    }
  }

  /**
   * @return {@code true} if a {@link Select} of all properties should happen via {@link SelectFrom#getAlias() alias},
   *         {@code false} otherwise (to simply use {@code *}). The default is {@code false}. Override to change. E.g.
   *         in JPQL you would write "SELECT a FROM Entity a ..." whereas in plain SQL you would write "SELECT * FROM
   *         Entity ..."
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
  protected void onSelectAll(SelectFrom<?, ?> selectFrom) {

    if (isSelectAllByAlias()) {
      write(" ");
      write(selectFrom.getAlias());
    } else {
      write(" *");
    }
  }

  @Override
  public void onFrom(FromClause<?, ?, ?> from) {

    write(" FROM ");
    onEntities(from);
    DbClauseVisitor.super.onFrom(from);
  }

  /**
   * @param entities the {@link AbstractEntitiesClause} to format.
   */
  protected void onEntities(AbstractEntitiesClause<?, ?, ?> entities) {

    onEntity(entities);
    for (EntitySubClause<?, ?> entity : entities.getAdditionalEntities()) {
      onAdditionalEntity(entity);
    }
  }

  /**
   * @param entity the {@link AbstractEntityClause} to format.
   */
  protected void onEntity(AbstractEntityClause<?, ?, ?> entity) {

    writeEntity(entity, true);
  }

  /**
   * @param entity the {@link AbstractEntityClause} for which to write the {@link AbstractEntityClause#getEntityName()
   *        entity name} or table name and optional {@link #onAlias(String, DbClause) alias}.
   * @param forceAlias - {@code true} to force an {@link #onAlias(String, DbClause) alias}, {@code false} otherwise.
   */
  protected void writeEntity(AbstractEntityClause<?, ?, ?> entity, boolean forceAlias) {

    writeEntityName(entity);
    if (forceAlias || entity.hasAlias()) {
      onAlias(entity.getAlias(), entity);
    }
  }

  /**
   * @param entityClause the {@link AbstractEntityClause} for which to write the
   *        {@link AbstractEntityClause#getEntityName() entity name} or table name.
   */
  protected void writeEntityName(AbstractEntityClause<?, ?, ?> entityClause) {

    write(entityClause.getEntityName());
  }

  /**
   * @param entity the {@link EntitySubClause} to format.
   */
  protected void onAdditionalEntity(EntitySubClause<?, ?> entity) {

    write(", ");
    onEntity(entity);
  }

  /**
   * @param alias the {@link EntitySubClause#getAlias() alias}.
   * @param clause the owning {@link AbstractEntityClause}.
   */
  protected void onAlias(String alias, DbClause clause) {

    if (alias != null) {
      write(" ");
      if (isUseAsBeforeAlias()) {
        write("AS ");
      }
      write(alias);
    }
  }

  @Override
  public void onWhere(WhereClause<?, ?> where) {

    write(" WHERE ");
    onPredicateClause(where);
    DbClauseVisitor.super.onWhere(where);
  }

  private void onPredicateClause(PredicateClause<?, ?> clause) {

    String s = "";
    for (CriteriaPredicate predicate : clause.getPredicates()) {
      write(s);
      this.criteriaFormatter.onExpression(predicate, PARENT_AND);
      s = " AND ";
    }
  }

  @Override
  public void onOrderBy(OrderBy<?> orderBy) {

    write(" ORDER BY ");
    String s = "";
    for (CriteriaOrdering ordering : orderBy.getOrderings()) {
      write(s);
      this.criteriaFormatter.onOrdering(ordering);
      s = ", ";
    }
    DbClauseVisitor.super.onOrderBy(orderBy);
  }

  @Override
  public void onInto(IntoClause<?, ?, ?> into) {

    write("INTO ");
    write(into.getEntityName());
    DbClauseVisitor.super.onInto(into);
  }

  @Override
  public void onValues(ValuesClause<?, ?> values) {

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
      i = onAssignment(assignment.getProperty(), assignment.getValue(), i, false, args);
    }
    write(")");
    if (args != null) { // isInto
      write(" VALUES (");
      s = "";
      ;
      for (int argIndex = 0; argIndex < i; argIndex++) {
        write(s);
        this.criteriaFormatter.onArg(args.get(argIndex), argIndex, null);
        s = ", ";
      }
      write(")");
    }
    DbClauseVisitor.super.onValues(values);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private CriteriaObject<?> mapValue(CriteriaObject<?> value, TypeMapper typeMapper) {

    if (value instanceof Literal<?> literal) {
      Object mapped = typeMapper.toTarget(literal.get());
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

    String name = typeMapper.mapName(property.getName());
    return new SimplePath(property.parentPath(), name);
  }

  @SuppressWarnings("rawtypes")
  private int onAssignment(PropertyPath<?> property, CriteriaObject<?> value, int index, boolean assign,
      List<CriteriaObject<?>> args) {

    if (property instanceof ReadableProperty<?> p) {
      TypeMapper typeMapper = p.getTypeMapper();
      if (typeMapper != null) {
        do {
          String name = typeMapper.mapName(property.getName());
          index = onAssignment(new SimplePath(property.parentPath(), name), mapValue(value, typeMapper), index, assign,
              args);
          typeMapper = typeMapper.next();
        } while (typeMapper != null);
        return index;
      }
    }
    if (index > 0) {
      write(", ");
    }
    if (assign) {
      this.criteriaFormatter.onPropertyPath(property, index, null);
      write("=");
      assert (args == null);
    }
    if (args == null) {
      this.criteriaFormatter.onArg(value, index, null);
    } else {
      this.criteriaFormatter.onPropertyPath(property, index, null);
      args.add(value);
    }
    return index + 1;
  }

  @Override
  public void onSet(SetClause<?, ?> set) {

    write(" SET ");
    int i = 0;
    for (PropertyAssignment<?> assignment : set.getAssignments()) {
      i = onAssignment(assignment.getProperty(), assignment.getValue(), i, true, null);
    }
    DbClauseVisitor.super.onSet(set);
  }

  /**
   * @param columns the {@link Iterable} with the {@link DbColumnSpec columns} to write.
   * @param entity the {@link EntityBean} containing the columns.
   * @param withDeclaration - {@code true} to write column declarations, {@code false} otherwise (to omit declarations).
   * @param skipDecompose - {@code true} to skip decomposing columns via ORM (e.g. for PK-Constraint), {@code false}
   *        otherwise.
   */
  protected void writeColumns(Iterable<DbColumnSpec> columns, EntityBean entity, boolean withDeclaration,
      boolean skipDecompose) {

    String s = "";
    if (skipDecompose || (this.dialect == null)) {
      for (DbColumnSpec column : columns) {
        write(s);
        if (withDeclaration) {
          writeIndent();
        }
        writeName(column);
        if (withDeclaration) {
          writeDeclaration(column, column.getDeclaration());
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
        writeName(columnName);
        if (withDeclaration) {
          String declaration = dbValue.getDeclaration();
          write(" ");
          write(declaration);
        }
        s = ",";
      }
    }
  }

  private void writeDeclaration(DbColumnSpec column, String declaration) {

    if (declaration == null) {
      Class<?> valueClass = column.getProperty().getValueClass();
      declaration = ClassNameMapper.get().getNameOrQualified(valueClass);
    }
    write(" ");
    write(declaration);
  }

  /**
   * @param constraint the {@link DbConstraint} to write.
   */
  private void writeConstraint(TableOperationType type, DbConstraint constraint) {

    writeConstraintKeywordWithName(type, constraint);
    write(" ");
    writeConstraintType(constraint);
    write(" (");
    if (constraint instanceof CheckConstraint ck) {
      CriteriaPredicate predicate = ck.getPredicate();
      writeCriteriaExpression(predicate);
    } else {
      EntityBean entity = (EntityBean) constraint.getFirstColumn().getProperty().getMetadata().getLock();
      boolean isPk = constraint instanceof PrimaryKeyConstraint;
      writeColumns(constraint, entity, false, isPk);
    }
    write(")");
    if (constraint instanceof ForeignKeyConstraint fk) {
      writeConstraintFkReference(fk.getReferenceColumn());
    }
  }

  /**
   * @param predicate the {@link CriteriaPredicate} to write (for a check constraint).
   */
  protected void writeCriteriaExpression(CriteriaExpression<?> predicate) {

    CriteriaOperator operator = predicate.getOperator();
    if (PredicateOperator.isNullBased(operator)) {
      assert (predicate.getArgCount() == 1);
      writeCriteriaObject(predicate.getFirstArg());
      write(" ");
      writeOperator(operator);
    } else if (operator.isInfix()) {
      // TODO add parenthesis as needed according to priorities...
      boolean first = true;
      for (CriteriaObject<?> arg : predicate.getArgs()) {
        if (first) {
          first = false;
        } else {
          if (operator.isConjunction()) {
            write(" ");
            writeOperator(operator);
            write(" ");
          } else {
            writeOperator(operator);
          }
        }
        writeCriteriaObject(arg);
      }
    } else if (operator.isUnary()) {
      assert (predicate.getArgCount() == 1);
      writeOperator(operator);
      write("(");
      writeCriteriaObject(predicate.getFirstArg());
      write(")");
    }
  }

  /**
   * @param operator the {@link PredicateOperator} to write.
   */
  protected void writeOperator(CriteriaOperator operator) {

    write(operator.getSyntax());
  }

  /**
   * @param object the {@link CriteriaObject} to write.
   */
  protected void writeCriteriaObject(CriteriaObject<?> object) {

    if (object instanceof ReadableProperty<?> property) {
      writeColumn(property);
    } else if (object instanceof CriteriaExpression<?> expression) {
      writeCriteriaExpression(expression);
    } else if (object instanceof Literal<?> literal) {
      writeLiteral(literal);
    } else if (object instanceof PropertyPath<?> path) {
      writePath(path);
    }
  }

  /**
   * @param path the {@link PropertyPath} to write.
   */
  private void writePath(PropertyPath<?> path) {

    write(path.path());
  }

  /**
   * @param literal the {@link Literal} to write.
   */
  private void writeLiteral(Literal<?> literal) {

    write(literal.toString());
  }

  /**
   * @param property the {@link ReadableProperty} to write as column.
   */
  protected void writeColumn(ReadableProperty<?> property) {

    // TODO not entirely correct, may be multiple columns...
    writeName(new DbColumnSpec(property));
  }

  /**
   * @param referenceColumn the referenced {@link DbColumnSpec column} (the primary key to use as foreign key).
   */
  protected void writeConstraintFkReference(DbColumnSpec referenceColumn) {

    write(" REFERENCES ");
    writeName(referenceColumn.getTable());
    write("(");
    writeName(referenceColumn);
    write(")");

  }

  /**
   * Writes "CONSTRAINT «constraint_name»". May be overridden as needed for specific dialect (e.g. for MySQL flaws).
   *
   * @param type the {@link TableOperationType}.
   * @param constraint the {@link DbConstraint}.
   */
  protected void writeConstraintKeywordWithName(TableOperationType type, DbConstraint constraint) {

    write("CONSTRAINT ");
    writeName(constraint);
  }

  private void writeConstraintType(DbConstraint constraint) {

    write(constraint.getType());
  }

  @Override
  public void onCreateIndex(CreateIndex createIndex) {

    writeIndent();
    write("CREATE ");
    if (createIndex.isUnique()) {
      write("UNIQUE ");
    }
    write("INDEX ");
    writeName(createIndex.getName());
    DbClauseVisitor.super.onCreateIndex(createIndex);
  }

  @Override
  public void onCreateIndexOn(CreateIndexOn<?> on) {

    write(" ON ");
    writeEntity(on, false);
    DbClauseVisitor.super.onCreateIndexOn(on);
  }

  @Override
  public void onCreateIndexColumns(CreateIndexColumns<?> columns) {

    write(" (");
    writeColumns(columns.getColumns(), columns.get().getOn().getEntity(), false, false);
    write(")");
    DbClauseVisitor.super.onCreateIndexColumns(columns);
  }

  @Override
  public void onAlterTableOperations(AlterTableOperations<?> operations) {

    String s = "\n";
    for (TableOperation operation : operations.getOperations()) {
      write(s);
      onAlterTableOperation(operation);
      s = ",\n";
    }
  }

  @Override
  public void onAlterTableOperation(TableOperation operation) {

    TableOperationType type = operation.getType();
    writeAlterTableOperationType(type);
    write(" ");
    if (operation instanceof TableColumnOperation columnOp) {
      writeAlterTableColumn(type);
      DbColumnSpec column = columnOp.getColumn();
      writeName(column);
      if ((type == TableOperationType.ADD) || (type == TableOperationType.MODIFY)) {
        writeDeclaration(column, column.getDeclaration());
      }
    } else if (operation instanceof TableConstraintOperation constraintOp) {
      DbConstraint constraint = constraintOp.getConstraint();
      if (type == TableOperationType.ADD) {
        writeConstraint(type, constraint);
      } else {
        String name;
        if (constraint == null) {
          name = constraintOp.getName();
        } else if (constraint.hasName() || (this.dialect == null)) {
          name = constraint.getName();
        } else {
          name = constraint.createName(this.dialect.getNamingStrategy());
        }
        writeName(name);
        if (operation instanceof TableRenameConstraintOperation rename) {
          write(" TO ");
          writeName(rename.getNewName());
        } else {
          assert (type == TableOperationType.DROP);
        }
      }
    }
  }

  /**
   * @param type the {@link TableOperationType} to write. May be overridden as needed for specific dialect (e.g. for MS
   *        SQL-Server flaws).
   */
  protected void writeAlterTableOperationType(TableOperationType type) {

    write(type.name());
  }

  /**
   * Write "COLUMN" keyword as needed for ALTER TABLE statement of the given {@code type}.
   *
   * @param type the {@link TableOperationType}.
   */
  protected void writeAlterTableColumn(TableOperationType type) {

    if ((type != TableOperationType.ADD) && (type != TableOperationType.MODIFY)) {
      write("COLUMN ");
    }
  }

  @Override
  public String toString() {

    return out().toString();
  }

  private static class CriteriaSqlFormatterInline extends CriteriaFormatter {

    CriteriaSqlFormatterInline() {

      super();
    }

  }

}
