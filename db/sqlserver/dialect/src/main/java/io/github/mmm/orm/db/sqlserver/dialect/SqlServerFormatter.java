/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.sqlserver.dialect;

import io.github.mmm.orm.ddl.operation.TableOperationType;
import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.orm.param.CriteriaParametersFactory;

/**
 * {@link DbDialectStatementFormatter} for <a href="https://docs.microsoft.com/en-us/sql/">MS SQL Server</a>.
 *
 * @since 1.0.0
 */
public class SqlServerFormatter extends DbDialectStatementFormatter {

  /**
   * The constructor.
   *
   * @param dialect the {@link SqlServerDialect}.
   */
  public SqlServerFormatter(SqlServerDialect dialect) {

    super(dialect);
  }

  /**
   * The constructor.
   *
   * @param dialect the {@link SqlServerDialect}.
   * @param parametersFactory the {@link CriteriaParametersFactory}.
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public SqlServerFormatter(SqlServerDialect dialect, CriteriaParametersFactory parametersFactory, String indentation) {

    super(dialect, parametersFactory, indentation);
  }

  @Override
  protected void formatAlterTableOperationType(TableOperationType type) {

    if (type == TableOperationType.MODIFY) {
      write("ALTER");
    } else {
      super.formatAlterTableOperationType(type);
    }
  }

}
