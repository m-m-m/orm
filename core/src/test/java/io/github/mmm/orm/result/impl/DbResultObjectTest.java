package io.github.mmm.orm.result.impl;

import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;

/**
 * Test of {@link DbResultObject}.
 */
class DbResultObjectTest extends AbstractDbResultTest {

  @Override
  protected DbResult create(DbResultValue<?>... cells) {

    return new DbResultObject(cells);
  }

}
