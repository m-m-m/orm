/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.link.Link;
import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Person;
import io.github.mmm.orm.statement.Song;

/**
 * Test of {@link CreateTableClause} and {@link CreateTableStatement}.
 */
public class CreateTableTest extends DbStatementTest {

  private static final String JQL = """
      CREATE TABLE Song (
        Composer Link,
        Duration Long,
        Genre String,
        Id Id,
        Title String,
        TrackNo Integer,
        CONSTRAINT FK_Song_Composer FOREIGN KEY (Composer) REFERENCES Person(Id),
        CONSTRAINT NN_Song_Title NOT NULL (Title),
        CONSTRAINT PK_Song PRIMARY KEY (Id)
      )""";

  private static final String SQL = """
      CREATE TABLE SONG (
        COMPOSER Long,
        DURATION Long,
        GENRE String,
        ID Long,
        REV Long,
        TITLE String,
        TRACK_NO Integer,
        CONSTRAINT FK_SONG_COMPOSER FOREIGN KEY (COMPOSER) REFERENCES PERSON(ID),
        CONSTRAINT NN_SONG_TITLE NOT NULL (TITLE),
        CONSTRAINT PK_SONG PRIMARY KEY (ID)
      )""";

  /** Test of {@link CreateTableClause} that automatically creates all columns. */
  @Test
  public void testAuto() {

    // given
    Song s = Song.of();
    // temporary workaround
    s.Composer().set(Link.of(Id.of(Person.class, 4711L)));
    // when
    CreateTableStatement<Song> createTableStatement = new CreateTableClause<>(s).columns().get();
    // then
    check(createTableStatement, JQL, SQL);
  }

}
