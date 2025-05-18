/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides ORM support for H2 database via JDBC.
 */
module io.github.mmm.orm.db.h2.jdbc {

  requires io.github.mmm.orm.db.h2.dialect;

  requires io.github.mmm.orm.jdbc;

  requires com.h2database;

  provides io.github.mmm.orm.connection.DbConnectionPoolProvider //
          with io.github.mmm.orm.db.h2.jdbc.connection.H2ConnectionPoolProvider;

}
