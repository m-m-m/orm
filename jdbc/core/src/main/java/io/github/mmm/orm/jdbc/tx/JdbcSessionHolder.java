/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.tx;

import java.util.HashMap;
import java.util.Map;

import io.github.mmm.orm.config.DbSource;
import io.github.mmm.orm.jdbc.access.session.JdbcSession;

/**
 * Holder for {@link JdbcSession} by {@link DbSource}.
 */
public class JdbcSessionHolder {

  JdbcSession session;

  private Map<DbSource, JdbcSession> sessionMap;

  /**
   * The constructor.
   */
  public JdbcSessionHolder() {

    super();
  }

  /**
   * @return session
   */
  public JdbcSession getSession(DbSource source) {

    if ((source == null) || (DbSource.get() == source)) {
      return this.session;
    }
    if (this.sessionMap == null) {
      return null;
    }
    return this.sessionMap.get(this.sessionMap.get(source));
  }

  void setSession(DbSource source, JdbcSession session) {

    if ((source == null) || (DbSource.get() == source)) {
      this.session = session;
    } else {
      if (this.sessionMap == null) {
        this.sessionMap = new HashMap<>();
      }
      this.sessionMap.put(source, session);
    }

  }

}
