/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredWriter;

/**
 * {@link Marshalling} for {@link DbStatement}s.
 *
 * @since 1.0.0
 */
public class DbStatementMarshalling implements Marshalling<DbStatement<?>> {

  private static final DbStatementMarshalling INSTANCE = new DbStatementMarshalling();

  /**
   * The constructor.
   */
  protected DbStatementMarshalling() {

    super();
  }

  @Override
  public void writeObject(StructuredWriter writer, DbStatement<?> statement) {

    if (statement == null) {
      writer.writeValueAsNull();
      return;
    }
    writer.writeValueAsString(statement.toString());
  }

  @Override
  public DbStatement<?> readObject(StructuredReader reader) {

    return read(reader);
  }

  static DbStatement<?> read(StructuredReader reader) {

    DbStatementParser parser = DbStatementParser.get();
    String statementString = reader.readValueAsString();
    return parser.parse(statementString);
  }

  /**
   * @return the singleton instance of this {@link DbStatementMarshalling}.
   */
  public static DbStatementMarshalling get() {

    return INSTANCE;
  }

}
