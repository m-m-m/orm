/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.tx;

import java.util.concurrent.Callable;

import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.tx.impl.DbTransactionExceutorProviderAdapter;

/**
 * Interface to execute a {@link Runnable} or {@link Callable} within a transaction. Unlike JTA, Spring's
 * {@code PlatformTransactionManager}, or other overkill this is a simple API that prevents pitfalls:
 * <ul>
 * <li>No need to call {@code begin()} as you begin the transaction automatically when calling
 * {@link #doInTx(Runnable)}.</li>
 * <li>No need to call {@code commit()} as you commit the transaction automatically when your task ({@link Runnable} or
 * {@link Callable}) returned successfully.</li>
 * <li>Non eed to call {@code rollback()} or {@code setRollbackOnly()} as you will rollback the transaction
 * automatically when your task ({@link Runnable} or {@link Callable}) failed by throwing any {@link Throwable}.</li>
 * <li>No support for two-phase commit as reality has shown that distributed transactions are causing more problems than
 * they actually solve. KISS is key so better design concepts for compensation in case of errors if multiple systems are
 * involved.</li>
 * <li>The same applies for save-points that are also not supported by this API.</li>
 * <li>Also nested transactions (sub-transactions) are typically an indicator of over-complicated or even flawed design.
 * However, whenever needed and supported this interface could be used again within the transaction task to express a
 * nested transaction.</li>
 * <li></li>
 * </ul>
 */
public interface DbTransactionExecutor {

  /**
   * Most simple variant to execute a task in a transaction when no result is needed. Otherwise use
   * {@link #doInTx(Callable)} instead.
   *
   * @param task the {@link Runnable} to execute in a transaction.
   */
  default void doInTx(Runnable task) {

    Callable<Void> callable = new Callable<>() {

      @Override
      public Void call() {

        task.run();
        return null;
      }
    };
    doInTx(callable);
  }

  /**
   * Executes a {@link Callable} task in a transaction and returns the result of it.
   *
   * @param <R> type of the result.
   * @param task the {@link Callable} to execute in a transaction.
   * @return the result of {@link Callable#call()}.
   */
  <R> R doInTx(Callable<R> task);

  /**
   * @return the {@link DbTransaction} or {@code null} if no transaction is currently active for this executor.
   */
  DbTransaction getTransaction();

  /**
   * @return the {@link DbTransactionExecutor} for the {@link DbSource#get() default} {@link DbSource}.
   */
  static DbTransactionExecutor get() {

    return get(DbSource.get());
  }

  /**
   * @param source the {@link DbSource}.
   * @return the corresponding {@link DbTransactionExecutor}.
   */
  static DbTransactionExecutor get(DbSource source) {

    return DbTransactionExceutorProviderAdapter.INSTANCE.create(source);
  }

}
