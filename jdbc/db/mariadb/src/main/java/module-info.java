/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides MariaDB database support based on {@code mmm-orm-jdbc}.
 *
 * @provides io.github.mmm.orm.dialect.DbDialect
 */
module io.github.mmm.orm.jdbc.mariadb {

  requires transitive io.github.mmm.orm.jdbc;

  provides io.github.mmm.orm.dialect.DbDialect //
      with io.github.mmm.orm.jdbc.mariadb.MariaDbDialect;

}
