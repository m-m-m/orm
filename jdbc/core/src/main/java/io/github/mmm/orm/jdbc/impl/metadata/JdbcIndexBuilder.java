/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.impl.metadata;

import io.github.mmm.base.sort.SortOrder;
import io.github.mmm.orm.metadata.DbColumn;
import io.github.mmm.orm.metadata.DbIndex;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.impl.DbIndexImpl;

/**
 * {@link JdbcContainerBuilder} to build a {@link DbIndex}.
 */
public class JdbcIndexBuilder extends JdbcContainerBuilder<DbColumn> {

  private final DbName name;

  private SortOrder sortOrder;

  private int type;

  private boolean populated;

  /**
   * The constructor.
   *
   * @param name the name of the foreign key.
   */
  public JdbcIndexBuilder(DbName name) {

    super();
    this.name = name;
  }

  /**
   * @param sortOrder the value of {@link DbIndex#getSortOrder()}.
   */
  public void setSortOrder(SortOrder sortOrder) {

    if (this.populated) {
      assert (this.sortOrder == sortOrder);
    }
    this.sortOrder = sortOrder;
  }

  /**
   * @param type the value of {@link DbIndex#getTypeCode()}.
   */
  public void setType(int type) {

    this.type = type;
  }

  /**
   * To be called if the regular values are populated from the first result.
   */
  public void setPopulated() {

    this.populated = true;
  }

  /**
   * @return the new {@link DbIndex} created from this builder.
   */
  public DbIndex buildIndex() {

    return new DbIndexImpl(this.name, this.sortOrder, build());
  }

}
