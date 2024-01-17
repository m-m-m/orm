/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import io.github.mmm.marshall.MarshallingConfig;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.orm.impl.DbContextNone;

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
      Consumer<AbstractDbClause> consumer = c -> {
        if (c != null) {
          list.add(c);
        }
      };
      addClauses(consumer);
      this.clauses = Collections.unmodifiableList(list);
    }
    return this.clauses;
  }

  /**
   * @param consumer the {@link List} where to {@link List#add(Object) add} the {@link DbClause}s.
   * @see #getClauses()
   */
  protected abstract void addClauses(Consumer<AbstractDbClause> consumer);

  @Override
  public void write(StructuredWriter writer) {

    String indendation = writer.getFormat().getConfig().get(MarshallingConfig.VAR_INDENTATION);
    AbstractDbStatementFormatter formatter = new AbstractDbStatementFormatter(indendation);
    formatter.formatStatement(this, DbContextNone.INSTANCE);
    writer.writeValueAsString(formatter.get());
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

    AbstractDbStatementFormatter formatter = new AbstractDbStatementFormatter();
    formatter.formatStatement(this);
    return formatter.toString();
  }

}
