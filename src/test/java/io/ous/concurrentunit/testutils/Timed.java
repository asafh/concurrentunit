package io.ous.concurrentunit.testutils;

public class Timed {
	protected long start;
	protected long end;
	public Timed() {
		end = Long.MAX_VALUE;
	}
	public Timed(long start) {
		end = Long.MAX_VALUE;
		this.start = start;
	}
	public Timed(long start, long end) {
		this.start = start;
		this.end = end;
	}
	
	public long getElapsed() {
		return end-start;
	}
	public long getEnd() {
		return end;
	}
	public long getStart() {
		return start;
	}
	public void end() {
		end = System.currentTimeMillis();
	}
	@Override
	public String toString() {
		return "["+getElapsed()+"ms]";
	}
}