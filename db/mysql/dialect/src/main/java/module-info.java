/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides ORM support for MySQL database (dialect).
 *
 * @provides io.github.mmm.orm.dialect.DbDialect
 */
module io.github.mmm.orm.db.mysql.dialect {

  requires transitive io.github.mmm.orm.spi;

  provides io.github.mmm.orm.dialect.DbDialect //
      with io.github.mmm.orm.db.mysql.dialect.MySqlDialect;

}
