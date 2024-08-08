package io.github.mmm.orm.memory;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssertJBugTest extends Assertions {

  @Test
  public void test() {

    Iterable<String> values = List.of("one", "two");
    assertThat(values).contains("two");
  }

}
