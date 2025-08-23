/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.h2.dialect;

import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.orm.param.CriteriaParametersFactory;
import io.github.mmm.orm.statement.select.SelectSequenceNextValueClause;

/**
 * {@link DbDialectStatementFormatter} for H2 Database.
 *
 * @since 1.0.0
 */
public class H2Formatter extends DbDialectStatementFormatter {

  /**
   * The constructor.
   *
   * @param dialect the {@link H2Dialect}.
   */
  public H2Formatter(H2Dialect dialect) {

    super(dialect);
  }

  /**
   * The constructor.
   *
   * @param dialect the {@link H2Dialect}.
   * @param parametersFactory the {@link CriteriaParametersFactory}.
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public H2Formatter(H2Dialect dialect, CriteriaParametersFactory parametersFactory, String indentation) {

    super(dialect, parametersFactory, indentation);
  }

  @Override
  protected void formatSelectSeqNextVal(SelectSequenceNextValueClause seq) {

    write("SELECT NEXT VALUE FOR ");
    formatQualifiedName(seq.getSequenceName());
  }

}
