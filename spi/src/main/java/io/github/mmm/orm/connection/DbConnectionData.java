package io.github.mmm.orm.connection;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.connection.impl.DbConnectionDataManager;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.source.DbSource;

/**
 * Interface for the data corresponding to a {@link DbSource}.
 */
public interface DbConnectionData {

  /**
   * @return the {@link DbDialect} used to talk to the database.
   */
  DbDialect getDialect();

  /**
   * @return the original {@link DbSource} of this data.
   */
  DbSource getSource();

  /**
   * @return the configuration as {@link MetaInfo}.
   */
  MetaInfo getConfig();

  /**
   * @return the {@link DbSource#KEY_URL database connection URL}.
   */
  default String getUrl() {

    return getConfig().getRequired(DbSource.KEY_URL);
  }

  /**
   * @return the {@link DbSource#KEY_USER database connection user login}.
   */
  default String getUser() {

    return getConfig().getRequired(DbSource.KEY_USER);
  }

  /**
   * @return the {@link DbSource#KEY_USER database connection user password}.
   */
  default String getPassword() {

    return getConfig().getRequired(DbSource.KEY_PASSWORD);
  }

  /**
   * @return the {@link DbSource#KEY_KIND kind of database connection}.
   */
  default String getKind() {

    return getConfig().get(DbSource.KEY_KIND);
  }

  /**
   * @return the {@link DbConnectionPool} used to manage {@link io.github.mmm.orm.connection.DbConnection connections}.
   */
  DbConnectionPool<?> getPool();

  /**
   * @param source the {@link DbSource}.
   * @return the {@link DbConnectionData} for the given {@link DbSource}. Will be created on the first call with the
   *         same {@link DbSource}.
   */
  static DbConnectionData of(DbSource source) {

    return DbConnectionDataManager.INSTANCE.getOrCreate(source);
  }

}
