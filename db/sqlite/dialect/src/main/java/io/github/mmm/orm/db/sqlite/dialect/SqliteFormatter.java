/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.sqlite.dialect;

import io.github.mmm.orm.dialect.DbContext;
import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.orm.statement.create.CreateSequenceClause;
import io.github.mmm.orm.statement.select.SelectSequenceNextValueClause;
import io.github.mmm.property.criteria.CriteriaFormatterFactory;

/**
 * {@link DbDialectStatementFormatter} for SQLite Database.
 *
 * @since 1.0.0
 */
public class SqliteFormatter extends DbDialectStatementFormatter {

  /**
   * The constructor.
   *
   * @param dialect the {@link SqliteDialect}.
   */
  public SqliteFormatter(SqliteDialect dialect) {

    super(dialect);
  }

  /**
   * The constructor.
   *
   * @param dialect the {@link SqliteDialect}.
   * @param criteriaFormatterFactory the {@link CriteriaFormatterFactory}.
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public SqliteFormatter(SqliteDialect dialect, CriteriaFormatterFactory criteriaFormatterFactory, String indentation) {

    super(dialect, criteriaFormatterFactory, indentation);
  }

  @Override
  public boolean isUseAsBeforeAlias() {

    return true;
  }

  @Override
  protected void formatCreateSequenceClause(CreateSequenceClause seq, DbContext context) {

    writeIndent();
    write("INSERT INTO ");
    formatQualifiedName(seq.getSequenceName());
    write(" (ID, NEXT_VALUE, INCREMENT_VALUE) VALUES (1, ");
    write(seq.getStartWith().toString());
    write(", ");
    write(seq.getIncrementBy().toString());
    write(")");

    newStatement();

    writeIndent();
    write("CREATE TABLE ");
    formatQualifiedName(seq.getSequenceName());
    write("""
        (
          ID INTEGER PRIMARY KEY,
          NEXT_VALUE INTEGER NOT NULL,
          INCREMENT_VALUE INTEGER NOT NULL
        )""");
  }

  @Override
  protected void formatSelectSeqNextVal(SelectSequenceNextValueClause seq) {

    writeIndent();
    write("UPDATE ");
    formatQualifiedName(seq.getSequenceName());
    write(" SET NEXT_VALUE = NEXT_VALUE + INCREMENT_VALUE WHERE ID = 1");

    newStatement();

    writeIndent();
    write("SELECT NEXT_VALUE FROM ");
    formatQualifiedName(seq.getSequenceName());
    write(" WHERE ID = 1");
  }

}
