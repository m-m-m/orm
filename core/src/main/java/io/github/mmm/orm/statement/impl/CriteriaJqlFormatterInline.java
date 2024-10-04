/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.impl;

import io.github.mmm.property.criteria.CriteriaFormatter;

/**
 * Implementation of {@link CriteriaFormatter} for JQL.
 */
public class CriteriaJqlFormatterInline extends CriteriaFormatter {

  /**
   * The constructor.
   */
  public CriteriaJqlFormatterInline() {

    super(CriteriaJqlParametersInline.get());
  }

}