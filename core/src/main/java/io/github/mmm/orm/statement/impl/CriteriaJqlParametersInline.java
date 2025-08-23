/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

import io.github.mmm.base.io.AppendableWriter;
import io.github.mmm.entity.id.Id;
import io.github.mmm.orm.param.CriteriaParametersFactory;
import io.github.mmm.property.criteria.CriteriaExpression;
import io.github.mmm.property.criteria.CriteriaParameter;
import io.github.mmm.property.criteria.CriteriaParameters;
import io.github.mmm.property.criteria.Literal;

/**
 * Implementation of {@link CriteriaParameters} for JQL.
 */
public class CriteriaJqlParametersInline implements CriteriaParameters<CriteriaParameter<?>> {

  private static final CriteriaJqlParametersInline INSTANCE = new CriteriaJqlParametersInline();

  /** {@link CriteriaParametersFactory} for this implementation. */
  public static final CriteriaParametersFactory FACTORY = _ -> INSTANCE;

  @Override
  public void onLiteral(Literal<?> literal, AppendableWriter out, CriteriaExpression<?> parent) {

    Object value = literal;
    if (literal != null) {
      Object literalValue = literal.get();
      if (literalValue instanceof Id) {
        value = literalValue;
      }
    }
    out.write(Objects.toString(value));
  }

  @Override
  public Iterator<CriteriaParameter<?>> iterator() {

    return Collections.emptyIterator();
  }

  /**
   * @return the singleton instance of this {@link CriteriaJqlParametersInline}.
   */
  public static CriteriaJqlParametersInline get() {

    return INSTANCE;
  }
}