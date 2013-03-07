package io.ous.concurrentunit.execution;

import io.ous.concurrentunit.util.OfferValueFuture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This execution strategy sends a set amount of executions at a given interval (fixed by either the rate of submition or delay between submissions) 
 * after a given delay period.
 * @author Asafh
 *
 */
public class ScheduledExecutionStrategy implements ExecutionStrategy {
	private final int count;
	private final FixedMode mode;
	private long delay;
	private long period;
	/**
	 * Used to determine if the delay given is between executions ("Delay") or between submits "Rate" 
	 * @author Asafh
	 *
	 */
	public static enum FixedMode {
		/**
		 * Delay is between executions, a delay (by GC or other) in a submit will delay the following executions
		 */
		Delay,
		/**
		 * Delay is between submits, a delay (by GC or other) in a submit will cause two (or more) closer submits after
		 */
		Rate
	}
	/**
	 * Same as ScheduledExecutionStrategy(count,delay,period, FixedMode.Rate)
	 * @param count
	 * @param delay
	 * @param period
	 */
	public ScheduledExecutionStrategy(int count, long delay, long period) {
		this(count, delay, period, FixedMode.Rate);
	}
	
	/**
	 * Scheduled an execution of <code>count</code> submits, starting after <code>delay</code> ms, with
	 * <code>period</code>ms interval, behaving as described in {@link FixedMode}
	 * @param count # of invocations
	 * @param delay ms delay to start
	 * @param period
	 * @param mode
	 */
	public ScheduledExecutionStrategy(int count, long delay, long period, FixedMode mode) {
		this.count = count;
		this.delay = delay;
		this.period = period;
		this.mode = mode;
	}
	public<V> Future<List<Future<V>>> run(final ExecutorService service, final Callable<V> target) {
		final Timer timer = new Timer();
		final OfferValueFuture<List<Future<V>>> ret = new OfferValueFuture<List<Future<V>>>();  //when are we done?
		final List<Future<V>> unsafe = new ArrayList<Future<V>>();
		final List<Future<V>> safe = Collections.synchronizedList(unsafe);
		final AtomicInteger submitCount = new AtomicInteger(0);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				int taskOrd = submitCount.getAndIncrement();
				if(taskOrd >= count) { //Enough
					timer.cancel();
					try {
						ret.offer(Collections.unmodifiableList(unsafe));
					}
					catch(IllegalStateException ise) {
						;//ignore, already offered by competing TimerTask execution
					}
				}
				else {
					Future<V> submit = service.submit(target);
					safe.add(submit);
				}
			}
		};
		if(mode == FixedMode.Delay) {
			timer.schedule(task, delay, period);
		}
		else {
			timer.scheduleAtFixedRate(task, delay, period);
		}
		
		return ret;
	}
	
	@Override
	public String toString() {
		return "SES:[Count="+count+", Delay="+delay+", Period="+period+", Mode="+mode+"]";
	}
}
