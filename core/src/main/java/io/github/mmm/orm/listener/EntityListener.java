package io.github.mmm.orm.listener;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.link.Link;
import io.github.mmm.orm.repository.EntityRepository;

/**
 * Interface for a listener of changes to an {@link EntityBean}. Methods are implemented to do nothing by default. You
 * need to override the according methods to react on the events.<br>
 * <b>ATTENTION:</b><br>
 * An {@link EntityListener} should typically never modify an {@link EntityBean}. If you decide to do so, there will be
 * the risk that future updates may cause problems or errors.<br>
 * In case an {@link EntityListener} causes a {@link Throwable}, it will not be catched and causes the operation to
 * fail.
 *
 * @param <E> type of the {@link EntityBean}.
 * @since 1.0.0
 */
public interface EntityListener<E extends EntityBean> {

  /**
   * Called before the {@link EntityBean} is inserted into the underlying store of the {@link EntityRepository}.
   *
   * @param entity the {@link EntityBean} to be inserted.
   */
  default void preInsert(E entity) {

  }

  /**
   * Called before the {@link EntityBean} is updated in the underlying store of the {@link EntityRepository}.
   *
   * @param entity the {@link EntityBean} to update.
   */
  default void preUpdate(E entity) {

  }

  /**
   * Called before the {@link EntityBean} is {@link EntityRepository#deleteById(Id) deleted} from the underlying store
   * of the {@link EntityRepository}.
   *
   * @param id the {@link EntityBean#getId() ID} of the {@link EntityBean} to remove.
   * @see EntityRepository#deleteById(Id)
   * @see EntityRepository#delete(EntityBean)
   * @see EntityRepository#deleteByLink(Link)
   */
  default void preDelete(Id<E> id) {

  }

  /**
   * Called before a {@link EntityRepository#deleteAllById(Iterable) batch delete} of {@link EntityBean}s from the
   * underlying store of the {@link EntityRepository}.
   *
   * @param ids the {@link Iterable} of the {@link EntityBean#getId() ID}s to remove.
   * @see EntityRepository#deleteAllById(Iterable)
   * @see EntityRepository#deleteAllByLink(Iterable)
   */
  default void preDelete(Iterable<Id<E>> ids) {

    for (Id<E> id : ids) {
      preDelete(id);
    }
  }

  /**
   * Called after an {@link EntityBean} has been {@link EntityRepository#findById(Id) loaded} from the underlying store
   * of the {@link EntityRepository}.
   *
   * @param entity the {@link EntityBean} that has been loaded.
   * @see EntityRepository#findById(Id)
   * @see EntityRepository#findAllById(Iterable)
   * @see EntityRepository#findByLink(Link)
   */
  default void postLoad(E entity) {

  }

}
