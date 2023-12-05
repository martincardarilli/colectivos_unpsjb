package colectivo.util;

public class Time {

	public static int toMins(String s) {
		String[] hourMin = s.split(":");
		int hour = Integer.parseInt(hourMin[0]);
		int mins = Integer.parseInt(hourMin[1]);
		int hoursInMins = hour * 60;
		return hoursInMins + mins;
	}

	public static String toTime(int minutes) {
		int h = minutes / 60;
		int m = minutes % 60;
		return String.format("%d:%02d", h, m);
	}

}
