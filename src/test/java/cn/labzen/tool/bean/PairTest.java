package cn.labzen.tool.bean;

import cn.labzen.tool.structure.MutablePair;
import cn.labzen.tool.structure.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PairTest {

  @Test
  void testPair() {
    Pair<String, Integer> numberPair = new Pair<>("number", 123);

    Pair<String, Integer> copy = numberPair.copy();
    Assertions.assertEquals(numberPair.first(), copy.first());

    Pair<String, Integer> copyOther = numberPair.copyFirst(456);
    Assertions.assertEquals(numberPair.first(), copyOther.first());
    Assertions.assertNotEquals(numberPair.second(), copyOther.second());

    MutablePair<String, Integer> mutablePair = numberPair.mutable();
    mutablePair.first("changed number");
    Assertions.assertEquals("changed number", mutablePair.first());

    Pair<String, Integer> nullPair = new Pair<>(null, null);
    Assertions.assertNull(nullPair.first());
  }

}
