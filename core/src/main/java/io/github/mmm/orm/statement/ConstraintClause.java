/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.criteria.CriteriaPredicate;
import io.github.mmm.value.PropertyPath;

/**
 * {@link DbClause} containing {@link #getProperties() properties} like a
 * {@link io.github.mmm.orm.statement.select.GroupByClause}-clause.
 *
 * @param <E> type of the {@link EntityBean} to query.
 * @param <SELF> type of this class itself.
 * @since 1.0.0
 */
public abstract class ConstraintClause<E extends EntityBean, SELF extends ConstraintClause<E, SELF>>
    extends AbstractTypedClause<E, SELF> {

  private final List<PropertyPath<?>> properties;

  /**
   * The constructor.
   */
  public ConstraintClause() {

    super();
    this.properties = new ArrayList<>();
  }

  /**
   * @param property the {@link PropertyPath} to add.
   * @return this {@link DbClause} itself for fluent API calls.
   */
  public SELF and(PropertyPath<?> property) {

    Objects.requireNonNull(property, "properety");
    this.properties.add(property);
    return self();
  }

  /**
   * @param paths the {@link PropertyPath}s to add.
   * @return this {@link DbClause} itself for fluent API calls.
   */
  public SELF and(PropertyPath<?>... paths) {

    for (PropertyPath<?> property : paths) {
      and(property);
    }
    return self();
  }

  /**
   * @return the {@link List} of {@link CriteriaPredicate}s.
   */
  public List<PropertyPath<?>> getProperties() {

    return this.properties;
  }

  @Override
  public boolean isOmit() {

    return getProperties().isEmpty();
  }

}
