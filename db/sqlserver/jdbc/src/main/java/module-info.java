/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides ORM support for Microsoft SQL-Server database via JDBC.
 */
module io.github.mmm.orm.db.sqlserver.jdbc {

  requires io.github.mmm.orm.db.sqlserver.dialect;

  requires io.github.mmm.orm.jdbc;

  requires com.microsoft.sqlserver.jdbc;
}
