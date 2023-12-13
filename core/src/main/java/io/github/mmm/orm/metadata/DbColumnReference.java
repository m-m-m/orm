package io.github.mmm.orm.metadata;

/**
 *
 */
public interface DbColumnReference extends DbObject {

  /**
   * @return the {@link DbTable#getQualifiedName()}.
   */
  DbQualifiedName getTableName();

  /**
   * @return the {@link DbName#get() name} of the referenced column.
   */
  @Override
  DbName getName();

}
