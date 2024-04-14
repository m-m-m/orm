/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides database support for {@code mmm-entity-bean}.
 *
 * @provides io.github.mmm.orm.spi.session.DbEntitySessionFactory
 * @uses io.github.mmm.orm.spi.access.DbAccessProvider
 * @uses io.github.mmm.orm.spi.session.DbEntitySessionFactory
 * @uses io.github.mmm.orm.connection.DbConnectionPoolProvider
 */
@SuppressWarnings("rawtypes") //
module io.github.mmm.orm.spi {

  requires transitive io.github.mmm.orm;

  requires java.sql;

  uses io.github.mmm.orm.connection.DbConnectionPoolProvider;

  uses io.github.mmm.orm.spi.session.DbEntitySessionFactory;

  provides io.github.mmm.orm.spi.session.DbEntitySessionFactory with //
      io.github.mmm.orm.spi.session.impl.DbEntitySessionFactoryInstance,
      io.github.mmm.orm.spi.session.impl.DbEntitySessionFactoryCopy,
      io.github.mmm.orm.spi.session.impl.DbEntitySessionFactoryReadOnlyInstance,
      io.github.mmm.orm.spi.session.impl.DbEntitySessionFactoryReadOnlyCopy;

  uses io.github.mmm.orm.spi.access.DbAccessProvider;

  exports io.github.mmm.orm.spi.access;

  exports io.github.mmm.orm.spi.repository;

  exports io.github.mmm.orm.spi.sequence;

  exports io.github.mmm.orm.spi.session;

  exports io.github.mmm.orm.connection;

}
