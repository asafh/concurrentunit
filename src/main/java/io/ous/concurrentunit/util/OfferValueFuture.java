package io.ous.concurrentunit.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A future that waits until a value has been offered to it. <br/>
 * Can be thought of as a future that gets a value from a {@link BlockingQueue} of size 1 without removing the value
 * @author Asafh
 *
 * @param <V> the offered value type
 */
public class OfferValueFuture<V> implements Future<V> {
	private V value;
	private boolean done;
	public OfferValueFuture() {
		done = false;
	}
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}
	public boolean isCancelled() {
		return false;
	}
	public boolean isDone() {
		return done;
	}
	public V get() throws InterruptedException, ExecutionException {
		if(done) {
			return value;
		}
		synchronized(this) {
			while(!done) {
				wait();
			}
			return value;
		}
	}
	public V get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		if(done) {
			return value;
		}
		synchronized(this) {
			if(done) {
				return value;
			}
			
			wait(unit.toMillis(timeout)); //releases the monitor
			if(done) { //We were notified
				return value;
			}
			else { //We just timed out
				throw new TimeoutException();
			}
		}
	}
	/**
	 * If value was already set, throws a IllegalStateException exception. <br/>
	 * Sets the value <code>v</code> as the offered value and wakes up any thread waiting on a get(*) call
	 * @param v the value to set
	 * @throws IllegalStateException  if this method was already called.
	 */
	public void offer(V v) {
		synchronized(this) {
			if(done) {
				throw new IllegalStateException("Value already set");
			}
			value = v;
			done = true;
			this.notifyAll(); //Will notify all waiting in get(*) methods
		}
	}
}
