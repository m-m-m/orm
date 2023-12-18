/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides repository based on {@code mmm-entity-bean}.
 */
module io.github.mmm.entity.repository.mem {

  requires transitive io.github.mmm.entity.bean;

  exports io.github.mmm.entity.repository.memory;

}
