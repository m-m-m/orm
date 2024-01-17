/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.dialect;

import java.util.Map;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.entity.bean.typemapping.TypeMapping;
import io.github.mmm.orm.impl.OrmImpl;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.naming.DbNamingStrategy;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.statement.AbstractDbStatementFormatter;

/**
 * Abstract base implementation of {@link DbDialect}.
 *
 * @param <SELF> this type itself for fluent API calls.
 * @since 1.0.0
 */
public abstract class AbstractDbDialect<SELF extends AbstractDbDialect<SELF>> implements DbDialect {

  private final OrmImpl orm;

  /**
   * The constructor.
   *
   * @param orm the {@link Orm}.
   */
  protected AbstractDbDialect(Orm orm) {

    super();
    this.orm = (OrmImpl) orm;
  }

  /**
   * The constructor.
   *
   * @param typeMapping the {@link TypeMapping}.
   */
  protected AbstractDbDialect(TypeMapping typeMapping) {

    super();
    this.orm = new OrmImpl(typeMapping, getDefaultNamingStrategy());
  }

  /**
   * @return the default {@link DbNamingStrategy}.
   */
  protected DbNamingStrategy getDefaultNamingStrategy() {

    return DbNamingStrategy.ofRdbms();
  }

  @Override
  public DbNamingStrategy getNamingStrategy() {

    return this.orm.getNamingStrategy();
  }

  /**
   * @return the {@link Orm}.
   */
  public Orm getOrm() {

    return this.orm;
  }

  /**
   * @param namingStrategy the new {@link DbNamingStrategy} to use.
   * @return this dialect for the given {@link DbNamingStrategy}.
   */
  @SuppressWarnings("unchecked")
  public SELF withNamingStrategy(DbNamingStrategy namingStrategy) {

    OrmImpl newOrm = this.orm.withNamingStrategy(namingStrategy);
    if (newOrm == this.orm) {
      return (SELF) this;
    }
    return withOrm(newOrm);
  }

  /**
   * @param newOrm the new {@link Orm} to use.
   * @return a new instance of this dialect with the given {@link Orm}.
   */
  protected abstract SELF withOrm(Orm newOrm);

  @Override
  public AbstractDbStatementFormatter createFormatter() {

    return new DbDialectStatementFormatter(this);
  }

  /**
   * @param config the {@link Map} with the configuration parameters for the {@link DbSource}.
   * @param source the {@link DbSource}.
   */
  public void autoConfigure(Map<String, String> config, DbSource source) {

    if (config.get(DbSource.KEY_PASSWORD) == null) {
      String user = config.get(DbSource.KEY_USER);
      if (user != null) {
        // convention over configuration for local development environments
        config.put(DbSource.KEY_PASSWORD, user);
      }
    }
    if (config.get(DbSource.KEY_URL) == null) {
      String kind = config.get(DbSource.KEY_KIND);
      if (kind == null) {
        // would actually be better to discover what is available...
        kind = "jdbc";
        config.put(DbSource.KEY_KIND, kind);
      }
      String url = autoConfigureUrl(config, source, kind);
      if (url == null) {
        throw new ObjectNotFoundException("Property", source.getPropertyKey(DbSource.KEY_URL));
      }
      config.put(DbSource.KEY_URL, url);
    }
  }

  /**
   * @param config the {@link Map} with the configuration parameters for the {@link DbSource}.
   * @param source the {@link DbSource}.
   * @param kind the {@link DbSource#KEY_KIND kind of database connection}.
   * @return the auto-configured database connection URL or {@code null} if there is no meaningful default.
   */
  protected String autoConfigureUrl(Map<String, String> config, DbSource source, String kind) {

    return null;
  }

  @Override
  public String toString() {

    return getId() + "[" + getClass().getSimpleName() + "]";
  }

}
