/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.param;

import java.util.Iterator;

/**
 * Implementation of {@link Iterator} for {@link CriteriaParameterImpl}.
 */
public class CriteriaParameterIterator implements Iterator<CriteriaParameterImpl<?>> {

  private CriteriaParameterImpl<?> next;

  /**
   * The constructor.
   *
   * @param first the initial {@link CriteriaParameterImpl} to iterate.
   */
  public CriteriaParameterIterator(CriteriaParameterImpl<?> first) {

    super();
    this.next = first;
  }

  @Override
  public boolean hasNext() {

    return (this.next != null);
  }

  @Override
  public CriteriaParameterImpl<?> next() {

    CriteriaParameterImpl<?> result = this.next;
    this.next = this.next.getNext();
    return result;
  }

}
