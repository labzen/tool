package cn.labzen.tool.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static cn.labzen.tool.definition.Constants.PATTERN_OF_DATE_TIME;

class DateTimesTest {

  @Test
  void test() {
    ZoneOffset offset = OffsetDateTime.now().getOffset();

    Date now = new Date();
    Calendar instance = Calendar.getInstance();
    instance.setTime(now);

    LocalDateTime localDateTime = DateTimes.toLocalDateTime(now);
    Assertions.assertNotNull(localDateTime);
    Assertions.assertEquals(now.getTime(), localDateTime.toInstant(offset).toEpochMilli());

    LocalDate localDate = DateTimes.toLocalDate(now);
    Assertions.assertNotNull(localDate);
    Assertions.assertEquals(instance.get(Calendar.DAY_OF_YEAR), localDate.getDayOfYear());

    LocalTime localTime = DateTimes.toLocalTime(now);
    Assertions.assertNotNull(localTime);
    Assertions.assertEquals(instance.get(Calendar.MINUTE), localTime.getMinute());

    Date date1 = DateTimes.toDate(localDateTime);
    Date date2 = DateTimes.toDate(localDate, localTime);
    Assertions.assertEquals(date1, date2);
  }

  @Test
  void testHowLong() {
    // todo 需要大量的测试
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_OF_DATE_TIME);
    LocalDateTime time1 = LocalDateTime.parse("2021-11-01 15:12:11", formatter);
    System.out.println(DateTimes.howLong(time1, "y年c(前|后)"));
  }
}
