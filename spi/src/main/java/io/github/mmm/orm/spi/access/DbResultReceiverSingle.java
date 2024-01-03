/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.access;

import java.util.function.Consumer;

import io.github.mmm.orm.mapping.DbMapper;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.statement.NonUniqueResultException;

/**
 * {@link Consumer} of {@link DbResult} building the mapped Java result object.
 *
 * @param <R> type of the Java result object.
 * @since 1.0.0
 */
public class DbResultReceiverSingle<R> extends DbResultReceiver<R> {

  private R result;

  /**
   * The constructor.
   *
   * @param mapper the {@link DbMapper}.
   */
  public DbResultReceiverSingle(DbMapper<R> mapper) {

    super(mapper);
  }

  /**
   * @return the single result object.
   */
  public R getResult() {

    return this.result;
  }

  @Override
  protected void receive(R resultObject) {

    if (this.result == null) {
      this.result = resultObject;
    } else {
      throw new NonUniqueResultException(2); // wrong but will never happen here
    }
  }

}
