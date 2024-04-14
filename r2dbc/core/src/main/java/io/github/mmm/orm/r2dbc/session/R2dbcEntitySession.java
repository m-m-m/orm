/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.r2dbc.session;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.spi.session.DbEntitySession;

/**
 * {@link DbEntitySession} for JDBC.
 *
 * @param <E> type of the managed {@link EntityBean}.
 */
public abstract class R2dbcEntitySession<E extends EntityBean> implements DbEntitySession<E> {

  /**
   * The constructor.
   *
   * @param connection the JDBC {@link Connection}.
   */
  public R2dbcEntitySession() {

    super();
    // this.connection.getMetaData().getColumns(null, null, null, "%");
  }

}
