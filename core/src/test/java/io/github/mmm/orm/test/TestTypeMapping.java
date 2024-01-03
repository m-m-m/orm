package io.github.mmm.orm.test;

import io.github.mmm.orm.type.DbTypeBoolean;
import io.github.mmm.orm.type.DbTypeDouble;
import io.github.mmm.orm.type.DbTypeInteger;
import io.github.mmm.orm.type.DbTypeLocalDate2Date;
import io.github.mmm.orm.type.DbTypeLong;
import io.github.mmm.orm.typemapping.DbTypeMapping;

/**
 * {@link DbTypeMapping} implementation for testing.
 */
public class TestTypeMapping extends DbTypeMapping {

  /**
   * The constructor.
   */
  public TestTypeMapping() {

    super();
    add(new DbTypeLong("BIGINT"));
    add(new DbTypeInteger("INTEGER"));
    add(new DbTypeDouble("DOUBLE PRECISION"));
    add(new DbTypeBoolean("BOOLEAN"));
    add(new DbTypeLocalDate2Date("DATE"));
    addString("VARCHAR", "VARCHAR(%s)", "CHAR(%s)");
  }

}