/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides H2 database support based on {@code mmm-orm-jdbc}.
 *
 * @provides io.github.mmm.orm.dialect.DbDialect
 */
module io.github.mmm.orm.db.h2.dialect {

  requires transitive io.github.mmm.orm.spi;

  provides io.github.mmm.orm.dialect.DbDialect //
      with io.github.mmm.orm.db.h2.dialect.H2Dialect;

}
