package io.github.mmm.orm.ddl;

import io.github.mmm.orm.ddl.constraint.DbConstraint;
import io.github.mmm.orm.naming.DbNamingStrategy;

/**
 * Abstract base class for a database DDL element such as a table, {@link DbColumnSpec column} or {@link DbConstraint
 * constraint}.
 */
public abstract class DbElement {

  private final String name;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public DbElement(String name) {

    super();
    this.name = name;
  }

  /**
   * @return the name of the predicate (e.g. "FK_Person_Order").
   */
  public String getName() {

    if (this.name == null) {
      return createName(DbNamingStrategy.of());
    }
    return this.name;
  }

  /**
   * @return {@code true} if this constraint has an explicit name.
   */
  public boolean hasName() {

    return (this.name != null);
  }

  /**
   * @param namingStrategy the {@link DbNamingStrategy}.
   * @return the computed {@link DbConstraint constraint} {@link #getName() name}.
   */
  public abstract String createName(DbNamingStrategy namingStrategy);

}
