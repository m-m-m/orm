/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.LongId;
import io.github.mmm.entity.link.Link;
import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Person;
import io.github.mmm.orm.statement.Song;

/**
 * Test of {@link CreateTable} and {@link CreateTableStatement}.
 */
public class CreateTableTest extends DbStatementTest {

  /** Test of {@link CreateTable} that automatically creates all columns. */
  @Test
  public void testAuto() {

    // given
    Song s = Song.of();
    // temporary workaround
    s.Composer().set(Link.of(LongId.of(4711L, Person.class)));
    // when
    CreateTableStatement<Song> createTableStatement = new CreateTable<>(s).columns().get();
    // then
    check(createTableStatement, "CREATE TABLE Song (\n" //
        + "  Composer Link,\n" // types (Link, Long, String, etc.) are mapped to proper DB types if dialect is used
        + "  Duration Long,\n" //
        + "  Genre String,\n" //
        + "  Id Id,\n" // will actually be ID and REV columns in regular SQL (if dialect is used)
        + "  Title String,\n" //
        + "  TrackNo Integer,\n" //
        + "  CONSTRAINT FK_Song_Composer FOREIGN KEY (Composer) REFERENCES Person(Id),\n" //
        + "  CONSTRAINT NN_Song_Title NOT NULL (Title),\n" //
        + "  CONSTRAINT PK_Song PRIMARY KEY (Id)\n" //
        + ")");
  }

}
