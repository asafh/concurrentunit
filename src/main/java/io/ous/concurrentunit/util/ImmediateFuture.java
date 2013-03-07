package io.ous.concurrentunit.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Returns a future that already has it's value computed, is never cancelled and is done from start.
 * @author Asafh
 *
 * @param <T>
 */
public final class ImmediateFuture<T> implements Future<T> {

	private final T value;
	
	public ImmediateFuture(T value) {
		this.value = value;
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return false; //Can never cancel
	}

	public boolean isCancelled() {
		return false; //Can never cancel
	}

	public boolean isDone() {
		return true;
	}

	public T get() throws InterruptedException, ExecutionException {
		return value;
	}

	public T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return value;
	}

}
