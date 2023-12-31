/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.mmm.entity.bean.EntityBean;

/**
 * A {@link AbstractEntitiesClause} is a {@link DbClause} of an SQL {@link DbStatement} that specifies the
 * {@link #getEntity() entity} and/or {@link #getEntityName() entity name} (table) to operate on.
 *
 * @param <R> type of the result. Only different from {@literal <E>} for complex selects.
 * @param <E> type of the {@link #getEntity() entity}.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class AbstractEntitiesClause<R, E extends EntityBean, SELF extends AbstractEntitiesClause<R, E, SELF>>
    extends AbstractEntityClause<R, E, SELF> {

  /** Name of property {@link #getEntityName()} for marshalling. */
  public static final String NAME_ADDITIONAL_ENTITIES = "and";

  private final List<EntitySubClause<?, ?>> additionalEntities;

  /**
   * The constructor.
   *
   * @param aliasMap the {@link #getAliasMap() aliasMap}.
   * @param entity the {@link #getEntity() entity} to operate on.
   * @param entityName the {@link #getEntityName() entity name}.
   */
  protected AbstractEntitiesClause(AliasMap aliasMap, E entity, String entityName) {

    super(aliasMap, entity, entityName);
    this.additionalEntities = new ArrayList<>();
  }

  /**
   * @param additionalEntity the additional {@link EntitySubClause#getEntity() entity} to access (similar to a JOIN).
   * @return this {@link DbClause} itself for fluent API calls.
   */
  public SELF and(EntityBean additionalEntity) {

    return and(new EntitySubClause<>(getAliasMap(), additionalEntity, null, null));
  }

  /**
   * @param additionalEntity the additional {@link EntitySubClause#getEntity() entity} to access (similar to a JOIN).
   * @param alias the {@link EntitySubClause#getAlias() alias}.
   * @return this {@link DbClause} itself for fluent API calls.
   */
  public SELF and(EntityBean additionalEntity, String alias) {

    return and(new EntitySubClause<>(getAliasMap(), additionalEntity, null, alias));
  }

  /**
   * @param entityName the {@link EntitySubClause#getEntityName() entity name}.
   * @param additionalEntity the additional {@link EntitySubClause#getEntity() entity} to access (similar to a JOIN).
   * @param alias the {@link EntitySubClause#getAlias() alias}.
   * @return this {@link DbClause} itself for fluent API calls.
   */
  public SELF and(String entityName, EntityBean additionalEntity, String alias) {

    return and(new EntitySubClause<>(getAliasMap(), additionalEntity, entityName, alias));
  }

  /**
   * @param entityFragment the {@link EntitySubClause} to add.
   * @return this {@link DbClause} itself for fluent API calls.
   */
  protected SELF and(EntitySubClause<?, ?> entityFragment) {

    Objects.requireNonNull(entityFragment);
    this.additionalEntities.add(entityFragment);
    return self();
  }

  @Override
  public SELF as(String entityAlias) {

    if (this.additionalEntities.isEmpty()) {
      return super.as(entityAlias);
    } else {
      EntitySubClause<?, ?> last = this.additionalEntities.get(this.additionalEntities.size() - 1);
      last.as(entityAlias);
      return self();
    }
  }

  /**
   * @return the {@link List} of {@link EntitySubClause}s in addition to the {@link #getEntity() main entity}.
   */
  public List<EntitySubClause<?, ?>> getAdditionalEntities() {

    return this.additionalEntities;
  }

}
