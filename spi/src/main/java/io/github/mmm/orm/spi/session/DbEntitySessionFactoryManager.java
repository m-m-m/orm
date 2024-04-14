package io.github.mmm.orm.spi.session;

import java.util.Set;

/**
 * Interface for the manager of the available {@link DbEntitySessionFactory} instances.
 *
 * @since 1.0.0
 */
public interface DbEntitySessionFactoryManager {

  /**
   * @param entityMode the {@link io.github.mmm.orm.source.DbSource#KEY_ENTITY_MODE entity-mode}.
   * @return the {@link DbEntitySessionFactory} for the given {@code entityMode} or {@code null} if no such factory is
   *         registered.
   */
  DbEntitySessionFactory getFactory(String entityMode);

  /**
   * @return an immuatble {@link Set} with the {@link io.github.mmm.orm.source.DbSource#KEY_ENTITY_MODE entity-modes}
   *         for which a {@link #getFactory(String) factory is available}.
   */
  Set<String> getEntityModes();

  /**
   * @return the instance of {@link DbEntitySessionFactoryManager}.
   */
  static DbEntitySessionFactoryManager get() {

    return io.github.mmm.orm.spi.session.impl.DbEntitySessionFactoryManagerImpl.INSTANCE;
  }

}
