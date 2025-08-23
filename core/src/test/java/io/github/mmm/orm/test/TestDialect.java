/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.test;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.naming.DbNamingStrategy;

/**
 * {@link AbstractDbDialect} implementation for testing.
 */
public class TestDialect extends AbstractDbDialect<TestDialect> {

  /**
   * The constructor.
   */
  public TestDialect() {

    super(new TestTypeMapping());
  }

  private TestDialect(Orm orm) {

    super(orm);
  }

  @Override
  public String getId() {

    return "test";
  }

  @Override
  protected TestDialect withOrm(Orm newOrm) {

    return new TestDialect(newOrm);
  }

  @Override
  protected DbNamingStrategy getDefaultNamingStrategy() {

    return DbNamingStrategy.of();
  }

}
