package io.github.mmm.orm.spi.session.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import io.github.mmm.base.service.ServiceHelper;
import io.github.mmm.orm.spi.session.DbEntitySessionFactory;
import io.github.mmm.orm.spi.session.DbEntitySessionFactoryManager;

/**
 * Implementation of {@link DbEntitySessionFactoryManager}.
 *
 * @since 1.0.0
 */
public class DbEntitySessionFactoryManagerImpl implements DbEntitySessionFactoryManager {

  /** The singleton instance. */
  public static final DbEntitySessionFactoryManagerImpl INSTANCE = new DbEntitySessionFactoryManagerImpl();

  private final Map<String, DbEntitySessionFactory> factoryMap;

  private final Set<String> entityModes;

  /**
   * The constructor.
   */
  public DbEntitySessionFactoryManagerImpl() {

    super();
    this.factoryMap = new HashMap<>();
    ServiceHelper.all(ServiceLoader.load(DbEntitySessionFactory.class), this.factoryMap,
        DbEntitySessionFactory::getEntityMode, 4);
    this.entityModes = Collections.unmodifiableSet(this.factoryMap.keySet());
  }

  @Override
  public DbEntitySessionFactory getFactory(String entityMode) {

    return this.factoryMap.get(entityMode);
  }

  @Override
  public Set<String> getEntityModes() {

    return this.entityModes;
  }

}
