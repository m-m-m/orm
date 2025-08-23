/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.sqlite.dialect;

import io.github.mmm.orm.dialect.DbContext;
import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.orm.param.CriteriaParametersFactory;
import io.github.mmm.orm.statement.AbstractEntityClause;
import io.github.mmm.orm.statement.SetClause;
import io.github.mmm.orm.statement.create.CreateSequenceClause;
import io.github.mmm.orm.statement.select.SelectSequenceNextValueClause;
import io.github.mmm.orm.statement.update.UpdateClause;

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
   * @param parametersFactory the {@link CriteriaParametersFactory}.
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public SqliteFormatter(SqliteDialect dialect, CriteriaParametersFactory parametersFactory, String indentation) {

    super(dialect, parametersFactory, indentation);
  }

  @Override
  public boolean isUseAsBeforeAlias() {

    return true;
  }

  @Override
  protected void formatEntity(AbstractEntityClause<?, ?, ?> entity) {

    if (entity instanceof UpdateClause<?>) {
      formatEntity(entity, false);
    } else {
      super.formatEntity(entity);
    }
  }

  @Override
  public void formatSetClause(SetClause<?, ?> set, DbContext context) {

    // TODO Auto-generated method stub
    super.formatSetClause(set, context);
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
