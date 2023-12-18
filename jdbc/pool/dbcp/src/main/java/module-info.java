/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides connection pooling via HikariCP for {@code mmm-orm-jdbc}.
 *
 * @provides io.github.mmm.orm.connection.DbConnectionPoolProvider
 */
@SuppressWarnings("rawtypes") //
module io.github.mmm.orm.jdbc.pool.hikari {

  requires transitive io.github.mmm.orm.jdbc;

  requires java.sql;

  requires org.apache.commons.dbcp2;

  provides io.github.mmm.orm.connection.DbConnectionPoolProvider
      with io.github.mmm.orm.jdbc.pool.dbcp.DbcpConnectionPoolProvider;

}
