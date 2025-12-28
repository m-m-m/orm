package io.github.mmm.orm.result.impl;

import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;

/**
 * Test of {@link DbResultPojo}.
 */
class DbResultPojoTest extends AbstractDbResultTest {

  @Override
  protected DbResult create(DbResultValue<?>... cells) {

    DbResultPojo result = new DbResultPojo();
    for (DbResultValue<?> cell : cells) {
      result.add(cell);
    }
    return result;
  }

}
