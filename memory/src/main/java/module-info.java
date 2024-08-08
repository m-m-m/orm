/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides in-memory implementation of entity repository.
 *
 * @see io.github.mmm.orm.memory.SequenceMemoryRepository
 */
module io.github.mmm.orm.memory {

  requires transitive io.github.mmm.orm;

  exports io.github.mmm.orm.memory;

  exports io.github.mmm.orm.memory.index;

}
