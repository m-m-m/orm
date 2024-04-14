package io.github.mmm.orm.spi.session.impl;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.spi.session.DbEntitySession;
import io.github.mmm.orm.spi.session.DbEntitySessionFactory;

/**
 * Implementation of {@link DbEntitySessionFactory} for {@link DbSource#VALUE_ENTITY_MODE_INSTANCE}.
 *
 * @since 1.0.0
 */
public final class DbEntitySessionFactoryInstance implements DbEntitySessionFactory {

  @Override
  public <E extends EntityBean> DbEntitySession<E> create() {

    return new DbEntitySessionImplInstance<>(false);
  }

  @Override
  public String getEntityMode() {

    return DbSource.VALUE_ENTITY_MODE_INSTANCE;
  }

}
