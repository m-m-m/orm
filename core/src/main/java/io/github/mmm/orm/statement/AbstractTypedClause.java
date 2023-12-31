/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

/**
 * Abstract base class implementing {@link TypedClause}.
 *
 * @param <E> type of this {@link DbClause} (typically the {@link AbstractEntityClause#getEntity() entity}).
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class AbstractTypedClause<E, SELF extends AbstractTypedClause<E, SELF>> extends AbstractDbClause
    implements TypedClause<E> {

  /**
   * The constructor.
   */
  public AbstractTypedClause() {

    super();
  }

  /**
   * @return this instance itself.
   */
  @SuppressWarnings("unchecked")
  protected SELF self() {

    return (SELF) this;
  }

}
