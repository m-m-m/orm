/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.mysql.dialect;

import io.github.mmm.orm.ddl.constraint.DbConstraint;
import io.github.mmm.orm.ddl.operation.TableOperationType;
import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.property.criteria.CriteriaFormatter;

/**
 * {@link DbDialectStatementFormatter} for H2 Database.
 *
 * @since 1.0.0
 */
public class MySqlFormatter extends DbDialectStatementFormatter {

  /**
   * The constructor.
   *
   * @param dialect the {@link MySqlDialect}.
   */
  public MySqlFormatter(MySqlDialect dialect) {

    super(dialect);
  }

  /**
   * The constructor.
   *
   * @param dialect the {@link MySqlDialect}.
   * @param criteriaFormatter the {@link CriteriaFormatter} used to format criteria fragments to database syntax (SQL).
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public MySqlFormatter(MySqlDialect dialect, CriteriaFormatter criteriaFormatter, String indentation) {

    super(dialect, criteriaFormatter, indentation);
  }

  @Override
  protected void formatConstraintKeywordWithName(TableOperationType type, DbConstraint constraint) {

    if (type == TableOperationType.DROP) {
      write("INDEX ");
    } else {
      super.formatConstraintKeywordWithName(type, constraint);
    }
  }

  @Override
  protected void formatAlterTableColumn(TableOperationType type) {

    if (type == TableOperationType.MODIFY) {
      write("COLUMN ");
    } else {
      super.formatAlterTableColumn(type);
    }
  }

}
