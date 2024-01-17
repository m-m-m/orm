/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.criteria.SequenceNextValue;
import io.github.mmm.orm.metadata.DbQualifiedName;

/**
 * {@link SelectClause} to get the next value of a database sequence.
 *
 * @since 1.0.0
 */
public class SelectSequenceNextValueClause extends SelectClause<Long> {

  private final SequenceNextValue selection;

  /**
   * The constructor.
   *
   * @param sequenceName the {@link #getSequenceName() sequence name}.
   */
  public SelectSequenceNextValueClause(DbQualifiedName sequenceName) {

    super(null);
    this.selection = new SequenceNextValue(sequenceName);
    setResultName(VALUE_RESULT_SEQ_NEXT_VAL);
    add(this.selection);
    setStatement(new SelectStatement<>(this, null));
  }

  /**
   * @return the single selection to select.
   */
  public SequenceNextValue getSelection() {

    return this.selection;
  }

  /**
   * @return the {@link DbQualifiedName} of the sequence.
   */
  public DbQualifiedName getSequenceName() {

    return this.selection.getSequenceName();
  }

  @Override
  public <E extends EntityBean> SelectFrom<Long, E> from(E entity) {

    assert (entity == null);
    return super.from(entity);
  }

  @Override
  public boolean isSelectEntity() {

    return false;
  }

  @Override
  public boolean isSelectResult() {

    return false;
  }

  @Override
  public boolean isSelectSingle() {

    return true;
  }
}
