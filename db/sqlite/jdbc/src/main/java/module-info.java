/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides ORM support for SQLite database via JDBC.
 */
module io.github.mmm.orm.db.sqlite.jdbc {

  requires io.github.mmm.orm.db.sqlite.dialect;

  requires io.github.mmm.orm.jdbc;

  requires org.xerial.sqlitejdbc;

  provides io.github.mmm.orm.connection.DbConnectionPoolProvider //
          with io.github.mmm.orm.db.sqlite.jdbc.connection.SqliteConnectionPoolProvider;

}
