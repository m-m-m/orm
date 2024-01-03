/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping.impl;

import java.util.Iterator;

import io.github.mmm.base.lang.Builder;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;
import io.github.mmm.orm.result.impl.DbResultPojo;
import io.github.mmm.orm.result.impl.DbResultValueObject;
import io.github.mmm.value.converter.TypeMapper;

/**
 * A node of the mapping tree for mapping from Java to database and vice versa.
 *
 * @param <S> the {@link TypeMapper#getSourceType() source type}.
 * @param <T> the {@link TypeMapper#getTargetType() target type}.
 * @since 1.0.0
 */
public final class DbSegmentMapper<S, T> extends ComposableDbMapper<S> {

  /** The {@link TypeMapper} to wrap. */
  private final TypeMapper<S, T> typeMapper;

  private final DbResultValueObject<T> dbResultValue;

  private final DbSegmentMapper<T, ?> child;

  /** The {@link TypeMapper#next()} {@link DbSegmentMapper}. */
  private final DbSegmentMapper<S, ?> next;

  /**
   * The constructor.
   *
   * @param typeMapper the {@link #getTypeMapper() type mapper}.
   * @param dbResultValue the {@link #getDbResultValue() entry}.
   * @param next the {@link #getNext() next}.
   */
  public DbSegmentMapper(TypeMapper<S, T> typeMapper, DbResultValueObject<T> dbResultValue,
      DbSegmentMapper<S, ?> next) {

    this(typeMapper, dbResultValue, null, next);
    assert (typeMapper.getDeclaration() == dbResultValue.getDeclaration());
  }

  /**
   * The constructor.
   *
   * @param typeMapper the {@link #getTypeMapper() type mapper}.
   * @param child the {@link #getChild() child}.
   * @param next the {@link #getNext() next}.
   */
  public DbSegmentMapper(TypeMapper<S, T> typeMapper, DbSegmentMapper<T, ?> child, DbSegmentMapper<S, ?> next) {

    this(typeMapper, null, child, next);
  }

  /**
   * The constructor.
   *
   * @param typeMapper the {@link #getTypeMapper() type mapper}.
   * @param dbResultValue the {@link #getDbResultValue() entry}.
   * @param child the {@link #getChild() child}.
   * @param next the {@link #getNext() next}.
   */
  private DbSegmentMapper(TypeMapper<S, T> typeMapper, DbResultValueObject<T> dbResultValue,
      DbSegmentMapper<T, ?> child, DbSegmentMapper<S, ?> next) {

    super();
    this.typeMapper = typeMapper;
    this.dbResultValue = dbResultValue;
    this.child = child;
    if ((this.child == null) == (this.dbResultValue == null)) {
      throw new IllegalArgumentException(); // exactly on of child or entry must not be null
    }
    this.next = next;
  }

  /**
   * @return the {@link TypeMapper} to wrap. May not be {@code null}.
   */
  protected TypeMapper<S, T> getTypeMapper() {

    return this.typeMapper;
  }

  /**
   * @return entry the {@link DbResultValueObject} to use as template. Will be {@code null} if this is no leaf node.
   */
  protected DbResultValueObject<T> getDbResultValue() {

    return this.dbResultValue;
  }

  /**
   * @return child the {@link DbSegmentMapper} to chain as child. Will be {@code null} if leaf node.
   */
  protected DbSegmentMapper<T, ?> getChild() {

    return this.child;
  }

  /**
   * @return the {@link TypeMapper#next() siblings} as {@link DbSegmentMapper}s. Will be {@code null} if this instance
   *         itself is already a sibling of another instance.
   */
  protected DbSegmentMapper<S, ?> getNext() {

    return this.next;
  }

  @Override
  public DbResult java2db(S javaValue) {

    DbResultPojo dbResult = new DbResultPojo();
    java2db(javaValue, dbResult);
    return dbResult;
  }

  /**
   * @param javaValue the Java value to convert and send to the database.
   * @param dbResult the {@link DbResultPojo} where to {@link DbResultPojo#add(io.github.mmm.orm.result.DbResultValue)
   *        add} the collected {@link io.github.mmm.orm.result.DbResultValue database values}.
   */
  public void java2db(S javaValue, DbResultPojo dbResult) {

    T dbValue;
    if (javaValue == null) {
      dbValue = this.typeMapper.toTargetNull();
    } else {
      dbValue = this.typeMapper.toTarget(javaValue);
    }
    if (this.child != null) {
      this.child.java2db(dbValue, dbResult);
    } else {
      dbResult.add(this.dbResultValue.withValue(dbValue));
    }
    DbSegmentMapper<S, ?> current = this.next;
    while (current != null) {
      current.java2db(javaValue, dbResult);
      current = current.next;
    }
  }

  @Override
  public S db2java(Iterator<DbResultValue<?>> dbIterator) {

    return db2java(dbIterator, null);
  }

  /**
   * @param dbIterator the {@link Iterator} of the {@link DbResultValue database values} to convert to Java.
   * @param javaBuilder the optional {@link Builder} to build the converted Java value. May be {@code null}.
   * @return the Java value.
   */
  @SuppressWarnings("unchecked")
  public S db2java(Iterator<DbResultValue<?>> dbIterator, Builder<S> javaBuilder) {

    T dbValue;
    if (this.child != null) {
      dbValue = this.child.db2java(dbIterator, null);
    } else {
      DbResultValue<?> dbResultVal = dbIterator.next();
      assert (dbResultVal.getName().equals(this.dbResultValue.getName()));
      dbValue = (T) dbResultVal.getValue();
    }
    S source;
    if (javaBuilder == null) {
      if (this.next == null) {
        source = this.typeMapper.toSource(dbValue);
      } else {
        javaBuilder = this.typeMapper.sourceBuilder();
        this.typeMapper.with(javaBuilder, dbValue);
        DbSegmentMapper<S, ?> current = this.next;
        while (current != null) {
          current.db2java(dbIterator, javaBuilder);
          current = current.next;
        }
        source = javaBuilder.build();
      }
    } else {
      this.typeMapper.with(javaBuilder, dbValue);
      source = null;
    }
    return source;
  }

}
