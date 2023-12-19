/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.source;

import java.util.Set;

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
 * Now, in your {@link io.github.mmm.entity.repository.EntityRepository repository} implementation you may override the
 * {@link DbSource} to {@link DbSource#of(String) DbSource.of}("h2") to work on the secondary database.
 *
 * @see io.github.mmm.orm.repository.AbstractDbRepository#getSource()
 */
public final class DbSource {

  private static final DbSource DEFAULT = new DbSource("default");

  private final String id;

  /** The static part of the {@link #getPropertyPrefix() property prefix}. */
  public static final String PROPERTY_PREFIX_DB = "db.";

  /** {@link io.github.mmm.base.metainfo.MetaInfo#get(String) Key} for the database connection URL. */
  public static final String KEY_URL = "url";

  /** {@link io.github.mmm.base.metainfo.MetaInfo#get(String) Key} for the user login of the database connection. */
  public static final String KEY_USER = "user";

  /** {@link io.github.mmm.base.metainfo.MetaInfo#get(String) Key} for the user password of the database connection. */
  public static final String KEY_PASSWORD = "password";

  /**
   * {@link io.github.mmm.base.metainfo.MetaInfo#get(String) Key} for the
   * {@link io.github.mmm.orm.dialect.DbDialect#getId() name} of the {@link io.github.mmm.orm.dialect.DbDialect database
   * dialect}. Will be auto-configured if undefined.
   */
  public static final String KEY_DIALECT = "dialect";

  /**
   * {@link io.github.mmm.base.metainfo.MetaInfo#get(String) Key} for the type of database. This is very similar to the
   * {@link #KEY_DIALECT dialect} but for the same database type potentially different dialects may exist (e.g. due to
   * different versions of the database product). Will be auto-configured if undefined.
   */
  public static final String KEY_TYPE = "type";

  /**
   * {@link io.github.mmm.base.metainfo.MetaInfo#get(String) Key} for the ID of the connection pool to use (e.g.
   * "hikari", "c3po", or "dbcp"). Will be auto-configured if undefined.
   */
  public static final String KEY_POOL = "pool";

  /**
   * {@link io.github.mmm.base.metainfo.MetaInfo#get(String) Key} for the kind of database connection (e.g. "jdbc" or
   * "r2dbc"). Will be auto-configured if undefined.
   */
  public static final String KEY_KIND = "kind";

  /**
   * The standard keys for the database connection. Other keys will be specific for particular implementations (e.g.
   * connection pools).
   */
  public static final Set<String> STANDARD_KEYS = Set.of(KEY_URL, KEY_USER, KEY_PASSWORD, KEY_DIALECT, KEY_TYPE,
      KEY_POOL, KEY_KIND);

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
   * @return the property prefix as "db.«id»." (e.g. "db.default." for the {@link #get() default source}).
   * @see #PROPERTY_PREFIX_DB
   * @see io.github.mmm.base.metainfo.MetaInfo#with(String)
   */
  public String getPropertyPrefix() {

    return PROPERTY_PREFIX_DB + this.id + ".";
  }

  /**
   * @param key the unqualified property key.
   * @return the {@link #getPropertyPrefix() qualified} property key.
   */
  public String getPropertyKey(String key) {

    return PROPERTY_PREFIX_DB + this.id + "." + key;
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
