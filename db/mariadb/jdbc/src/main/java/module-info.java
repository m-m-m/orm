/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides ORM support for H2 database via JDBC.
 */
module io.github.mmm.orm.db.mariadb.jdbc {

  requires io.github.mmm.orm.db.mariadb.dialect;

  requires io.github.mmm.orm.jdbc;

  requires org.mariadb.jdbc;

}
