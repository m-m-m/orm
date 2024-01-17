/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import java.util.function.Consumer;

import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.AbstractDbStatement;
import io.github.mmm.orm.statement.AliasMap;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.DbStatementType;
import io.github.mmm.orm.statement.StartClause;
import io.github.mmm.orm.statement.impl.DbStatementTypeImpl;

/**
 * {@link DbStatement} to {@link CreateSequenceClause create a sequence}.
 *
 * @since 1.0.0
 */
public class CreateSequenceStatement extends AbstractDbStatement<Void> {

  private final CreateSequenceClause createSequence;

  /**
   * The constructor.
   *
   * @param createSequence the {@link #getCreateSequence() create sequence}.
   */
  public CreateSequenceStatement(CreateSequenceClause createSequence) {

    super();
    this.createSequence = createSequence;
  }

  /**
   * @deprecated use {@link #getCreateSequence()} to make it more explicit.
   */
  @Deprecated
  @Override
  public StartClause getStart() {

    return this.createSequence;
  }

  /**
   * @return the opening {@link CreateSequenceClause}.
   */
  public CreateSequenceClause getCreateSequence() {

    return this.createSequence;
  }

  @Override
  protected void addClauses(Consumer<AbstractDbClause> consumer) {

    consumer.accept(this.createSequence);
  }

  @Override
  public DbStatementType getType() {

    return DbStatementTypeImpl.CREATE_SEQUENCE;
  }

  @Override
  protected AliasMap getAliasMap() {

    return null;
  }

}
