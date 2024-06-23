/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.repository;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.repository.operation.DbDeleteOperations;
import io.github.mmm.orm.repository.operation.DbFindOperations;
import io.github.mmm.orm.repository.operation.DbUpdateOperations;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * {@link EntityRepository} allowing {@link #findByQuery(SelectStatement)}
 *
 * @param <E> type of the managed {@link EntityBean}.
 */
public interface DbRepository<E extends EntityBean>
    extends EntityRepository<E>, DbFindOperations<E>, DbDeleteOperations<E>, DbUpdateOperations<E> {

}
