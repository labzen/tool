package cn.labzen.tool.bean;

import cn.labzen.tool.structure.MutableTriple;
import cn.labzen.tool.structure.Triple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TripleTest {

  @Test
  void testTriple() {
    Triple<String, Integer, Integer> numberTriple = new Triple<>("number", 123, 567);

    Triple<String, Integer, Integer> copy = numberTriple.copy();
    Assertions.assertEquals(numberTriple.first(), copy.first());

    Triple<String, Integer, Integer> copyOther = numberTriple.copyFirst(456, 789);
    Assertions.assertEquals(numberTriple.first(), copyOther.first());
    Assertions.assertNotEquals(numberTriple.second(), copyOther.second());

    MutableTriple<String, Integer, Integer> mutableTriple = numberTriple.mutable();
    mutableTriple.first("changed number");
    Assertions.assertEquals("changed number", mutableTriple.first());

    Triple<String, Integer, Integer> nullTriple = new Triple<>(null, null, null);
    Assertions.assertNull(nullTriple.first());
  }

}
