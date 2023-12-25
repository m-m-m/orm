/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.mmm.marshall.MarshallingConfig;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredWriter;

/**
 * Abstract base implementation of an SQL {@link DbStatement} that may be executed to the database.
 *
 * @param <E> type of the {@link AbstractEntityClause#getEntity() entity}.
 * @since 1.0.0
 */
public abstract class AbstractDbStatement<E> implements DbStatement<E> {

  private List<AbstractDbClause> clauses;

  @Override
  public List<? extends DbClause> getClauses() {

    if (this.clauses == null) {
      List<AbstractDbClause> list = new ArrayList<>();
      addClauses(list);
      this.clauses = Collections.unmodifiableList(list);
    }
    return this.clauses;
  }

  /**
   * @param list the {@link List} where to {@link List#add(Object) add} the {@link DbClause}s.
   * @see #getClauses()
   */
  protected abstract void addClauses(List<AbstractDbClause> list);

  @Override
  public void write(StructuredWriter writer) {

    String indendation = writer.getFormat().getConfig().get(MarshallingConfig.VAR_INDENTATION);
    DbStatementFormatter formatter = new DbStatementFormatter(indendation);
    writer.writeValueAsString(formatter.onStatement(this).toString());
  }

  @Override
  public DbStatement<?> read(StructuredReader reader) {

    return DbStatementMarshalling.read(reader);
  }

  /**
   * @return the {@link AliasMap} of this statement.
   */
  protected abstract AliasMap getAliasMap();

  @Override
  public String toString() {

    return new DbStatementFormatter().onStatement(this).toString();
  }

}
