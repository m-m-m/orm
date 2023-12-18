/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides H2 database support based on {@code mmm-orm-jdbc}.
 *
 * @provides io.github.mmm.orm.dialect.DbDialect
 * @provides io.github.mmm.orm.connection.DbConnectionPoolProvider
 */
@SuppressWarnings("rawtypes") //
module io.github.mmm.orm.jdbc.oracle {

  requires transitive io.github.mmm.orm.jdbc;

  requires ucp;

  requires ojdbc10;

  provides io.github.mmm.orm.dialect.DbDialect //
      with io.github.mmm.orm.jdbc.oracle.OracleDialect;

  provides io.github.mmm.orm.connection.DbConnectionPoolProvider //
      with io.github.mmm.orm.jdbc.oracle.OracleConnectionPoolProvider;

}
