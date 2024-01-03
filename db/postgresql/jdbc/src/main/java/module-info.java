/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides ORM support for PostGreSQL database via JDBC.
 */
module io.github.mmm.orm.db.postgresql.jdbc {

  requires io.github.mmm.orm.db.postgresql.dialect;

  requires org.postgresql.jdbc;

}
