package io.github.mmm.orm.statement;

import java.util.Collection;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.orm.statement.delete.DeleteClause;
import io.github.mmm.orm.statement.delete.DeleteStatement;
import io.github.mmm.orm.statement.select.SelectEntityClause;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * This class provides support for CRUD (Create, Retrieve, Update, Delete) operations.
 *
 * @param <E> type of the {@link EntityBean}.
 */
public class CrudSupport<E extends EntityBean> {

  /**
   * The constructor.
   */
  public CrudSupport() {

    super();
  }

  /**
   * @param id the {@link Id}.
   * @return the new {@link EntityBean} instance.
   */
  protected E createBean(Id<E> id) {

    return BeanFactory.get().create(id.getEntityClass());
  }

  /**
   * @param id the {@link Id} of the {@link EntityBean} to delete.
   * @return the {@link DeleteStatement} to delete by the given {@link Id}s.
   */
  public DeleteStatement<E> deleteById(Id<E> id) {

    if (id == null) {
      return null;
    }
    E entity = createBean(id);
    return new DeleteClause().from(entity).where(entity.Id().eq(id)).get();
  }

  /**
   * @param ids the {@link Collection} with the {@link Id}s of the {@link EntityBean}s to delete.
   * @return the {@link DeleteStatement} to delete by the given {@link Id}s.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public DeleteStatement<E> deleteByIds(Collection<Id<E>> ids) {

    if ((ids == null) || ids.isEmpty()) {
      return null;
    }
    E entity = createBean(ids.iterator().next());
    return new DeleteClause().from(entity).where(entity.Id().in((Collection) ids)).get();
  }

  /**
   * @param ids the {@link Collection} with the {@link Id}s of the {@link EntityBean}s to select.
   * @return the {@link SelectStatement} to select from the given {@link Id}s.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public SelectStatement<E> selectByIds(Collection<Id<E>> ids) {

    if ((ids == null) || ids.isEmpty()) {
      return null;
    }
    E entity = createBean(ids.iterator().next());
    SelectEntityClause<E> select = new SelectEntityClause<>(entity);
    return select.from().where(entity.Id().in((Collection) ids)).get();
  }

  /**
   * @param id the {@link Id} of the {@link EntityBean} to select.
   * @return the {@link SelectStatement} to select from the given {@link Id}.
   */
  public SelectStatement<E> selectById(Id<E> id) {

    if (id == null) {
      return null;
    }
    E entity = createBean(id);
    SelectEntityClause<E> select = new SelectEntityClause<>(entity);
    return select.from().where(entity.Id().eq(id)).get();
  }

}
