/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides H2 database support based on {@code mmm-orm-jdbc}.
 *
 * @provides io.github.mmm.orm.dialect.DbDialect
 */
module io.github.mmm.orm.jdbc.h2 {

  requires transitive io.github.mmm.orm.jdbc;

  requires java.sql;

  exports io.github.mmm.orm.jdbc.h2;

  provides io.github.mmm.orm.dialect.DbDialect //
      with io.github.mmm.orm.jdbc.h2.H2Dialect;

}
