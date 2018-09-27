package process;

import java.util.concurrent.TimeUnit;

public class WaitTime {
	private final long time;
	private final TimeUnit unit;
	
	public WaitTime(long time, TimeUnit unit) {
		this.time = time;
		this.unit = unit;
	}	
	
	public long getTime() {
		return time;
	}
	public TimeUnit getUnit() {
		return unit;
	}	
}