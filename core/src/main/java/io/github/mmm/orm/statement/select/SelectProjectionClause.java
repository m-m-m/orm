/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import java.util.List;
import java.util.Objects;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.criteria.CriteriaAggregation;
import io.github.mmm.property.criteria.ProjectionProperty;
import io.github.mmm.value.PropertyPath;

/**
 * {@link SelectClause} to query a projection. This is called a <em>constructor query</em> in JPA such as
 *
 * <pre>
 * SELECT new com.example.ResultBean(sum(i.price), count(i.price)) FROM Item AS i GROUP BY i.order
 * </pre>
 *
 * As analogy we can create {@code ResultBean} as {@link WritableBean}:
 *
 * <pre>
 * public interface ResultBean extends {@link WritableBean} {
 *   {@link io.github.mmm.property.number.bigdecimal.BigDecimalProperty} Total();
 *   {@link io.github.mmm.property.number.integers.IntegerProperty} Count();
 *   static Result of() {
 *     return {@link io.github.mmm.bean.BeanFactory#get()}.{@link io.github.mmm.bean.BeanFactory#create(Class) create}(Result.class);
 *   }
 * }
 * </pre>
 *
 * Now we can create the above JPA query example as following:
 *
 * <pre>
 * ResultBean result = ResultBean.of();
 * Item item = Item.of();
 * SelectStatement<ResultBean> query = new {@link SelectProjectionClause}<>(result, item.Price().count()).from(item).as("i").groupBy(i.Order()).get();
 * </pre>
 *
 * @param <R> type of the result of the selection.
 * @since 1.0.0
 */
public final class SelectProjectionClause<R extends WritableBean> extends SelectClause<R> {

  /**
   * The constructor.
   *
   * @param resultBean the {@link #getResultBean() result bean} to project to (via "constructor query").
   */
  public SelectProjectionClause(R resultBean) {

    super(resultBean);
  }

  /**
   * The constructor.
   *
   * @param <V> type of the type of the {@link PropertyPath#get() property value}.
   * @param resultBean the {@link #getResultBean() result bean} to project to (via "constructor query").
   * @param selection the {@link PropertyPath} to add to the selection.
   * @param property the {@link ProjectionProperty#getProperty() projection property} to map to.
   */
  public <V> SelectProjectionClause(R resultBean, PropertyPath<V> selection, PropertyPath<V> property) {

    this(resultBean);
    and(selection, property);
  }

  /**
   * The constructor.
   *
   * @param <V> type of the type of the {@link PropertyPath#get() property value}.
   * @param resultBean the {@link #getResultBean() result bean} to project to (via "constructor query").
   * @param selection the {@link CriteriaAggregation} to add to the selection.
   * @param property the {@link ProjectionProperty#getProperty() projection property} to map to.
   */
  public <V extends Number & Comparable<V>> SelectProjectionClause(R resultBean, CriteriaAggregation<V> selection,
      PropertyPath<V> property) {

    this(resultBean);
    and(selection, property);
  }

  /**
   * @param <V> type of the type of the {@link PropertyPath#get() property value}.
   * @param selection the {@link PropertyPath} to add to the selection.
   * @param property the {@link ProjectionProperty#getProperty() projection property} to map to.
   * @return this {@link SelectClause} for fluent API calls.
   */
  public <V> SelectProjectionClause<R> and(PropertyPath<V> selection, PropertyPath<V> property) {

    ProjectionProperty<V> projectionProperty = ProjectionProperty.of(selection, property);
    return and(projectionProperty);
  }

  /**
   * @param <V> type of the type of the {@link PropertyPath#get() property value}.
   * @param selection the {@link CriteriaAggregation} to add to the selection.
   * @param property the {@link ProjectionProperty#getProperty() projection property} to map to.
   * @return this {@link SelectClause} for fluent API calls.
   */
  public <V extends Number & Comparable<V>> SelectProjectionClause<R> and(CriteriaAggregation<V> selection,
      PropertyPath<V> property) {

    ProjectionProperty<V> projectionProperty = ProjectionProperty.of(selection, property);
    return and(projectionProperty);
  }

  /**
   * @param projectionProperty the {@link ProjectionProperty} to add to the selection.
   * @return this {@link SelectProjectionClause} for fluent API calls.
   */
  public SelectProjectionClause<R> and(ProjectionProperty<?> projectionProperty) {

    Objects.requireNonNull(projectionProperty, "projectionProperty");
    add(projectionProperty);
    return this;
  }

  /**
   * @param projectionProperties the {@link ProjectionProperty} instances to add to the selection.
   * @return this {@link SelectProjectionClause} for fluent API calls.
   */
  public SelectProjectionClause<R> and(ProjectionProperty<?>... projectionProperties) {

    for (ProjectionProperty<?> property : projectionProperties) {
      add(property);
    }
    return this;
  }

  @Override
  public SelectProjectionClause<R> distinct() {

    super.distinct();
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ProjectionProperty<?>> getSelections() {

    return (List<ProjectionProperty<?>>) super.getSelections();
  }

  @Override
  public <E extends EntityBean> SelectFromClause<R, E> from(E entity) {

    if (getSelections().isEmpty()) {
      throw new IllegalStateException("Selections must not be empty! Call 'and' method at least once!");
    }
    return super.from(entity);
  }

  @Override
  public boolean isSelectEntity() {

    return false;
  }

  @Override
  public boolean isSelectResult() {

    return false;
  }

  @Override
  public boolean isSelectSingle() {

    return false;
  }
}
