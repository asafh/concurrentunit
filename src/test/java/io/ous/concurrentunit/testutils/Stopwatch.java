package io.ous.concurrentunit.testutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stopwatch extends Timed {
	private static final int MAX_MARKS = 1000;
	private long[] marks;
	private int nextMark;

	public Stopwatch() {
		marks = new long[MAX_MARKS];
		nextMark = 0;
		start = System.currentTimeMillis();
	}
	
	public void mark() {
		marks[nextMark] = System.currentTimeMillis();
		++nextMark;
	}
	
	public List<Timed> getLaps() {
		if(nextMark == 0) {
			return Arrays.asList((Timed)this);
		}
		List<Timed> ret = new ArrayList<Timed>(nextMark+1);
		long start = this.start;
		long end;
		for(int i = 0; i < nextMark; ++i) {
			end = marks[i];
			ret.add(new Timed(start,end));
			start = end;
		}
		ret.add(new Timed(start,this.end));
		
		return ret;
		
	}
}
