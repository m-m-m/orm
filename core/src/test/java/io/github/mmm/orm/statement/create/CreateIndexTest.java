/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import org.junit.jupiter.api.Test;

import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Song;

/**
 * Test of {@link CreateIndexClause} and {@link CreateIndexStatement}.
 */
public class CreateIndexTest extends DbStatementTest {

  /** Test of {@link CreateIndexClause} on a single column. */
  @Test
  public void testSingleColumn() {

    // arrange
    Song s = Song.of();
    // act
    CreateIndexStatement<Song> createIndexStatement = new CreateIndexClause("IDX_GENRE").on(s).column(s.Genre()).get();
    // assert
    check(createIndexStatement, "CREATE INDEX IDX_GENRE ON Song (Genre)", true);
  }

  /** Test of {@link CreateIndexClause} on multiple columns. */
  @Test
  public void testMultipleColumns() {

    // arrange
    Song s = Song.of();
    // act
    CreateIndexStatement<Song> createIndexStatement = new CreateIndexClause("IDX_GENRE").on(s).as("S")
        .columns(s.Title(), s.Genre()).get();
    // assert
    check(createIndexStatement, "CREATE INDEX IDX_GENRE ON Song S (S.Title,S.Genre)", true);
  }

}
