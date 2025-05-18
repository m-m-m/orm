/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

import java.util.Objects;

/**
 * A potentially qualified identifier consisting of {@link #getCatalog() catalog}, {@link #getSchema() schema}, and
 * {@link #getName() name}. The {@link #getName() name} is always required, while the other attributes can be
 * {@code null}. In such case the default values of the database connection will apply.
 *
 * @since 1.0.0
 */
public final class DbQualifiedName {

  private final DbName catalog;

  private final DbName schema;

  private final DbName name;

  private final String qName;

  /**
   * The constructor.
   *
   * @param catalog the {@link #getCatalog() catalog}.
   * @param schema the {@link #getSchema() schema}.
   * @param name the {@link #getName() name}.
   */
  public DbQualifiedName(DbName catalog, DbName schema, DbName name) {

    super();
    Objects.requireNonNull(name);
    this.catalog = catalog;
    this.schema = schema;
    this.name = name;
    this.qName = buildQualifiedName();
  }

  private String buildQualifiedName() {

    String nameString = this.name.toString();
    int len = nameString.length();
    String catalogString = null;
    if (this.catalog != null) {
      catalogString = this.catalog.toString();
      len += catalogString.length() + 1;
    }
    String schemaString = null;
    if (this.schema != null) {
      schemaString = this.schema.toString();
      len += schemaString.length() + 1;
    }
    StringBuilder sb = new StringBuilder(len);
    if (catalogString != null) {
      sb.append(catalogString);
      sb.append('.');
    }
    if (schemaString != null) {
      sb.append(schemaString);
      sb.append('.');
    }
    sb.append(nameString);
    return sb.toString();
  }

  /**
   * @return the {@link DbName} of the catalog (a database in the DB server aka cluster) or {@code null} for no catalog.
   */
  public DbName getCatalog() {

    return this.catalog;
  }

  /**
   * @return the {@link DbName} of the schema (namespace containing tables) or {@code null} for no schema.
   */
  public DbName getSchema() {

    return this.schema;
  }

  /**
   * @return the {@link DbName} with the local database name. May not be {@code null}.
   */
  public DbName getName() {

    return this.name;
  }

  /**
   * @param newName the new {@link #getName() name}.
   * @return the new {@link DbQualifiedName} with the given {@link #getName() name}.
   */
  public DbQualifiedName withName(String newName) {

    return withName(DbName.of(newName));
  }

  /**
   * @param newName the new {@link #getName() name}.
   * @return the new {@link DbQualifiedName} with the given {@link #getName() name}.
   */
  public DbQualifiedName withName(DbName newName) {

    if (Objects.equals(newName, this.name)) {
      return this;
    }
    return new DbQualifiedName(this.catalog, this.schema, newName);
  }

  /**
   * @param newSchema the new {@link #getSchema() schema}.
   * @return the new {@link DbQualifiedName} with the given {@link #getSchema() schema}.
   */
  public DbQualifiedName withSchema(DbName newSchema) {

    if (Objects.equals(newSchema, this.schema)) {
      return this;
    }
    return new DbQualifiedName(this.catalog, newSchema, this.name);
  }

  /**
   * @param newCatalog the new {@link #getCatalog() catalog}.
   * @return the new {@link DbQualifiedName} with the given {@link #getCatalog() catalog}.
   */
  public DbQualifiedName withCatalog(DbName newCatalog) {

    if (Objects.equals(newCatalog, this.catalog)) {
      return this;
    }
    return new DbQualifiedName(newCatalog, this.schema, this.name);
  }

  /**
   * @return {@code true} if this {@link DbQualifiedName} has a qualifier ({@link #getCatalog() catalog} or
   *         {@link #getSchema() schema}), {@code false} otherwise (actually unqualified).
   */
  public boolean hasQualifier() {

    return (this.catalog != null) || (this.schema != null);
  }

  @Override
  public int hashCode() {

    return Objects.hash(this.catalog, this.schema, this.name);
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    } else if ((obj == null) || !(obj instanceof DbQualifiedName)) {
      return false;
    }
    DbQualifiedName other = (DbQualifiedName) obj;
    return Objects.equals(this.catalog, other.catalog) && Objects.equals(this.schema, other.schema)
        && Objects.equals(this.name, other.name);
  }

  @Override
  public String toString() {

    return this.qName;
  }

}
