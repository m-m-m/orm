/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Provides ORM support for Oracle database via JDBC.
 *
 * @provides io.github.mmm.orm.connection.DbConnectionPoolProvider
 */
@SuppressWarnings("rawtypes") //
module io.github.mmm.orm.db.oracle.jdbc {

  requires io.github.mmm.orm.db.oracle.dialect;

  requires io.github.mmm.orm.jdbc;

  requires ucp;

  requires ojdbc10;

  provides io.github.mmm.orm.connection.DbConnectionPoolProvider //
      with io.github.mmm.orm.db.oracle.jdbc.connection.OracleConnectionPoolProvider;

}
