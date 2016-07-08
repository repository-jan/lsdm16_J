package programming;

import java.util.concurrent.TimeUnit;

public class StopWatch {
	
	private long _time;
	
	public void startTimer() {
		_time = System.currentTimeMillis();
	}
	
	public String stopTime() {
		long stopTime = System.currentTimeMillis();
		long diff = stopTime - _time;
		
		String t = String.format("%d h, %d min, %d sec, %d ms",
				TimeUnit.MILLISECONDS.toHours(diff),
				TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
			    TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)),
			    TimeUnit.MILLISECONDS.toMillis(diff) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(diff)) );
		
		_time = System.currentTimeMillis();
		return t;
	}
}