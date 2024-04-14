package io.github.mmm.orm.spi.session.impl;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.spi.session.DbEntitySession;
import io.github.mmm.orm.spi.session.DbEntitySessionFactory;

/**
 * Implementation of {@link DbEntitySessionFactory} for {@link DbSource#VALUE_ENTITY_MODE_READONLY_COPY}.
 *
 * @since 1.0.0
 */
public final class DbEntitySessionFactoryReadOnlyCopy implements DbEntitySessionFactory {

  @Override
  public <E extends EntityBean> DbEntitySession<E> create() {

    return new DbEntitySessionImplCopy<>(true);
  }

  @Override
  public String getEntityMode() {

    return DbSource.VALUE_ENTITY_MODE_READONLY_COPY;
  }

}
