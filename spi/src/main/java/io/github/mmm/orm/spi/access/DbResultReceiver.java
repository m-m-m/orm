/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.access;

import java.util.function.Consumer;

import io.github.mmm.orm.mapping.DbMapper;
import io.github.mmm.orm.mapping.DbMapper2Java;
import io.github.mmm.orm.result.DbResult;

/**
 * {@link Consumer} of {@link DbResult} building the mapped Java result object.
 *
 * @param <R> type of the Java result object.
 * @since 1.0.0
 */
public abstract class DbResultReceiver<R> implements Consumer<DbResult> {

  private final DbMapper2Java<R> mapper;

  /**
   * The constructor.
   *
   * @param mapper the {@link DbMapper}.
   */
  public DbResultReceiver(DbMapper2Java<R> mapper) {

    super();
    this.mapper = mapper;
  }

  @Override
  public void accept(DbResult dbResult) {

    R result = this.mapper.db2java(dbResult);
    receive(result);
  }

  /**
   * @param result the converted Java result.
   */
  protected abstract void receive(R result);

}
