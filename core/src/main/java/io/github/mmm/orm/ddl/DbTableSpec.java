/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.ddl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.naming.DbNamingStrategy;

/**
 * {@link DbElement} for a database table.
 */
public final class DbTableSpec extends DbElement {

  private static final Map<String, DbTableSpec> TYPE_MAP = new ConcurrentHashMap<>();

  private final EntityBean entity;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param entity the {@link EntityBean}.
   * @see #of(EntityBean)
   */
  public DbTableSpec(String name, EntityBean entity) {

    super(name);
    this.entity = entity;
  }

  /**
   * @return the associated {@link EntityBean}. It is strictly forbidden to mutate this object and doing so will cause
   *         harm and might be explicitly prevented in future releases.
   */
  public EntityBean getEntity() {

    return this.entity;
  }

  @Override
  public String createName(DbNamingStrategy namingStrategy) {

    return namingStrategy.getTableName(this.entity);
  }

  /**
   * @param entity the {@link EntityBean}.
   * @return the corresponding {@link DbTableSpec}.
   */
  public static DbTableSpec of(EntityBean entity) {

    return TYPE_MAP.computeIfAbsent(entity.getType().getQualifiedName(), k -> new DbTableSpec(null, entity));
  }

}
