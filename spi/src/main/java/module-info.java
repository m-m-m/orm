
/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides database support for {@code mmm-entity-bean}.
 *
 * @provides io.github.mmm.orm.dialect.DbDialect
 * @uses io.github.mmm.orm.connection.DbConnectionPoolProvider
 * @uses io.github.mmm.orm.dialect.DbDialect
 */
@SuppressWarnings("rawtypes") //
module io.github.mmm.orm.spi {

  requires transitive io.github.mmm.orm;

  requires java.sql;

  uses io.github.mmm.orm.connection.DbConnectionPoolProvider;

  exports io.github.mmm.orm.spi.access;

  exports io.github.mmm.orm.spi.repository;

  exports io.github.mmm.orm.spi.session;

  exports io.github.mmm.orm.connection;

}
