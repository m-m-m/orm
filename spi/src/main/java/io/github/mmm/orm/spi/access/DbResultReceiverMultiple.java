/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.access;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.github.mmm.orm.mapping.DbMapper;
import io.github.mmm.orm.result.DbResult;

/**
 * {@link Consumer} of {@link DbResult} building the mapped Java result object.
 *
 * @param <R> type of the Java result object.
 * @since 1.0.0
 */
public class DbResultReceiverMultiple<R> extends DbResultReceiver<R> {

  private final List<R> results;

  /**
   * The constructor.
   *
   * @param mapper the {@link DbMapper}.
   */
  public DbResultReceiverMultiple(DbMapper<R> mapper) {

    super(mapper);
    this.results = new ArrayList<>();
  }

  /**
   * @return the single result object.
   */
  public List<R> getResults() {

    return this.results;
  }

  @Override
  protected void receive(R resultObject) {

    this.results.add(resultObject);
  }

}
