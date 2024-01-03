/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.sync;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.connection.DbConnection;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.metadata.DbMetaData;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.metadata.DbTable;
import io.github.mmm.orm.repository.EntityRepository;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.spi.access.DbAccess;
import io.github.mmm.orm.spi.repository.AbstractDbRepository;
import io.github.mmm.orm.spi.repository.EntityRepositoryManager;
import io.github.mmm.property.WritableProperty;

/**
 * Implementation of {@link ModelSynchronizer}.
 *
 */
public class ModelSynchronizerImpl implements ModelSynchronizer {

  private DbConnection connection;

  private DbDialect dialect;

  private DbAccess dbAccess;

  @Override
  public void sync() {

    EntityRepositoryManager erm = EntityRepositoryManager.get();
    for (EntityRepository<?> repository : erm) {
      if (repository instanceof AbstractDbRepository<?> aer) {
        DbSource source = aer.getSource();
        EntityBean entity = aer.getPrototype();
        sync(entity);
      }
    }
  }

  @Override
  public void sync(Class<? extends EntityBean> entityClass) {

    BeanFactory factory = BeanFactory.get();
    EntityBean entity = factory.create(entityClass);
    sync(entity);
  }

  @Override
  public void sync(EntityBean entity) {

    DbMetaData metaData = this.connection.getMetaData();
    DbName catalog = metaData.getCurrentCatalog();
    DbName schema = metaData.getCurrentSchema();
    DbName tableName = DbName.of(this.dialect.getNamingStrategy().getTableName(entity));
    DbQualifiedName qName = new DbQualifiedName(catalog, schema, tableName);
    DbTable table = metaData.getTable(qName);
    if (table == null) {
      this.dbAccess.createTable(entity);
      return;
    }
    for (WritableProperty<?> property : entity.getProperties()) {
      if (!property.isTransient()) {
        String name = property.getName();
      }
    }
  }

}
