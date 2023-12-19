/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides ORM support for MySQL database via JDBC.
 */
module io.github.mmm.orm.db.mysql.jdbc {

  requires io.github.mmm.orm.db.mysql.dialect;

  requires io.github.mmm.orm.jdbc;

  requires mysql.connector.j;

}
