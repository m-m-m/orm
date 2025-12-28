package io.github.mmm.orm.db.sqlite.jdbc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;

import io.github.mmm.orm.db.sqlite.dialect.SqliteDialect;
import io.github.mmm.orm.test.JdbcTest;

/**
 * Test of Sqlite database support.
 */
class SqliteJdbcTest extends JdbcTest {

  @BeforeAll
  static void setup() {

    Path path = Path.of("target").resolve(SqliteDialect.DEFAULT_DB);
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
