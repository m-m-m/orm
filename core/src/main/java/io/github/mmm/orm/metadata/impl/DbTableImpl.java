/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import io.github.mmm.orm.metadata.DbColumn;
import io.github.mmm.orm.metadata.DbForeignKey;
import io.github.mmm.orm.metadata.DbIndex;
import io.github.mmm.orm.metadata.DbObjectContainer;
import io.github.mmm.orm.metadata.DbPrimaryKey;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.metadata.DbTable;
import io.github.mmm.orm.metadata.DbTableData;
import io.github.mmm.orm.metadata.DbTableType;

/**
 * Implementation of {@link DbTable}.
 *
 * @since 1.0.0
 */
public class DbTableImpl extends DbObjectWithCommentImpl implements DbTable {

  /** @see #getType() */
  protected final DbTableType type;

  private final DbObjectContainer<DbColumn> columns;

  private DbPrimaryKey primaryKey;

  private boolean primaryKeyLoaded;

  private DbObjectContainer<DbForeignKey> foreignKeys;

  private DbObjectContainer<DbIndex> indexes;

  /**
   * The constructor.
   *
   * @param data the {@link DbTableDataImpl}.
   * @param columns the {@link #getColumns() columns}.
   * @param primaryKey the {@link #getPrimaryKey() primary key}.
   * @param foreignKeys the {@link #getForeignKeys() foreign keys}.
   * @param indexes the {@link #getIndexes() indexes}.
   */
  public DbTableImpl(DbTableData data, DbObjectContainer<DbColumn> columns, DbPrimaryKey primaryKey,
      DbObjectContainer<DbForeignKey> foreignKeys, DbObjectContainer<DbIndex> indexes) {

    this(data.getQualifiedName(), data.getComment(), data.getType(), columns, primaryKey, foreignKeys, indexes);
  }

  /**
   * The constructor.
   *
   * @param qName the {@link #getQualifiedName() qualified name}.
   * @param comment the {@link #getComment() comment}.
   * @param type the {@link #getType() table type}.
   * @param columns the {@link #getColumns() columns}.
   * @param primaryKey the {@link #getPrimaryKey() primary key}.
   * @param foreignKeys the {@link #getForeignKeys() foreign keys}.
   * @param indexes the {@link #getIndexes() indexes}.
   */
  public DbTableImpl(DbQualifiedName qName, String comment, DbTableType type, DbObjectContainer<DbColumn> columns,
      DbPrimaryKey primaryKey, DbObjectContainer<DbForeignKey> foreignKeys, DbObjectContainer<DbIndex> indexes) {

    super(qName, comment);
    this.type = type;
    this.columns = columns;
    this.primaryKey = primaryKey;
    this.foreignKeys = foreignKeys;
    this.indexes = indexes;
  }

  @Override
  public DbTableType getType() {

    return this.type;
  }

  @Override
  public DbObjectContainer<DbColumn> getColumns() {

    return this.columns;
  }

  @Override
  public DbObjectContainer<DbForeignKey> getForeignKeys() {

    if (this.foreignKeys == null) {
      if (this.type.isSynonym() || this.type.isView()) {
        this.foreignKeys = DbObjectContainerImplEmpty.get();
      } else {
        this.foreignKeys = loadForeignKeys();
      }
    }
    return this.foreignKeys;
  }

  /**
   * @return the value of {@link #getForeignKeys()}.
   */
  protected DbObjectContainer<DbForeignKey> loadForeignKeys() {

    throw lazyLoadingNotImplemented();
  }

  @Override
  public DbPrimaryKey getPrimaryKey() {

    if (!this.primaryKeyLoaded) {
      assert (this.primaryKey == null);
      // synonyms and views do not have a primary key so we do not want to waste resources by asking the DB
      if (!this.type.isSynonym() && !this.type.isView()) {
        this.primaryKey = loadPrimaryKey();
      }
      this.primaryKeyLoaded = true;
    }
    return this.primaryKey;
  }

  /**
   * @return the value of {@link #getPrimaryKey()}.
   */
  protected DbPrimaryKey loadPrimaryKey() {

    throw lazyLoadingNotImplemented();
  }

  @Override
  public DbObjectContainer<DbIndex> getIndexes() {

    if (this.indexes == null) {
      this.indexes = loadIndexes();
    }
    return this.indexes;
  }

  /**
   * @return the value of {@link #getForeignKeys()}.
   */
  protected DbObjectContainer<DbIndex> loadIndexes() {

    throw lazyLoadingNotImplemented();
  }

  private RuntimeException lazyLoadingNotImplemented() {

    throw new IllegalStateException(
        "Child object was given as null in constructor and lazy loading is not implemented!");
  }

  @Override
  public void toString(StringBuilder sb, boolean detailed) {

    sb.append(this.type);
    sb.append(':');
    sb.append(this.qualifiedName);
    if (detailed && !this.comment.isEmpty()) {
      sb.append('(');
      sb.append(this.comment);
      sb.append(')');
    }
  }

}
