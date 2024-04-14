package io.github.mmm.orm.spi.session;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.source.DbSource;

/**
 * Interface for a factory of an implementation of {@link DbEntitySession}.
 *
 * @since 1.0.0
 */
public interface DbEntitySessionFactory {

  /**
   * @param <E> type of the managed {@link EntityBean}.
   * @return the new instance of {@link DbEntitySession}.
   */
  <E extends EntityBean> DbEntitySession<E> create();

  /**
   * @return the {@link DbSource#KEY_ENTITY_MODE entity-mode}.
   */
  String getEntityMode();

}
