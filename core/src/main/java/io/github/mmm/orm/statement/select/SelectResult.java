package io.github.mmm.orm.statement.select;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.result.DbResult;

/**
 * {@link Select} to query arbitrary {@link #getSelections() selections} as generic {@link DbResult}.
 *
 * @see Select#result()
 * @since 1.0.0
 */
public class SelectResult extends Select<DbResult> {

  /**
   * The constructor.
   */
  public SelectResult() {

    super(null);
    setResultName(VALUE_RESULT_RESULT);
  }

  @Override
  protected <E extends EntityBean> SelectFrom<DbResult, E> from(E entity) {

    return super.from(entity);
  }

  @Override
  public boolean isSelectEntity() {

    return false;
  }

  @Override
  public boolean isSelectResult() {

    return true;
  }

  @Override
  public boolean isSelectSingle() {

    return false;
  }
}
