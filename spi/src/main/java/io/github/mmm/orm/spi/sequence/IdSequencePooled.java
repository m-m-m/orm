/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.sequence;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.sequence.IdSequence;

/**
 * Implementation of {@link IdSequence} using a pool of {@link Id}s in memory. This will reduce the number of statements
 * to the database for requesting a next value from a database sequence and hence can significantly improve
 * performance.<br>
 * To make this work, you have to create your database sequence with a according increment value:
 *
 * <pre>
 * CREATE SEQUENCE ENTITY_SEQUENCE
 * START WITH 1000000000000
 * INCREMENT BY 10
 * MINVALUE 1000000000000
 * MAXVALUE 9123456789123456789
 * NOCYCLE
 * </pre>
 *
 * And in your code, you must configure the increment value to the exact same value defined in the SQL for your sequence
 * (here {@code 10}) and obviously use the same sequence (here {@code ENTITY_SEQUENCE}). This way your Java application
 * will only request the next value from the database sequence for every 10 entities while the next 9 {@link Id}s can be
 * generated in memory. This implementation is thread-safe using {@link ReentrantLock}. If your Java application is shut
 * down or crashes before the "pool" of 10 {@link Id}s is fully utilized, you can end up in gaps so some numbers of your
 * {@link Id} space are wasted (at maximum {@code increment-1}). Further, this approach is also guaranteed to work if
 * multiple concurrent cluster nodes using the same approach to communicate with the same database. In case you have
 * clients or SQL statements not aware of this approach they will waste quite some {@link Id}s creating gaps. However,
 * still you are always guaranteed to get unique {@link Id}s.<br>
 * <br>
 * If you require fully sequential {@link Id}s and cannot tolerate gaps, you should not use this strategy. Otherwise it
 * will boost your performance significantly. You can also increase the {@code increment} to a higher value (e.g. 20,
 * 50, or 100). However, be aware that the higher the number the more likely you will cause gaps and thereby waste
 * {@link Id}s reducing your total {@link Id} space.<br>
 * <br>
 * We recommend to use a huge start value for your database sequence (1.000.000.000.000). This reserves an {@link Id}
 * range for master-data that is big enough for whatever may come in the future. You can ship SQL statements together
 * with your application release updates that adds new master-data records with fixed {@link Id}s or migrates existing
 * such records simplifying your life.<br>
 * By setting a very large positive MAXVALUE and using NOCYCLE, you can prevent the potential error that your sequence
 * overflows accidentally. While this is extremely unlikely to ever happen looking at the gigantic range of available
 * {@link Id}s but if that ever happens by accident (e.g. because some data-base script ran crazy), it is better to fail
 * fast and detect such error before causing even bigger harm. Please note that the above MAXVALUE is not the highest
 * positive long value ({@link Long#MAX_VALUE}) but a close value that is easy to remember and type without mistake.
 *
 * @since 1.0.0
 */
public class IdSequencePooled implements IdSequence {

  private final Lock lock;

  private final IdSequence sequence;

  private final long increment;

  private long current;

  private long limit;

  /**
   * The constructor.
   *
   * @param sequence the actual {@link IdSequence} that gives us the {@link IdSequence#next(Id) next value} from the
   *        database.
   * @param increment the "INCREMENT BY" value of the underlying database sequence that is used as pool size.
   */
  public IdSequencePooled(IdSequence sequence, long increment) {

    super();
    Objects.requireNonNull(sequence);
    if (increment < 3) {
      throw new IllegalArgumentException("Invalid increment '" + increment + "' - must be at least 3.");
    }
    this.lock = new ReentrantLock();
    this.sequence = sequence;
    this.increment = increment;
    this.current = 0;
    this.limit = 0;
  }

  @Override
  public long next(Id<?> template) {

    this.lock.lock();
    try {
      if (this.current == this.limit) {
        this.current = this.sequence.next(template);
        this.limit = this.current + this.increment;
      }
      assert (this.current < this.limit);
      return this.current++;
    } finally {
      this.lock.unlock();
    }
  }

  @Override
  public String toString() {

    return this.current + "[" + this.limit + "]";
  }

}
