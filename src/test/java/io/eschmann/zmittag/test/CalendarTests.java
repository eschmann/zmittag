package io.eschmann.zmittag.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

public class CalendarTests {

	@Test
	public void testShouldEnsureDateIsInTheMorning() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(2014, 8, 1, 8, 00);
		long eightOClock = calendar.getTimeInMillis();
		calendar.roll(Calendar.HOUR_OF_DAY, 4);
		long twelveOClock = calendar.getTimeInMillis();

		long difference = twelveOClock - eightOClock;

		System.out.println(difference);

	}

}
