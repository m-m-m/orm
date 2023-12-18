/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides JDBC support for {@code mmm-orm}.
 */
module io.github.mmm.orm.jdbc {

  requires transitive io.github.mmm.orm.spi;

  requires transitive java.sql;

  exports io.github.mmm.orm.jdbc.access;

  exports io.github.mmm.orm.jdbc.connection;

}
