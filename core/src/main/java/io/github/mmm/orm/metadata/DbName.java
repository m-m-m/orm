/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

import java.util.Objects;

import io.github.mmm.orm.dialect.DbDialect;

/**
 * Name of a database object (schema, table, column, etc.).
 */
public final class DbName {

  private final String name;

  private final boolean quoted;

  private DbName(String name, boolean quoted) {

    super();
    Objects.requireNonNull(name);
    if ("null".equals(name)) {
      throw new IllegalArgumentException(name);
    }
    this.name = name;
    this.quoted = quoted;
  }

  /**
   * @return the unquoted identifier as {@link String}.
   */
  public String get() {

    return this.name;
  }

  /**
   * @return {@code true} if this identifier shall be quoted, {@code false} otherwise.
   */
  public boolean isQuoted() {

    return this.quoted;
  }

  @Override
  public int hashCode() {

    return this.name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    } else if ((obj == null) || (obj.getClass() != DbName.class)) {
      return false;
    }
    DbName other = (DbName) obj;
    return this.name.equals(other.name);
    // intentionally we do not consider quotation here...
    // && this.quoted == other.quoted;
  }

  @Override
  public String toString() {

    return toString(null);
  }

  /**
   * @param dialect the {@link DbDialect} to use.
   * @return the {@link String} representation of this identifier.
   * @see #toString()
   * @see #isQuoted()
   * @see DbDialect#quoteIdentifier(String, Appendable)
   */
  public String toString(DbDialect dialect) {

    if (!this.quoted) {
      return this.name;
    }
    StringBuilder sb = new StringBuilder(this.name.length() + 2);
    if (dialect == null) {
      sb.append('"');
      sb.append(this.name);
      sb.append('"');
    } else {
      dialect.quoteIdentifier(this.name, sb);
    }
    return sb.toString();
  }

  /**
   * @param name the {@link #get() name}. May be {@link #isQuoted() quoted}.
   * @return the new {@link DbName} or {@code null} if the given {@link String} was {@code null} or
   *         {@link String#isBlank() blank}.
   */
  public static DbName of(String name) {

    if (name == null) {
      return null;
    }
    name = name.trim();
    if (name.isEmpty()) {
      return null;
    }
    boolean quoted = false;
    int len = name.length();
    if (len > 2) {
      char first = name.charAt(0);
      char last = name.charAt(len - 1);
      if (((first == '"') && (last == '"')) || ((first == '`') && (last == '`')) || ((first == '[') && (last == ']'))) {
        quoted = true;
        name = name.substring(1, len - 1);
      }
    }
    return new DbName(name, quoted);
  }

  /**
   * @param name the {@link DbName}. May be {@code null}.
   * @return the plain {@link DbName#get() name}. Will be {@code null} if {@link DbName} was {@code null}.
   */
  public static String getName(DbName name) {

    if (name == null) {
      return null;
    }
    return name.name;
  }

  /**
   * @param name the {@link DbName}. May be {@code null}.
   * @return the {@link DbName#toString() string representation}. Will be {@code null} if {@link DbName} was
   *         {@code null}.
   */
  public static String format(DbName name) {

    return format(name, null);
  }

  /**
   * @param name the {@link DbName}. May be {@code null}.
   * @param dialect the {@link DbDialect} to use.
   * @return the {@link DbName#toString() string representation}. Will be {@code null} if {@link DbName} was
   *         {@code null}.
   */
  public static String format(DbName name, DbDialect dialect) {

    if (name == null) {
      return null;
    }
    return name.toString(dialect);
  }

}
