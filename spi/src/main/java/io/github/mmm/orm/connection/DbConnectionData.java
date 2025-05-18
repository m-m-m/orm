package io.github.mmm.orm.connection;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.connection.impl.DbConnectionDataManager;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
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
   * @return the {@link DbSource#KEY_SCHEMA schema} or {@code null} if not explicitly configured.
   */
  default String getSchema() {

    return getConfig().get(DbSource.KEY_SCHEMA);
  }

  /**
   * @return the {@link DbSource#KEY_CATALOG schema} or {@code null} if not explicitly configured.
   */
  default String getCatalog() {

    return getConfig().get(DbSource.KEY_CATALOG);
  }

  /**
   * @return the {@link DbQualifiedName} with {@link #getSchema() schema} and {@link #getCatalog() catalog} to use as
   *         template.
   * @see DbQualifiedName#withName(String)
   * @see DbQualifiedName#withName(DbName)
   */
  DbQualifiedName getQualifiedNameTemplate();

  /**
   * @return the {@link DbSource#KEY_SEQUENCE_INCREMENT sequence increment} value.
   */
  default int getSequenceIncrement() {

    return getConfig().getAsInteger(DbSource.KEY_SEQUENCE_INCREMENT, DbSource.VALUE_SEQUENCE_INCREMENT_DEFAULT);
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
