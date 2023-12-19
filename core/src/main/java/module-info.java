
/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides database support for {@code mmm-entity-bean}.
 *
 * @uses io.github.mmm.orm.dialect.DbDialect
 */
module io.github.mmm.orm {

  requires transitive io.github.mmm.entity.bean;

  requires transitive io.github.mmm.binary;

  requires java.sql;

  uses io.github.mmm.orm.dialect.DbDialect;

  exports io.github.mmm.orm.access;

  exports io.github.mmm.orm.constraint;

  exports io.github.mmm.orm.dialect;

  exports io.github.mmm.orm.metadata;

  exports io.github.mmm.orm.metadata.impl to io.github.mmm.orm.jdbc;

  exports io.github.mmm.orm.naming;

  exports io.github.mmm.orm.orm;

  exports io.github.mmm.orm.param;

  exports io.github.mmm.orm.repository;

  exports io.github.mmm.orm.result;

  exports io.github.mmm.orm.source;

  exports io.github.mmm.orm.statement;

  exports io.github.mmm.orm.statement.create;

  exports io.github.mmm.orm.statement.delete;

  exports io.github.mmm.orm.statement.insert;

  exports io.github.mmm.orm.statement.merge;

  exports io.github.mmm.orm.statement.select;

  exports io.github.mmm.orm.statement.update;

  exports io.github.mmm.orm.statement.upsert;

  exports io.github.mmm.orm.tx;

  exports io.github.mmm.orm.type;

  exports io.github.mmm.orm.typemapping;

}