/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.config;

import io.github.mmm.entity.repository.EntityRepository;

/**
 * A {@link DbSource} identifies a database to connect to as a tenant. It is just a wrapper for a {@link String} used as
 * {@link #getId() identifier}. Most applications only need to talk to a single database and schema. There is always a
 * {@link DbSource#get() default} {@link DbSource}. However, to support multi-tenancy and connecting to multiple
 * different databases the {@link DbSource} is used to identify the tenant. <br>
 * From a logical point-of-view a {@link DbSource} is similar to a {@link javax.sql.DataSource}. However, it is a much
 * higher abstraction. Only in case a {@link DbSource} points to an RDBMS and you are using JDBC to connect then there
 * will be a {@link javax.sql.DataSource} associated under the hood to talk to that database.<br>
 * When you configure database settings, you will always include the {@link DbSource} {@link #getId() identifier} in the
 * property names:
 *
 * <pre>
 * db.default.url=jdbc:postgresql://db.company.com:5432/db
 * db.default.dialect=postgresql
 * db.default.user=admin
 * db.default.password=top$ecret
 * db.default.pool=hikari
 * </pre>
 *
 * So in order to define a secondary database you can simply use a {@link #getId() identifier} other than "default":
 *
 * <pre>
 * db.h2.url=jdbc:h2:mem:db
 * db.h2.dialect=h2
 * db.h2.user=sa
 * db.h2.password=
 * db.h2.pool=hikari
 * </pre>
 *
 * Now, in your {@link EntityRepository} implementation you may override the {@link DbSource} to
 * {@link DbSource#of(String) DbSource.of}("h2") to work on the secondary database.
 *
 * @see io.github.mmm.orm.repository.AbstractDbRepository#getSource()
 */
public final class DbSource {

  private static final DbSource DEFAULT = new DbSource("default");

  private final String id;

  private DbSource(String name) {

    super();
    this.id = name;
  }

  /**
   * @return the identifier of the database as tenant.
   */
  public String getId() {

    return this.id;
  }

  /**
   * @return the default {@link DbSource} for the "primary" database. It has the {@link #getId() identifier} "default".
   */
  public static DbSource get() {

    return DEFAULT;
  }

  /**
   * @param id the {@link #getId() identifier} to wrap as {@link DbSource}.
   * @return a {@link DbSource} with the given {@code id}. Will be the {@link #get() default} {@link DbSource} if
   *         {@code id} is {@code null} or "default".
   */
  public static DbSource of(String id) {

    if ((id == null) || id.equals(DEFAULT.id)) {
      return DEFAULT;
    }
    return new DbSource(id);
  }

}
