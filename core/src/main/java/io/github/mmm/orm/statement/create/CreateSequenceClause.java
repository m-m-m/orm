/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.MainDbClause;
import io.github.mmm.orm.statement.StartClause;

/**
 * A {@link CreateSequenceClause} of an SQL {@link CreateSequenceStatement}.
 *
 * @since 1.0.0
 */
public class CreateSequenceClause extends AbstractDbClause implements StartClause, MainDbClause<Void> {

  /** Name of {@link CreateSequenceClause} for marshaling. */
  public static final String NAME_CREATE_TABLE = "CREATE SEQUENCE";

  private final DbQualifiedName sequenceName;

  private final CreateSequenceStatement statement;

  private Long startWith;

  private Integer incrementBy;

  private Long minValue;

  private Long maxValue;

  private Boolean cycle;

  /**
   * The constructor.
   *
   * @param sequenceName the {@link #getSequenceName()}.
   */
  public CreateSequenceClause(String sequenceName) {

    this(DbName.of(sequenceName));
  }

  /**
   * The constructor.
   *
   * @param sequenceName the {@link #getSequenceName()}.
   */
  public CreateSequenceClause(DbName sequenceName) {

    this(new DbQualifiedName(null, null, sequenceName));
  }

  /**
   * The constructor.
   *
   * @param sequenceName the {@link #getSequenceName()}.
   */
  public CreateSequenceClause(DbQualifiedName sequenceName) {

    super();
    this.sequenceName = sequenceName;
    this.statement = new CreateSequenceStatement(this);
  }

  /**
   * @return the {@link DbQualifiedName} of the sequence to create.
   */
  public DbQualifiedName getSequenceName() {

    return this.sequenceName;
  }

  /**
   * @param start the {@link #getStartWith() start with} value.
   * @return the {@link CreateSequenceClause} for fluent API calls.
   */
  public CreateSequenceClause startWith(long start) {

    assert (this.startWith == null);
    this.startWith = Long.valueOf(start);
    return this;
  }

  /**
   * @return the {@link #startWith(long) start with} value of the sequence or {@code null} if undefined.
   */
  public Long getStartWith() {

    return this.startWith;
  }

  /**
   * @param increment the {@link #getIncrementBy() increment by} value.
   * @return the {@link CreateSequenceClause} for fluent API calls.
   */
  public CreateSequenceClause incrementBy(int increment) {

    assert (this.incrementBy == null);
    this.incrementBy = Integer.valueOf(increment);
    return this;
  }

  /**
   * @return the {@link #incrementBy(int) increment by} value of the sequence or {@code null} if undefined.
   */
  public Integer getIncrementBy() {

    return this.incrementBy;
  }

  /**
   * @param min the {@link #getMinValue() minimum value}.
   * @return the {@link CreateSequenceClause} for fluent API calls.
   */
  public CreateSequenceClause minValue(long min) {

    assert (this.minValue == null);
    this.minValue = Long.valueOf(min);
    return this;
  }

  /**
   * @return the {@link #minValue(long) minimum value} of the sequence or {@code null} if undefined (NOMINVALUE).
   */
  public Long getMinValue() {

    return this.minValue;
  }

  /**
   * @param max the {@link #getMaxValue() maximum value}.
   * @return the {@link CreateSequenceClause} for fluent API calls.
   */
  public CreateSequenceClause maxValue(long max) {

    assert (this.maxValue == null);
    this.maxValue = Long.valueOf(max);
    return this;
  }

  /**
   * @return the {@link #maxValue(long) maximum value} of the sequence or {@code null} if undefined (NOMAXVALUE).
   */
  public Long getMaxValue() {

    return this.maxValue;
  }

  /**
   * Sets {@link #getCycle() cycle} to {@link Boolean#TRUE}.
   *
   * @return the {@link CreateSequenceClause} for fluent API calls.
   */
  public CreateSequenceClause cycle() {

    assert (this.cycle == null);
    this.cycle = Boolean.TRUE;
    return this;
  }

  /**
   * Sets {@link #getCycle() cycle} to {@link Boolean#FALSE}.
   *
   * @return the {@link CreateSequenceClause} for fluent API calls.
   */
  public CreateSequenceClause nocycle() {

    assert (this.cycle == null);
    this.cycle = Boolean.FALSE;
    return this;
  }

  /**
   * @return {@link Boolean#TRUE} for {@link #cycle()}, {@link Boolean#FALSE} for {@link #nocycle()} and {@code null} if
   *         undefined.
   */
  public Boolean getCycle() {

    return this.cycle;
  }

  @Override
  public CreateSequenceStatement get() {

    return this.statement;
  }

}
