/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import static io.github.mmm.base.filter.CharFilter.NEWLINE_OR_SPACE;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.github.mmm.base.filter.CharFilter;
import io.github.mmm.base.sort.SortOrder;
import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.mapping.ClassNameMapper;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.GenericId;
import io.github.mmm.entity.id.IdFactory;
import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.ddl.constraint.CheckConstraint;
import io.github.mmm.orm.ddl.constraint.DbConstraint;
import io.github.mmm.orm.ddl.constraint.ForeignKeyConstraint;
import io.github.mmm.orm.ddl.constraint.NotNullConstraint;
import io.github.mmm.orm.ddl.constraint.PrimaryKeyConstraint;
import io.github.mmm.orm.ddl.constraint.UniqueConstraint;
import io.github.mmm.orm.ddl.operation.TableOperationKind;
import io.github.mmm.orm.ddl.operation.TableOperationType;
import io.github.mmm.orm.statement.alter.AlterTable;
import io.github.mmm.orm.statement.alter.AlterTableOperations;
import io.github.mmm.orm.statement.alter.AlterTableStatement;
import io.github.mmm.orm.statement.create.CreateIndex;
import io.github.mmm.orm.statement.create.CreateIndexColumns;
import io.github.mmm.orm.statement.create.CreateIndexOn;
import io.github.mmm.orm.statement.create.CreateIndexStatement;
import io.github.mmm.orm.statement.create.CreateTable;
import io.github.mmm.orm.statement.create.CreateTableContents;
import io.github.mmm.orm.statement.create.CreateTableStatement;
import io.github.mmm.orm.statement.create.CreateUniqueIndex;
import io.github.mmm.orm.statement.delete.Delete;
import io.github.mmm.orm.statement.delete.DeleteFrom;
import io.github.mmm.orm.statement.delete.DeleteStatement;
import io.github.mmm.orm.statement.insert.Insert;
import io.github.mmm.orm.statement.insert.InsertInto;
import io.github.mmm.orm.statement.insert.InsertStatement;
import io.github.mmm.orm.statement.insert.InsertValues;
import io.github.mmm.orm.statement.merge.Merge;
import io.github.mmm.orm.statement.merge.MergeInto;
import io.github.mmm.orm.statement.merge.MergeStatement;
import io.github.mmm.orm.statement.select.GroupBy;
import io.github.mmm.orm.statement.select.Having;
import io.github.mmm.orm.statement.select.OrderBy;
import io.github.mmm.orm.statement.select.Select;
import io.github.mmm.orm.statement.select.SelectEntity;
import io.github.mmm.orm.statement.select.SelectFrom;
import io.github.mmm.orm.statement.select.SelectProjection;
import io.github.mmm.orm.statement.select.SelectResult;
import io.github.mmm.orm.statement.select.SelectSingle;
import io.github.mmm.orm.statement.select.SelectStatement;
import io.github.mmm.orm.statement.update.Update;
import io.github.mmm.orm.statement.update.UpdateStatement;
import io.github.mmm.orm.statement.upsert.Upsert;
import io.github.mmm.orm.statement.upsert.UpsertInto;
import io.github.mmm.orm.statement.upsert.UpsertStatement;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.criteria.CriteriaExpression;
import io.github.mmm.property.criteria.CriteriaObjectParser;
import io.github.mmm.property.criteria.CriteriaOperator;
import io.github.mmm.property.criteria.CriteriaOrdering;
import io.github.mmm.property.criteria.CriteriaPredicate;
import io.github.mmm.property.criteria.Literal;
import io.github.mmm.property.criteria.ProjectionProperty;
import io.github.mmm.property.criteria.PropertyAssignment;
import io.github.mmm.property.criteria.PropertyPathParser;
import io.github.mmm.property.criteria.SimplePathParser;
import io.github.mmm.scanner.CharScannerParser;
import io.github.mmm.scanner.CharStreamScanner;
import io.github.mmm.value.CriteriaObject;
import io.github.mmm.value.PropertyPath;
import io.github.mmm.value.SimplePath;

/**
 * {@link CharScannerParser} for {@link DbStatement}s.<br>
 * <b>ATTENTION:</b> This is NOT a generic SQL parser. It will only support the exact syntax produced by
 * {@link DbStatementFormatter} with the defaults (as used by {@link DbStatement#toString()}).
 *
 * @since 1.0.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DbStatementParser implements CharScannerParser<DbStatement<?>> {

  private static final DbStatementParser INSTANCE = new DbStatementParser();

  private final ClassNameMapper classNameMapper;

  private final BeanFactory beanFactory;

  private final CriteriaObjectParser criteriaSelectionParser;

  /**
   * The constructor.
   */
  protected DbStatementParser() {

    super();
    this.classNameMapper = ClassNameMapper.get();
    this.beanFactory = BeanFactory.get();
    this.criteriaSelectionParser = CriteriaObjectParser.get();
  }

  @Override
  public DbStatement<?> parse(CharStreamScanner scanner) {

    try {
      DbStatement<?> statement;
      scanner.skipWhile(NEWLINE_OR_SPACE);
      String name = readKeyword(scanner);
      if (Select.NAME_SELECT.equals(name)) {
        statement = parseSelectStatement(scanner);
      } else if (matchesKeyword(Update.NAME_UPDATE, name, scanner)) {
        statement = parseUpdateStatement(scanner);
      } else if (matchesKeyword(Insert.NAME_INSERT, name, scanner)) {
        statement = parseInsertStatement(scanner);
      } else if (matchesKeyword(Delete.NAME_DELETE, name, scanner)) {
        statement = parseDeleteStatement(scanner);
      } else if ("CREATE".equals(name)) {
        name = readKeyword(scanner);
        boolean unique = false;
        if (name.equals("UNIQUE")) {
          unique = true;
          name = readKeyword(scanner);
        }
        if (name.equals("TABLE") && !unique) {
          statement = parseCreateTableStatement(scanner);
        } else if (name.equals("INDEX")) {
          statement = parseCreateIndexStatement(scanner, unique);
        } else {
          throw new IllegalStateException();
        }
      } else if (matchesKeyword(Upsert.NAME_UPSERT, name, scanner)) {
        statement = parseUsertStatement(scanner);
      } else if (matchesKeyword(Merge.NAME_MERGE, name, scanner)) {
        statement = parseMergeStatement(scanner);
      } else if (matchesKeyword(AlterTable.NAME_ALTER_TABLE, name, scanner)) {
        statement = parseAlterTableStatement(scanner);
      } else {
        throw new IllegalStateException("Unknown statement: " + name);
      }
      scanner.skipWhile(NEWLINE_OR_SPACE);
      if (scanner.hasNext()) {
        throw new IllegalStateException("Internal error: Statement not parsed to the end.");
      }
      return statement;
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to parse DB statement after \n" + scanner.getBufferParsed()
          + "\n and before \n" + scanner.getBufferToParse() + "\nwith error: " + e.getMessage(), e);
    }
  }

  private boolean matchesKeyword(String keyword, String tokenUpper, CharStreamScanner scanner) {

    if (keyword.equals(tokenUpper)) {
      return true;
    }
    int space = keyword.indexOf(' ');
    if (space > 0) {
      String first = keyword.substring(0, space);
      if (tokenUpper.equals(first)) {
        String second = keyword.substring(space + 1);
        scanner.require(second, true);
        scanner.requireOneOrMore(NEWLINE_OR_SPACE);
        return true;
      }
    }
    return false;
  }

  private CreateIndexStatement<?> parseCreateIndexStatement(CharStreamScanner scanner, boolean unique) {

    CreateIndex createIndex = parseCreateIndex(scanner, unique);
    CreateIndexOn<?> createIndexOn = parseCreateIndexOn(scanner, createIndex);
    CreateIndexColumns<?> createIndexColumns = parseCreateIndexColumns(scanner, createIndexOn);
    return createIndexColumns.get();
  }

  private CreateIndex parseCreateIndex(CharStreamScanner scanner, boolean unique) {

    String name = parseSegment(scanner);
    scanner.requireOneOrMore(NEWLINE_OR_SPACE);
    if (unique) {
      return new CreateUniqueIndex(name);
    } else {
      return new CreateIndex(name);
    }
  }

  private CreateIndexOn<?> parseCreateIndexOn(CharStreamScanner scanner, CreateIndex createIndex) {

    CreateIndexOn<?> createIndexOn = new CreateIndexOn<>(createIndex, null);
    scanner.require(CreateIndexOn.NAME_ON, true);
    scanner.requireOneOrMore(NEWLINE_OR_SPACE);
    parseEntityClause(scanner, createIndexOn, true);
    return createIndexOn;
  }

  private CreateIndexColumns<?> parseCreateIndexColumns(CharStreamScanner scanner, CreateIndexOn<?> createIndexOn) {

    DbColumnSpec[] columns = parseColumns(scanner, createIndexOn.getEntity(), 1);
    return createIndexOn.columns(columns);
  }

  private AlterTableStatement<?> parseAlterTableStatement(CharStreamScanner scanner) {

    AlterTable<?> alterTable = parseAlterTable(scanner);
    AlterTableOperations<?> operation = parseAlterTableOperations(scanner, alterTable);
    return operation.get();
  }

  private AlterTable<?> parseAlterTable(CharStreamScanner scanner) {

    AlterTable<?> alterTable = new AlterTable<>(null);
    parseEntityClause(scanner, alterTable, false);
    return alterTable;
  }

  private AlterTableOperations<?> parseAlterTableOperations(CharStreamScanner scanner, AlterTable<?> alterTable) {

    AlterTableOperations<?> operations = alterTable.addColumns(DbColumnSpec.NO_COLUMNS);
    EntityBean entity = alterTable.getEntity();
    do {
      scanner.skipWhile(NEWLINE_OR_SPACE);
      TableOperationType type = parseAlterTableOperationType(scanner);
      scanner.skipWhile(NEWLINE_OR_SPACE);
      TableOperationKind kind = TableOperationKind.COLUMN;
      if (scanner.expect("CONSTRAINT", true)) {
        kind = TableOperationKind.CONSTRAINT;
        scanner.skipWhile(NEWLINE_OR_SPACE);
      } else if (scanner.expect("COLUMN", true)) {
        scanner.skipWhile(NEWLINE_OR_SPACE);
      }
      String name = parseSegment(scanner);
      scanner.skipWhile(NEWLINE_OR_SPACE);
      switch (type) {
        case ADD -> {
          if (kind == TableOperationKind.COLUMN) {
            DbColumnSpec column = parseColumn(name, entity, true, scanner);
            operations.addColumn(column);
          } else {
            DbConstraint constraint = parseConstraint(name, entity, scanner);
            operations.addConstraint(constraint);
          }
        }
        case DROP -> {
          if (kind == TableOperationKind.COLUMN) {
            DbColumnSpec column = parseColumn(name, entity, false, scanner);
            operations.dropColumn(column);
          } else {
            operations.dropConstraint(name);
          }
        }
        case MODIFY -> {
          if (kind == TableOperationKind.COLUMN) {

          } else {
            throw new IllegalStateException("ALTER TABLE MODIFY is only allowed on COLUMN");
          }
        }
        case RENAME -> {
          if (kind == TableOperationKind.COLUMN) {
            DbColumnSpec column = parseColumn(name, entity, false, scanner);
            scanner.require("TO", true);
            scanner.requireOneOrMore(NEWLINE_OR_SPACE);
            DbColumnSpec newColumn = parseColumn(null, entity, false, scanner);
            operations.renameColumn(column, newColumn);
          } else {
            scanner.require("TO", true);
            scanner.requireOneOrMore(NEWLINE_OR_SPACE);
            String newName = scanner.readWhile(CharFilter.IDENTIFIER, 1, 128);
            operations.renameConstraint(name, newName);
          }
        }
      }
    } while (scanner.expectOne(','));
    return operations;
  }

  private DbConstraint parseConstraint(String constraintName, ReadableBean entity, CharStreamScanner scanner) {

    if (constraintName == null) {
      constraintName = parseSegment(scanner);
      scanner.skipWhile(NEWLINE_OR_SPACE);
    }
    DbConstraint constraint = null;
    if (scanner.expect(ForeignKeyConstraint.TYPE, true)) {
      DbColumnSpec column = parseColumn(scanner, entity);
      scanner.require("REFERENCES", true);
      scanner.requireOneOrMore(NEWLINE_OR_SPACE);
      DbColumnSpec referenceColumn = parseColumn(scanner);
      constraint = new ForeignKeyConstraint(constraintName, column, referenceColumn);
    } else if (scanner.expect(PrimaryKeyConstraint.TYPE, true)) {
      DbColumnSpec column = parseColumn(scanner, entity);
      constraint = new PrimaryKeyConstraint(constraintName, column);
    } else if (scanner.expect(NotNullConstraint.TYPE, true)) {
      DbColumnSpec column = parseColumn(scanner, entity);
      constraint = new NotNullConstraint(constraintName, column);
    } else if (scanner.expect(UniqueConstraint.TYPE, true)) {
      DbColumnSpec[] columns = parseColumns(scanner, entity, 1);
      constraint = new UniqueConstraint(constraintName, columns);
    } else if (scanner.expect(CheckConstraint.TYPE, true)) {
      PropertyPathParser pathParser = new EntityPathParser(entity);
      CriteriaPredicate predicate = this.criteriaSelectionParser.parsePredicate(scanner, pathParser);
      constraint = new CheckConstraint(constraintName, predicate);
    }
    return constraint;
  }

  private DbColumnSpec parseColumn(String name, ReadableBean bean, boolean withDeclaration, CharStreamScanner scanner) {

    if (name == null) {
      name = scanner.readWhile(CharFilter.IDENTIFIER, 1, 128);
      scanner.skipWhile(NEWLINE_OR_SPACE);
    }
    ReadableProperty<?> property = bean.getProperty(name);
    String declaration = null;
    if (withDeclaration) {
      declaration = scanner.readWhile(CharFilter.IDENTIFIER);
      scanner.skipWhile(NEWLINE_OR_SPACE);
      if (property != null) {
        Class<?> valueClass = property.getValueClass();
        String type = ClassNameMapper.get().getNameOrQualified(valueClass);
        if (declaration.equals(type)) {
          declaration = null;
        }
      }
    }
    return new DbColumnSpec(name, property, null, declaration);
  }

  private TableOperationType parseAlterTableOperationType(CharStreamScanner scanner) {

    String type = scanner.readWhile(CharFilter.LATIN_LETTER).toUpperCase(Locale.ROOT);
    return TableOperationType.valueOf(type);
  }

  private CreateTableStatement<?> parseCreateTableStatement(CharStreamScanner scanner) {

    CreateTable<?> createTable = parseCreateTable(scanner);
    CreateTableContents<?> contents = parseCreateTableContents(scanner, createTable);
    return contents.get();
  }

  private CreateTableContents<?> parseCreateTableContents(CharStreamScanner scanner, CreateTable<?> createTable) {

    CreateTableContents<?> contents = createTable.columns(DbColumnSpec.NO_COLUMNS);
    EntityBean entity = createTable.getEntity();
    scanner.skipWhile(NEWLINE_OR_SPACE);
    scanner.requireOne('(');
    do {
      scanner.skipWhile(NEWLINE_OR_SPACE);
      if (scanner.expect("CONSTRAINT", true)) {
        scanner.requireOneOrMore(NEWLINE_OR_SPACE);
        DbConstraint constraint = parseConstraint(null, entity, scanner);
        contents.constraint(constraint);
      } else {
        ReadableProperty<?> columnProperty = EntityPathParser.parsePath(scanner, entity);
        // if (columnProperty != EntityPathParser.PROPERTY_REVISION) {
        contents.column(columnProperty);
        scanner.requireOne(NEWLINE_OR_SPACE);
        String columnType = parseSegment(scanner);
        Class<?> columnClass = this.classNameMapper.getClass(columnType);
        assert (columnClass == columnProperty.getValueClass());
        // }
      }
      scanner.skipWhile(NEWLINE_OR_SPACE);
    } while (scanner.expectOne(','));
    scanner.requireOne(')');
    return contents;
  }

  private DbColumnSpec parseColumn(CharStreamScanner scanner) {

    String entityName = PropertyPathParser.readSegment(scanner, null);
    scanner.skipWhile(NEWLINE_OR_SPACE);
    scanner.requireOne('(');
    scanner.skipWhile(NEWLINE_OR_SPACE);
    String propertyName = PropertyPathParser.readSegment(scanner, null);
    scanner.skipWhile(NEWLINE_OR_SPACE);
    scanner.requireOne(')');
    // rebuild original property
    Class entityClass = ClassNameMapper.get().getClass(entityName);
    WritableBean entity = BeanFactory.get().create(entityClass);
    // propertyName could be qualified with an alias...
    ReadableProperty<?> property = entity.getProperty(propertyName);
    return new DbColumnSpec(property);
  }

  private DbColumnSpec parseColumn(CharStreamScanner scanner, ReadableBean entity) {

    scanner.skipWhile(NEWLINE_OR_SPACE);
    scanner.requireOne('(');
    scanner.skipWhile(NEWLINE_OR_SPACE);
    ReadableProperty<?> column = EntityPathParser.parsePath(scanner, entity);
    scanner.skipWhile(NEWLINE_OR_SPACE);
    scanner.requireOne(')');
    scanner.skipWhile(NEWLINE_OR_SPACE);
    return new DbColumnSpec(column);
  }

  private DbColumnSpec[] parseColumns(CharStreamScanner scanner, ReadableBean entity, int min) {

    List<DbColumnSpec> columns = new ArrayList<>();
    scanner.skipWhile(NEWLINE_OR_SPACE);
    scanner.requireOne('(');
    do {
      scanner.skipWhile(NEWLINE_OR_SPACE);
      ReadableProperty<?> column = EntityPathParser.parsePath(scanner, entity);
      columns.add(new DbColumnSpec(column));
      scanner.skipWhile(NEWLINE_OR_SPACE);
    } while (scanner.expectOne(','));
    scanner.requireOne(')');
    if (columns.size() < min) {
      throw new IllegalArgumentException("Requires " + min + " column(s) but found only " + columns.size() + ".");
    }
    return columns.toArray(new DbColumnSpec[columns.size()]);
  }

  private MergeStatement<?> parseMergeStatement(CharStreamScanner scanner) {

    MergeStatement statement = new MergeInto<>(new Merge(), null).values(PropertyAssignment.EMPTY_ARRAY).get();
    // TODO Auto-generated method stub
    return statement;
  }

  private UpsertStatement<?> parseUsertStatement(CharStreamScanner scanner) {

    UpsertStatement upsertStatement = new UpsertInto<>(new Upsert(), null).values(PropertyAssignment.EMPTY_ARRAY).get();
    // TODO Auto-generated method stub
    return upsertStatement;
  }

  private DeleteStatement<?> parseDeleteStatement(CharStreamScanner scanner) {

    DeleteFrom<EntityBean> from = new DeleteFrom<>(new Delete(), null);
    parseFrom(scanner, from);
    DeleteStatement<EntityBean> statement = from.get();
    parseWhere(scanner, statement.getWhere());
    // TODO
    return statement;
  }

  private InsertStatement<?> parseInsertStatement(CharStreamScanner scanner) {

    InsertInto<?> into = new InsertInto<>(new Insert(), null);
    parseInto(scanner, into);
    scanner.skipWhile(' ');
    InsertValues<?> values = into.values(PropertyAssignment.EMPTY_ARRAY);
    parseValues(scanner, values, into.getEntity());
    InsertStatement statement = values.get();
    // TODO Auto-generated method stub
    return statement;
  }

  private UpdateStatement<?> parseUpdateStatement(CharStreamScanner scanner) {

    Update<?> update = parseUpdate(scanner);
    UpdateStatement<?> statement = update.get();
    AliasMap aliasMap = getAliasMap(update);
    parseSetClause(scanner, statement.getSet(), aliasMap);
    parseWhere(scanner, statement.getWhere());
    return statement;
  }

  private void parseSetClause(CharStreamScanner scanner, SetClause set, PropertyPathParser pathParser) {

    do {
      scanner.skipWhile(NEWLINE_OR_SPACE);
      PropertyPath<?> property = pathParser.parse(scanner);
      scanner.skipWhile(NEWLINE_OR_SPACE);
      scanner.requireOne('=');
      scanner.skipWhile(NEWLINE_OR_SPACE);
      CriteriaObject value = this.criteriaSelectionParser.parseSelection(scanner, pathParser);
      set.set(PropertyAssignment.of(property, value));
      scanner.skipWhile(NEWLINE_OR_SPACE);
    } while (scanner.expectOne(','));
  }

  private Update<?> parseUpdate(CharStreamScanner scanner) {

    Update<?> update = new Update<>(null);
    parseEntitiesClause(scanner, update);
    scanner.require("SET", true);
    scanner.requireOneOrMore(NEWLINE_OR_SPACE);
    return update;
  }

  private SelectStatement<?> parseSelectStatement(CharStreamScanner scanner) {

    Select<?> select = parseSelect(scanner);
    SelectFrom from = new SelectFrom<>(select, null);
    parseFrom(scanner, from);
    if (select instanceof SelectEntity) {
      String aliasFrom = from.getAlias();
      String aliasSelect = select.getResultName();
      if (!Objects.equals(aliasFrom, aliasSelect)) {
        throw new IllegalArgumentException(
            "Alias of SELECT (" + aliasSelect + ") and FROM (" + aliasFrom + ") do not match.");
      }
    }
    SelectStatement statement = from.get();
    parseWhere(scanner, statement.getWhere());
    parseGroupBy(scanner, statement.getGroupBy());
    parseHaving(scanner, statement.getHaving());
    parseOrderBy(scanner, statement.getOrderBy());
    // post process selections
    AliasMap aliasMap = ((AbstractDbStatement) statement).getAliasMap();
    List<CriteriaObject<?>> selections = (List<CriteriaObject<?>>) select.getSelections();
    int size = selections.size();
    for (int i = 0; i < size; i++) {
      CriteriaObject<?> selection = selections.get(i);
      CriteriaObject<?> resolved = resolve(selection, aliasMap);
      if (resolved != selection) {
        selections.set(i, resolved);
      }
    }
    return statement;

  }

  private void parseValues(CharStreamScanner scanner, ValuesClause values, EntityBean entity) {

    // TODO consider mql change: INSERT INTO MyEntity e VALUES (e.Property1=value1, e.Property2=value2)
    scanner.requireOne('(');
    List<ReadableProperty<?>> properties = new ArrayList<>();
    boolean todo = true;
    while (todo) {
      scanner.skipWhile(NEWLINE_OR_SPACE);
      ReadableProperty<?> path = EntityPathParser.parsePath(scanner, entity);
      properties.add(path);
      scanner.skipWhile(NEWLINE_OR_SPACE);
      if (scanner.expectOne(')')) {
        todo = false;
      } else if (!scanner.expectOne(',')) {
        throw new IllegalArgumentException("Expecting ')' or ','.");
      }
    }
    scanner.skipWhile(' ');
    // TODO: make optional (e.g. support sub-query instead)
    scanner.require("VALUES", true);
    scanner.skipWhile(' ');
    scanner.requireOne('(');
    int i = 0;
    todo = true;
    int columnCount = properties.size();
    while (todo) {
      if (i >= columnCount) {
        break;
      }
      ReadableProperty<?> property = properties.get(i);
      scanner.skipWhile(' ');
      Literal literal = this.criteriaSelectionParser.parseLiteral(scanner);
      if (property != EntityPathParser.PROPERTY_REVISION) {
        if (property == entity.Id()) {
          Object pk = literal.get();
          i++;
          Object rev = null;
          if (i < columnCount) {
            ReadableProperty<?> ref = properties.get(i);
            if (ref == EntityPathParser.PROPERTY_REVISION) {
              scanner.requireOne(',');
              scanner.skipWhile(' ');
              literal = this.criteriaSelectionParser.parseLiteral(scanner);
              rev = literal.get();
            }
          }
          if (rev == null) {
            i--; // edge-case
          }
          GenericId<?, Object, Comparable<?>> id = IdFactory.get().create(entity.getJavaClass(), pk,
              (Comparable<?>) rev);
          literal = Literal.of(id);
        }
        values.value(PropertyAssignment.of(property, literal));
      }
      i++;
      scanner.skipWhile(' ');
      if (scanner.expectOne(')')) {
        todo = false;
      } else if (!scanner.expectOne(',')) {
        throw new IllegalArgumentException("Expecting ')' or ','.");
      }
    }
    if (i != columnCount) {
      throw new IllegalArgumentException("Found " + columnCount + " coumn(s) but " + i + " value(s).");
    }
  }

  private void parseInto(CharStreamScanner scanner, IntoClause into) {

    scanner.skipWhile(NEWLINE_OR_SPACE);
    scanner.require("INTO", true);
    scanner.requireOneOrMore(NEWLINE_OR_SPACE);
    parseEntityClause(scanner, into, false);
  }

  private CreateTable<?> parseCreateTable(CharStreamScanner scanner) {

    CreateTable<?> createTable = new CreateTable<>(null);
    parseEntityClause(scanner, createTable, false);
    return createTable;
  }

  private CriteriaObject<?> resolve(CriteriaObject<?> selection, AliasMap aliasMap) {

    if (selection instanceof SimplePath) {
      return aliasMap.resolvePath((SimplePath) selection);
    } else if (selection instanceof CriteriaExpression) {
      CriteriaExpression<?> expression = (CriteriaExpression<?>) selection;
      CriteriaOperator operator = expression.getOperator();
      List<? extends CriteriaObject<?>> args = expression.getArgs();
      List<CriteriaObject<?>> newArgs = null;
      int i = 0;
      for (CriteriaObject<?> arg : args) {
        CriteriaObject<?> resolved = resolve(arg, aliasMap);
        if (newArgs == null) {
          if (resolved != arg) {
            newArgs = new ArrayList<>(args.size());
            for (int j = 0; j <= i; j++) {
              newArgs.add(args.get(j));
            }
          }
        } else {
          newArgs.add(resolved);
        }
        i++;
      }
      if (newArgs != null) {
        return operator.expression(newArgs);
      }
    }
    // nothing to do (e.g. literal)...
    return selection;
  }

  private void parseOrderBy(CharStreamScanner scanner, OrderBy orderBy) {

    if (!scanner.expect("ORDER BY ", true)) {
      return;
    }
    AliasMap aliasMap = ((AbstractDbStatement) orderBy.get()).getAliasMap();
    do {
      scanner.skipWhile(' ');
      PropertyPath<?> property = aliasMap.parse(scanner);
      SortOrder order = SortOrder.ASC;
      int spaces = scanner.skipWhile(' ');
      if (spaces > 0) {
        if (scanner.expect("ASC", true)) {
          order = SortOrder.ASC;
        } else if (scanner.expect("DESC", true)) {
          order = SortOrder.DESC;
        }
        scanner.skipWhile(' ');
      }
      CriteriaOrdering ordering = new CriteriaOrdering(property, order);
      orderBy.and(ordering);
    } while (scanner.expectOne(','));

  }

  private Select parseSelect(CharStreamScanner scanner) {

    scanner.skipWhile(NEWLINE_OR_SPACE);
    Select select;
    if (scanner.expect("new", true)) {
      scanner.requireOneOrMore(NEWLINE_OR_SPACE);
      WritableBean projectionBean = null;
      String bean = scanner.readUntil('(', false);
      if (bean == null) {
        throw new IllegalArgumentException("Missing '(' after 'SELECT +'.");
      } else if (bean.length() > 0) {
        Class beanClass = this.classNameMapper.getClass(bean);
        projectionBean = this.beanFactory.create(beanClass);
        select = new SelectProjection(projectionBean);
      } else {
        select = new SelectResult();
      }
      char c;
      do {
        scanner.skipWhile(NEWLINE_OR_SPACE);
        CriteriaObject<?> selection = this.criteriaSelectionParser.parseSelection(scanner);
        if (projectionBean != null) {
          PropertyPath path = null;
          int spaces = scanner.skipWhile(NEWLINE_OR_SPACE);
          if ((spaces > 0)) {
            if (scanner.expect("AS", true)) {
              scanner.requireOneOrMore(NEWLINE_OR_SPACE);
            }
            path = EntityPathParser.parsePath(scanner, projectionBean);
          } else if (selection instanceof PropertyPath) {
            path = EntityPathParser.resolvePath(projectionBean, (PropertyPath) selection);
          } else {
            // error?
          }
          selection = new ProjectionProperty(selection, path);
        }
        select.getSelections().add(selection);
        c = scanner.next();
      } while (c == ',');
      if (c != ')') {
        throw new IllegalArgumentException("Missing ')'.");
      }
    } else {
      // single selection or entityAlias
      PropertyPath path = SimplePathParser.INSTANCE.parse(scanner);
      if (path.parentPath() == null) {
        select = new SelectEntity(path.getName());
      } else {
        select = new SelectSingle(path);
      }
    }
    return select;
  }

  private void parseFrom(CharStreamScanner scanner, FromClause from) {

    scanner.skipWhile(NEWLINE_OR_SPACE);
    scanner.require("FROM", true);
    scanner.requireOneOrMore(NEWLINE_OR_SPACE);
    parseEntitiesClause(scanner, from);
  }

  private void parseEntityClause(CharStreamScanner scanner, AbstractEntityClause entityClause, boolean allowAlias) {

    String entityName = parseSegment(scanner);
    Class entityClass = this.classNameMapper.getClass(entityName);
    EntityBean entity = (EntityBean) this.beanFactory.create(entityClass);
    scanner.skipWhile(NEWLINE_OR_SPACE);
    String entityAlias = "";
    if (allowAlias) {
      boolean hasAsKeyword = false;
      if (scanner.expect("AS", true)) {
        hasAsKeyword = true;
        scanner.requireOneOrMore(NEWLINE_OR_SPACE);
      }
      entityAlias = parseSegment(scanner);
      if (entityAlias.isEmpty()) {
        if (hasAsKeyword) {
          throw new IllegalStateException("Missing alias after AS keyword!");
        }
      } else {
        scanner.skipWhile(NEWLINE_OR_SPACE);
      }
    }
    if (entityClause.getEntity() == null) {
      entityClause.setEntity(entity);
      if (!entityAlias.isEmpty()) {
        entityClause.as(entityAlias);
      }
    } else if (entityClause instanceof AbstractEntitiesClause entitesClause) {
      entitesClause.and(entity, entityAlias);
    }
  }

  private void parseEntitiesClause(CharStreamScanner scanner, AbstractEntitiesClause entitesClause) {

    do {
      scanner.skipWhile(NEWLINE_OR_SPACE);
      parseEntityClause(scanner, entitesClause, true);
    } while (scanner.expectOne(','));
  }

  private void parseWhere(CharStreamScanner scanner, WhereClause where) {

    if (!scanner.expect("WHERE", true)) {
      return;
    }
    do {
      scanner.requireOneOrMore(NEWLINE_OR_SPACE);
      CriteriaObject<?> expression = this.criteriaSelectionParser.parse(scanner);
      if (expression instanceof CriteriaPredicate) {
        where.and((CriteriaPredicate) expression);
      } else {
        throw new IllegalArgumentException("Expected predicate but found " + expression);
      }
      scanner.skipWhile(NEWLINE_OR_SPACE);
    } while (scanner.expect("AND", true));
  }

  private void parseGroupBy(CharStreamScanner scanner, GroupBy groupBy) {

    if (!scanner.expect(" GROUP BY", true)) {
      return;
    }
    scanner.requireOneOrMore(NEWLINE_OR_SPACE);
    PropertyPathParser pathParser = getAliasMap(groupBy);
    do {
      scanner.skipWhile(NEWLINE_OR_SPACE);
      PropertyPath<?> path = pathParser.parse(scanner);
      groupBy.and(path);
      scanner.skipWhile(NEWLINE_OR_SPACE);
    } while (scanner.expectOne(','));
  }

  private String parseSegment(CharStreamScanner scanner) {

    return scanner.readWhile(CharFilter.SEGMENT, 1, 128);
  }

  private String readKeyword(CharStreamScanner scanner) {

    String keywordUpperCase = scanner.readWhile(CharFilter.LATIN_LETTER, 1, 128).toUpperCase(Locale.ROOT);
    scanner.requireOneOrMore(NEWLINE_OR_SPACE);
    return keywordUpperCase;
  }

  private void parseHaving(CharStreamScanner scanner, Having having) {

    // TODO Auto-generated method stub

  }

  private static AliasMap getAliasMap(MainDbClause clause) {

    return ((AbstractDbStatement) clause.get()).getAliasMap();
  }

  /**
   * @return the singleton instance of this {@link DbStatementParser}.
   */
  public static DbStatementParser get() {

    return INSTANCE;
  }

}
