/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.repository.operation;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.repository.EntityRepository;
import io.github.mmm.property.ReadableProperty;

/**
 * Interface for the {@link #findByText(String)} operation of an {@link EntityRepository}. This operation is only
 * supported if the {@link EntityRepository} has a full-text-search index.<br>
 * The syntax of the text query is not entirely specified and may be specific for the underlying technology used. In any
 * case the text query should be split into words separated by spaces. Each word should be searched as sub-string and is
 * typically stemmed. Advanced full-text-engines may support additional features (e.g. "«property»:«word»" to search for
 * the word «word» in the property «property»).
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public interface EntityFindByTextOperation<E extends EntityBean> {

  /**
   * Performs a full-text search on all {@link EntityBean#getProperties() properties} that are indexed.
   *
   * @param text the query text to find.
   * @return an {@link Iterable} with all entities of this repository.
   */
  Iterable<E> findByText(String text);

  /**
   * Performs a full-text search on the specified {@link ReadableProperty property}.
   *
   * @param text the query text to find.
   * @param property the dedicated {@link ReadableProperty property} to search on. All other properties shall be
   *        excluded for this search. Needs to be a {@link EntityBean#getProperties() property of} the managed
   *        {@link EntityBean}.
   * @return an {@link Iterable} with all entities of this repository.
   */
  Iterable<E> findByText(String text, ReadableProperty<?> property);

}
