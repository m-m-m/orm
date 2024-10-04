/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import org.assertj.core.api.Assertions;

import io.github.mmm.marshall.MarshallingConfig;
import io.github.mmm.marshall.StandardFormat;
import io.github.mmm.marshall.StructuredTextFormat;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbDialectStatementFormatter;

/**
 * Abstract base class for tests of {@link DbStatement}s.
 */
public abstract class DbStatementTest extends Assertions {

  /**
   * Formats the given {@link DbStatement} to SQL and compares with the given SQL.
   *
   * @param statement the {@link DbStatement} to check.
   * @param jql the expected JQL corresponding to the {@link DbStatement}.
   * @param checkJson {@code true} to also check the JSON of the {@link DbStatement}, {@code false} otherwise.
   */
  protected void check(DbStatement<?> statement, String jql, boolean checkJson) {

    assertThat(statement).isNotNull().hasToString(jql);
    DbStatement<?> copy = DbStatementParser.get().parse(jql);
    assertThat(copy).isNotNull().hasToString(jql);

    if (checkJson) {
      StructuredTextFormat format = StandardFormat.json(MarshallingConfig.NO_INDENTATION);
      StringBuilder sb = new StringBuilder();
      statement.writeObject(format.writer(sb), statement);
      String actualJson = sb.toString();
      assertThat(actualJson).isEqualTo('"' + jql + '"');
      DbStatement<?> statement2 = DbStatementMarshalling.get().readObject(format.reader(actualJson));
      assertThat(statement2).isNotNull().hasToString(jql).isInstanceOf(statement.getClass());
    }
  }

  /**
   * Formats the given {@link DbStatement} to SQL and compares with the given SQL.
   *
   * @param statement the {@link DbStatement} to check.
   * @param jql the expected JQL corresponding to the {@link DbStatement}.
   * @param sql the expected SQL corresponding to the {@link DbStatement}.
   */
  protected void check(DbStatement<?> statement, String jql, String sql) {

    AbstractDbDialect<?> sqlDialect = new SqlDialect();
    DbDialectStatementFormatter formatter = sqlDialect.createFormatter();
    formatter.formatStatement(statement);
    assertThat(formatter.get()).isEqualTo(sql);
    check(statement, jql, false);
  }

}
