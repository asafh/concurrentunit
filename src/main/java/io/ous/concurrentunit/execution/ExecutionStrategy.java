package io.ous.concurrentunit.execution;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public interface ExecutionStrategy {
	/**
	 * Executes <code>target</code> according to this execution strategy
	 * @param service the executor service to utilize
	 * @param target the target for execution
	 * @return a future, who completes once all invocations have been submitted, and whose value is a list containing the Future<?> instances for each <code>target</code> invocation that has been submitted 
	 */
	public<V> Future<List<Future<V>>> run(ExecutorService service, Callable<V> target);
}
