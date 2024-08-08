package cn.labzen.tool.bean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TripleTest {

  @Test
  void testTriple() {
    Triple<String, Integer, Integer> numberTriple = new Triple<>("number", 123, 567);

    Triple<String, Integer, Integer> copy = numberTriple.copy();
    Assertions.assertEquals(numberTriple.getFirst(), copy.getFirst());

    Triple<String, Integer, Integer> copyOther = numberTriple.copy("new number", 456, 789);
    Assertions.assertNotEquals(numberTriple.getFirst(), copyOther.getFirst());

    MutableTriple<String, Integer, Integer> mutableTriple = numberTriple.toMutableTriple();
    mutableTriple.setFirst("changed number");
    Assertions.assertEquals(mutableTriple.getFirst(), "changed number");

    Triple<String, Integer, Integer> nullTriple = new Triple<>(null, null, null);
    Assertions.assertNull(nullTriple.getFirst());
  }

  @Test
  void testStrictTriple() {
    Assertions.assertThrows(NullPointerException.class, () -> new StrictTriple<>(null, null, ""));
  }
}
