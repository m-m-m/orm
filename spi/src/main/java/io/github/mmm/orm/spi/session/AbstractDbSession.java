/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.spi.session;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.source.DbSource;

/**
 * Database session data for a single transaction.
 *
 * @since 1.0.0
 */
public abstract class AbstractDbSession implements DbSession {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractDbSession.class);

  private Map<String, DbEntitySession<?>> entitySessions;

  /** @see #getConnectionData() */
  protected final DbConnectionData connectionData;

  private final String entityMode;

  /**
   * The constructor.
   *
   * @param connectionData the {@link DbConnectionData}.
   */
  protected AbstractDbSession(DbConnectionData connectionData) {

    super();
    this.connectionData = connectionData;
    this.entitySessions = new HashMap<>();
    String mode = this.connectionData.getConfig().get(DbSource.KEY_ENTITY_MODE);
    if (mode == null) {
      mode = DbSource.VALUE_ENTITY_MODE_COPY;
    } else {
      mode = mode.toLowerCase(Locale.ROOT);
      if (!DbEntitySessionFactoryManager.get().getEntityModes().contains(mode)) {
        LOG.error("Ignoring unsupported value '{}' for property '{}{}'.", mode,
            this.connectionData.getSource().getPropertyPrefix(), DbSource.KEY_ENTITY_MODE);
        mode = DbSource.VALUE_ENTITY_MODE_COPY;
      }
    }
    this.entityMode = mode;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public <E extends EntityBean> DbEntitySession<E> get(E entity) {

    String key = entity.getType().getQualifiedName();
    DbEntitySession session = this.entitySessions.computeIfAbsent(key, this::newEntitySession);
    return session;
  }

  private <E extends EntityBean> DbEntitySession<E> newEntitySession(String key) {

    return DbEntitySessionFactoryManager.get().getFactory(this.entityMode).create();
  }

  /**
   * @return the {@link DbConnectionData}.
   */
  public DbConnectionData getConnectionData() {

    return this.connectionData;
  }

}
